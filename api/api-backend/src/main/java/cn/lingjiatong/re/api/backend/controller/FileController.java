package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.api.backend.service.FileService;
import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/12/28 20:44
 */
@Slf4j
@RestController
@RequestMapping("/file")
@Api(tags = "后端管理系统文件模块接口")
public class FileController {

    @Autowired
    private FileService fileService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 上传文件
     *
     * @param file 文件
     * @param currentUser 当前用户
     */
    @PostMapping("/uploadFile")
    @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "后端上传文件接口", httpMethod = "POST")
    public ResultVO<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "currentUser", required = false) User currentUser) {
        log.info("==========上传图片，参数：{}", file);
        return ResultVO.success(fileService.uploadFile(file));
    }

}
