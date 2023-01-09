package cn.lingjiatong.re.common;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分页查询基类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/31 20:46
 */
@Data
@Schema(name = "BasePageDTO", description = "分页查询基类")
public class BasePageDTO {

    /**
     * 当前页
     */
    @Schema(description = "分页查询页数 不传默认为1", example = "1", type = "Integer")
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    @Schema(description = "每页条数 不传默认为10", example = "10", type = "Integer")
    private Integer pageSize = 10;

    /**
     * 排序条件列表
     */
    @Schema(description = "排序条件列表")
    private List<String> orderFieldList;

    /**
     * 排序条件列表
     */
    @Schema(description = "排序条件列表")
    private List<Byte> orderFlagList;

    /**
     * 排序字段限定列表，由子类覆盖
     */
    @Schema(description = "排序字段限定列表", hidden = true)
    protected List<String> orderFieldLimitList;

    /**
     * 生成的排序条件
     */
    @Schema(description = "生成的排序条件", hidden = true)
    private String order;

    public BasePageDTO() {
        orderFieldList = Lists.newArrayList();
        orderFlagList = Lists.newArrayList();
    }

    /**
     * 根据排序字段和排序类型列表生成排序ORDER BY语句
     *
     * @author Ling, Jiatong
     */
    public void generateOrderCondition() {
        StringBuilder orderCondition = new StringBuilder();
        List<String> orderFieldList = getOrderFieldList();
        List<Byte> orderFlagList = getOrderFlagList();
        if (!CollectionUtils.isEmpty(orderFieldList)) {
            // 检查排序值取值范围是否正确
            for (String field : orderFieldList) {
                // 如果不是取值返回内的数，抛出请求参数异常
                if (!getOrderFieldLimitList().contains(field)) {
                    throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), "排序参数异常");
                }
            }
            // 如果没有传排序方式，那么全部按照降序排
            if (CollectionUtils.isEmpty(orderFlagList)) {
                orderFlagList = new ArrayList<>();
                for (int i = 0; i < orderFieldList.size(); i++) {
                    orderFlagList.add(CommonConstant.ORDER_BY_ASC);
                }
            }
            // 如果有传排序方式
            if (!CollectionUtils.isEmpty(orderFlagList)) {
                for (int i = 0; i < orderFieldList.size(); i++) {
                    orderCondition.append(orderFieldList.get(i)).append(" ");
                    if (orderFlagList.get(i) != null) {
                        // 排序方式只有这两种
                        if (orderFlagList.get(i) == 1) {
                            orderCondition.append("DESC");
                        } else {
                            // 传其他的默认为升序
                            orderCondition.append("ASC");
                        }
                    }
                    if (i < orderFieldList.size() - 1) {
                        orderCondition.append(",");
                    }
                }
                // 拼接排序参数
                setOrder(orderCondition.toString());
            }
        }
    }
}
