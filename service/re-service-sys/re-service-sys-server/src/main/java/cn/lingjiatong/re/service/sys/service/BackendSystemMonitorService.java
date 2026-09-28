package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorCPUVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorHardDiskVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorIOVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorMemoryVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * 后台系统监控service层
 * <p>
 * 采集当前运行主机的CPU、内存、硬盘信息（依赖oshi）
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 8:53 PM
 */
@Slf4j
@Service
public class BackendSystemMonitorService {

    private final SystemInfo systemInfo = new SystemInfo();

    // 上次磁盘、网络IO采样快照，用于计算速率
    private long prevDiskReadBytes = -1;
    private long prevDiskWriteBytes = -1;
    private long prevNetworkRecvBytes = -1;
    private long prevNetworkSendBytes = -1;
    private long prevIOSampleTime = -1;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取本机CPU信息
     *
     * @return 后台系统监控CPU VO对象
     */
    public BackendSystemMonitorCPUVO findCPUInfo() {
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 间隔一段时间后再取一次tick值，计算两次采样之间各类型的cpu占用率
        // oshi读取/proc/stat存在短暂缓存，循环采样直到tick发生变化（最多等待2s）
        long[] ticks = null;
        long deadline = System.currentTimeMillis() + 2000;
        while (System.currentTimeMillis() < deadline) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.toString(), e);
                throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
            }
            long[] current = processor.getSystemCpuLoadTicks();
            if (!Arrays.equals(current, prevTicks)) {
                ticks = current;
                break;
            }
        }
        if (ticks == null) {
            log.error("cpu占用信息获取异常");
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        // tick数组下标对应 CentralProcessor.TickType 的顺序：
        // 0 USER、1 NICE、2 SYSTEM、3 IDLE、4 IOWAIT、5 IRQ、6 SOFTIRQ、7 STEAL
        long total = 0;
        long[] delta = new long[prevTicks.length];
        for (int i = 0; i < prevTicks.length; i++) {
            delta[i] = ticks[i] - prevTicks[i];
            total += delta[i];
        }
        if (total <= 0) {
            log.error("cpu占用信息获取异常");
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        double userUsedPercent = scale2(delta[CentralProcessor.TickType.USER.getIndex()] * 100.0 / total);
        double systemUsedPercent = scale2((delta[CentralProcessor.TickType.SYSTEM.getIndex()]
                + delta[CentralProcessor.TickType.IRQ.getIndex()]
                + delta[CentralProcessor.TickType.SOFTIRQ.getIndex()]) * 100.0 / total);
        double freePercent = scale2(delta[CentralProcessor.TickType.IDLE.getIndex()] * 100.0 / total);

        BackendSystemMonitorCPUVO vo = new BackendSystemMonitorCPUVO();
        vo.setCpuCoreNum(processor.getLogicalProcessorCount());
        vo.setUserUsedPercent(userUsedPercent + "%");
        vo.setSystemUsedPercent(systemUsedPercent + "%");
        vo.setFreePercent(freePercent + "%");

        // 1/5/15分钟负载（Linux下取自/proc/loadavg，不可用时为负数）
        double[] loadAverage = processor.getSystemLoadAverage(3);
        if (loadAverage != null && loadAverage.length == 3) {
            vo.setLoadAverage1(loadAverage[0] >= 0 ? String.valueOf(scale2(loadAverage[0])) : null);
            vo.setLoadAverage5(loadAverage[1] >= 0 ? String.valueOf(scale2(loadAverage[1])) : null);
            vo.setLoadAverage15(loadAverage[2] >= 0 ? String.valueOf(scale2(loadAverage[2])) : null);
        }
        return vo;
    }

    /**
     * 获取本机内存信息
     *
     * @return 后台系统监控内存VO对象
     */
    public BackendSystemMonitorMemoryVO findMemoryInfo() {
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        long total = memory.getTotal();
        long available = memory.getAvailable();
        long used = total - available;
        if (total <= 0) {
            log.error("内存信息获取异常");
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        double totalMemory = scale2(total / 1024.0 / 1024 / 1024);
        double usedMemory = scale2(used / 1024.0 / 1024 / 1024);
        double availableMemory = scale2(available / 1024.0 / 1024 / 1024);
        double memoryUsedPercent = scale2(used * 100.0 / total);

        BackendSystemMonitorMemoryVO vo = new BackendSystemMonitorMemoryVO();
        vo.setTotalMemory(totalMemory + "GB");
        vo.setUsedMemory(usedMemory + "GB");
        vo.setAvailableMemory(availableMemory + "GB");
        vo.setMemoryUsedPercent(memoryUsedPercent + "%");
        return vo;
    }

    /**
     * 获取本机硬盘信息
     *
     * @return 后台系统监控硬盘VO对象列表
     */
    public List<BackendSystemMonitorHardDiskVO> findHardDiskInfo() {
        OperatingSystem os = systemInfo.getOperatingSystem();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fileStores = fileSystem.getFileStores();
        List<BackendSystemMonitorHardDiskVO> result = Lists.newArrayList();
        for (OSFileStore store : fileStores) {
            long total = store.getTotalSpace();
            long usable = store.getUsableSpace();
            long used = total - usable;

            BackendSystemMonitorHardDiskVO vo = new BackendSystemMonitorHardDiskVO();
            vo.setMountPoint(store.getMount());
            vo.setFileSystem(store.getType());
            vo.setTotalSize(formatSize(total));
            vo.setAvailableSize(formatSize(usable));
            vo.setUsedSize(formatSize(used));
            vo.setUsedPercent(total > 0 ? scale2(used * 100.0 / total) + "%" : "-");
            result.add(vo);
        }
        return result;
    }

    /**
     * 获取本机磁盘、网络IO速率
     * <p>
     * 通过两次请求之间的采样差值计算平均速率，首次调用返回0
     *
     * @return 后台系统监控磁盘网络IO VO对象
     */
    public synchronized BackendSystemMonitorIOVO findIOInfo() {
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        long diskReadBytes = 0;
        long diskWriteBytes = 0;
        long networkRecvBytes = 0;
        long networkSendBytes = 0;
        for (HWDiskStore diskStore : hal.getDiskStores()) {
            if (diskStore.updateAttributes()) {
                diskReadBytes += diskStore.getReadBytes();
                diskWriteBytes += diskStore.getWriteBytes();
            }
        }
        for (NetworkIF networkIF : hal.getNetworkIFs()) {
            if (networkIF.updateAttributes()) {
                networkRecvBytes += networkIF.getBytesRecv();
                networkSendBytes += networkIF.getBytesSent();
            }
        }

        long now = System.currentTimeMillis();
        BackendSystemMonitorIOVO vo = new BackendSystemMonitorIOVO();
        if (prevIOSampleTime > 0 && now > prevIOSampleTime) {
            double seconds = (now - prevIOSampleTime) / 1000.0;
            // 计数器可能被重置（如网卡重启），差值不为负
            vo.setDiskReadRate(formatRate(Math.max(0, diskReadBytes - prevDiskReadBytes) / seconds));
            vo.setDiskWriteRate(formatRate(Math.max(0, diskWriteBytes - prevDiskWriteBytes) / seconds));
            vo.setNetworkRecvRate(formatRate(Math.max(0, networkRecvBytes - prevNetworkRecvBytes) / seconds));
            vo.setNetworkSendRate(formatRate(Math.max(0, networkSendBytes - prevNetworkSendBytes) / seconds));
        } else {
            vo.setDiskReadRate("0KB/s");
            vo.setDiskWriteRate("0KB/s");
            vo.setNetworkRecvRate("0KB/s");
            vo.setNetworkSendRate("0KB/s");
        }
        prevDiskReadBytes = diskReadBytes;
        prevDiskWriteBytes = diskWriteBytes;
        prevNetworkRecvBytes = networkRecvBytes;
        prevNetworkSendBytes = networkSendBytes;
        prevIOSampleTime = now;
        return vo;
    }

    // ********************************私有函数********************************

    /**
     * 保留两位小数
     */
    private double scale2(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 将字节数格式化为可读大小（参照df -h风格）
     */
    private String formatSize(long bytes) {
        if (bytes < 0) {
            return "-";
        }
        if (bytes < 1024) {
            return bytes + "B";
        }
        double value = bytes;
        String[] units = {"B", "K", "M", "G", "T", "P"};
        int unitIndex = 0;
        while (value >= 1024 && unitIndex < units.length - 1) {
            value = value / 1024;
            unitIndex++;
        }
        return scale2(value) + units[unitIndex];
    }

    /**
     * 将速率（字节/秒）格式化为可读字符串
     */
    private String formatRate(double bytesPerSecond) {
        if (bytesPerSecond < 0) {
            return "-";
        }
        if (bytesPerSecond < 1024) {
            return scale2(bytesPerSecond) + "B/s";
        }
        if (bytesPerSecond < 1024 * 1024) {
            return scale2(bytesPerSecond / 1024) + "KB/s";
        }
        if (bytesPerSecond < 1024 * 1024 * 1024) {
            return scale2(bytesPerSecond / 1024 / 1024) + "MB/s";
        }
        return scale2(bytesPerSecond / 1024 / 1024 / 1024) + "GB/s";
    }

    // ********************************公共函数********************************

}
