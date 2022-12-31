package cn.lingjiatong.re.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 获取公钥接口controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/24 21:08
 */
@Slf4j
@RestController
@Api(tags = "公钥模块接口")
public class PublicKeyController {

    @Autowired
    private KeyPair keyPair;

    @GetMapping("/getPublicKey")
    @ApiOperation(value = "获取公钥", httpMethod = "GET")
    public Map<String, Object> getPublicKey() {
        log.info("==========获取公钥");
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
