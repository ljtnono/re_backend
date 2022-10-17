package cn.lingjiatong.re.common.util;

import cn.hutool.core.lang.UUID;

import java.util.Random;

/**
 * 随机工具类
 *
 * @author Ling, Jiatong
 * Date: 2020/8/20 18:59
 */
public class RandomUtil {

    private RandomUtil() {
    }

    public static RandomUtil getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RandomUtil INSTANCE = new RandomUtil();
    }

    public static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String NUMBER_CHAR = "0123456789";

    public static final String SYMBOL = "!@#$%^&*(){}[].?_`-,;:'|~0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 生成 8-16 位包含数字、字母、特殊字符的随机密码字符串
     *
     * @return 字符串
     */
    public String randomPassword() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        // 输出 8-16 位长度
        int length = random.nextInt(8) + 8;
        for (int i = 0; i < length; i++) {
            builder.append(SYMBOL.charAt(random.nextInt(SYMBOL.length())));
        }
        return builder.toString();
    }

    /**
     * 生成随机字符串数据
     *
     * @param length 随机串的长度
     * @return String 随机字符串
     */
    public String randomString(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
        }
        return builder.toString();
    }

    /**
     * 生成随机字母串（只含字母）
     *
     * @param length 随机串长度
     * @return String 随机字母串
     */
    public String randomLetter(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(LETTER_CHAR.charAt(random.nextInt(LETTER_CHAR.length())));
        }
        return builder.toString();
    }

    /**
     * 生成随机数字串（只含数字）
     *
     * @param length 随机串长度
     * @return String 随机数字串
     */
    public String randomNumber(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));
        }
        return builder.toString();
    }

    /**
     * 生成UUID
     *
     * @return UUID字符串
     */
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成简单UUID
     *
     * @return UUID字符串
     */
    public String generateSimpleUUID() {
        return UUID.randomUUID().toString(true);
    }

}
