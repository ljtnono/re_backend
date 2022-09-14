package cn.ljtnono.re.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 搜索类相关
 *
 * @author Ling, Jiatong
 * Date: 2021/10/2 1:36 上午
 */
@FeignClient("re-search")
@RequestMapping("/re/search")
public interface SearchFeign {


}
