package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.service.sys.api.vo.FrontendFriendLinkListVO;
import cn.lingjiatong.re.service.sys.entity.SysFriendLink;
import cn.lingjiatong.re.service.sys.mapper.SysFriendLinkMapper;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 前端友情链接接口service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 23:17
 */
@Slf4j
@Service
public class FrontendFriendLinkService {

    @Resource
    private SysFriendLinkMapper sysFriendLinkMapper;

    // ********************************新增类接口********************************

    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    /**
     * 获取前端友情链接列表
     * 默认获取所有数据
     *
     * @return 前端友情链接列表VO对象列表
     */
    public List<FrontendFriendLinkListVO> findFrontendFriendLinkList() {
        List<SysFriendLink> sysFriendLinkList = sysFriendLinkMapper.selectList(new LambdaQueryWrapper<SysFriendLink>()
                        .select(SysFriendLink::getName, SysFriendLink::getUrl, SysFriendLink::getType, SysFriendLink::getFaviconUrl));
        List<FrontendFriendLinkListVO> result = Lists.newArrayList();

        sysFriendLinkList.forEach(link -> {
            FrontendFriendLinkListVO vo = new FrontendFriendLinkListVO();
            BeanUtils.copyProperties(link, vo);
            result.add(vo);
        });

        return result;
    }

}
