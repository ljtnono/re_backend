package cn.lingjiatong.re.service.sys.mapper;

import cn.lingjiatong.re.service.sys.entity.SpBaiduImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 百度图片爬虫数据mapper层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/12 21:51
 */
public interface SpBaiduImgMapper extends BaseMapper<SpBaiduImg> {


    /**
     * 默认获取10张长宽比在 1.8 - 2.0之间的图片
     *
     * @return 图片url列表
     */
    List<String> getFrontendSwiperImageList();
}
