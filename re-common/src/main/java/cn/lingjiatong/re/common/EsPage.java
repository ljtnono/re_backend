package cn.lingjiatong.re.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * es分页查询结果包装类
 *
 * @author Ling, Jiatong
 * Date: 2023/1/9 20:38
 */
@Schema(name = "EsPage", description = "es分页查询结果包装类")
public class EsPage<T> implements IPage<T> {

    /**
     * 总条数
     */
    @Schema(description = "总条数")
    private long total;

    /**
     * 当前页数
     */
    @Schema(description = "当前页数")
    private long current;

    /**
     * 每页条数
     */
    @Schema(description = "每页条数")
    private long size;

    /**
     * 记录
     */
    @Schema(description = "记录")
    private List<T> records;

    @Override
    public List<OrderItem> orders() {
        return null;
    }

    @Override
    public List<T> getRecords() {
        return records;
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public IPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return current;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }
}
