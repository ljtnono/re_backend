package cn.lingjiatong.re.api.backend.service;

import cn.lingjiatong.re.common.constant.MinioConstant;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.common.exception.ServerException;
import cn.lingjiatong.re.common.util.MinioUtil;
import cn.lingjiatong.re.common.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * 图片模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/12/28 21:43
 */
@Slf4j
@Service
public class FileService {

    @Autowired
    private MinioUtil minioUtil;

    // ********************************新增类接口********************************

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件访问地址
     */
    public String uploadFile(MultipartFile file) {
        Optional.ofNullable(file)
                .orElseThrow(() -> new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR));
        try {
            String objectName =  MinioConstant.ARTICLE_QUOTE_FOLDER + "/" + file.getOriginalFilename();
            minioUtil.uploadFile(MinioConstant.MINIO_BUCKET_NAME, file, objectName, file.getContentType());
            String urlWithParam = minioUtil.getPresignedObjectUrl(MinioConstant.MINIO_BUCKET_NAME, objectName);
            return UrlUtil.removeUrlParameter(urlWithParam);
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.MINIO_SERVER_ERROR);
        }
    }

    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
