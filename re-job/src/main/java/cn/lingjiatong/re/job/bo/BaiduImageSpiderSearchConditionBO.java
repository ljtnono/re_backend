package cn.lingjiatong.re.job.bo;

import lombok.Data;
//import org.springframework.util.StringUtils;

/**
 * 百度图片查询条件BO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/13 21:08
 */
@Data
public class BaiduImageSpiderSearchConditionBO {

    /**
     * 图片的宽度
     * 单位：像素
     */
    private String width;

    /**
     * 图片的长度
     * 单位：像素
     */
    private String height;

    /**
     * 查询关键字
     */
    private String word;

    /**
     * 是否有版权
     *
     * 0 没有
     * 1 有
     */
    private String copyright;

    /**
     * 是否高清图片
     * 0 否
     * 1 是
     */
    private String hd;

    /**
     * 是否最新
     * 0 否
     * 1 是
     */
    private String latest;

    /**
     * 是否是动图
     *
     * 如果是动图，该值为%2C, 即逗号的URL编码
     * 如果不是动图，那么该值为空字符串
     */
    private String z;

    /**
     * 颜色
     * 仅支持以下几种颜色
     *
     * 白色：ic = 1024
     * 黑白：ic = 2048
     * 黑色：ic = 512
     * 粉色：ic = 64
     * 蓝色：ic = 16
     * 红色：ic = 1
     * 黄色：ic = 2
     * 绿色：ic = 4
     * 青色：ic = 8
     * 橙色：ic = 256
     * 棕色：ic = 128
     */
    private String ic;

    // 当只搜索不添加额外筛选条件时使用
    public static final String ONLY_SEARCH_CONDITION_PREFIX = "width=&height=&copyright=0&hd=0&latest=0&z=&ic=&word=";

//    /**
//     * 最终拼接出来的查询条件
//     */
//    private String condition;
//
//    public BaiduImageSpiderSearchConditionBO(String jobParam) {
//        if (StringUtils.isEmpty(jobParam)) {
//            // 默认不设置图片长、宽，无版权，不是高清图
//            condition = "width=&height=&word=&copyright=0&hd=0&latest=0&z=&ic=";
//        } else {
//            // 参数按照分号隔开
//            String[] split = jobParam.split(";");
//            //
//        }
//    }
//
//    @Override
//    public String toString() {
//        return condition;
//    }
}
