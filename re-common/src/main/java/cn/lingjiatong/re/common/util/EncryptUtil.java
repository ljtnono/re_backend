package cn.lingjiatong.re.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加解密工具
 *
 * @author Ling, Jiatong
 * Date: 2020/7/8 9:49 上午
 */
@Slf4j
public class EncryptUtil {

    /**
     * 工具类不允许实例化
     */
    private EncryptUtil() {
    }

    public static EncryptUtil getInstance() {
        return Holder.INSTANCE;
    }

    public static class Holder {
        private final static EncryptUtil INSTANCE = new EncryptUtil();
    }

    /**
     * 使用MD5对字符串进行加密
     *
     * @param source 源字符串
     * @return MD5加密后的字符串
     */
    public String getMd5(String source) {
        return getMd5(source.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * MD5加密算法
     *
     * @param source 源字节数组
     * @return 加密后的字符串
     */
    private String getMd5(byte[] source) {
        String s = null;
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] tmp;
            synchronized (EncryptUtil.class) {
                md.update(source);
                tmp = md.digest();
            }
            char[] str = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            // 换后的结果转换为字符串
            s = new String(str);
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return s;
    }

    /**
     * MD5加密转小写
     *
     * @param source 源字符串
     * @return MD5小写加密字符串
     */
    public String getMd5LowerCase(String source) {
        return getMd5(source).toLowerCase();
    }

    /**
     * MD5加密转大写
     *
     * @param source 源字符串
     * @return MD5大写加密
     */
    public String getMd5UpperCase(String source) {
        return getMd5(source).toUpperCase();
    }


    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回加密数据
     */
    public String encryptAES(String content, String password) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getAESSecretKey(password));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //通过Base64转码返回
            return new String(Base64Utils.encode(result));
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }
        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content  密文
     * @param password 解密秘钥
     * @return 解密之后的明文
     */
    public String decryptAES(String content, String password) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance("AES");
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getAESSecretKey(password));
            //执行操作
            byte[] decode = Base64Utils.decode(content.getBytes(StandardCharsets.UTF_8));
            byte[] result = cipher.doFinal(decode);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @param password 解密秘钥
     * @return SecretKeySpec 解密秘钥
     */
    private SecretKeySpec getAESSecretKey(String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            //AES 要求密钥长度为 128
            kg.init(128, random);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex.toString(), ex);
        }
        return null;
    }
}
