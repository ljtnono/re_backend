package cn.lingjiatong.re.api.file.service;

import cn.lingjiatong.re.common.constant.MinioConstant;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.common.exception.ServerException;
import cn.lingjiatong.re.common.util.MinioUtil;
import cn.lingjiatong.re.common.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

    /**
     * MinIO 浏览器/前端访问的公网地址（区别于内部SDK连接地址 minio.endpoint），
     * 为空时回退到用内部endpoint生成的预签名地址
     */
    @Value("${minio.url:}")
    private String minioPublicUrl;

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
            // 优先使用公网地址拼接（bucket为公共读，无需签名参数），
            // 避免把docker内网地址（如http://re-minio:9000）返回给前端导致浏览器无法访问
            if (StringUtils.hasLength(minioPublicUrl)) {
                return minioPublicUrl + "/" + MinioConstant.MINIO_BUCKET_NAME + "/" + objectName;
            }
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
