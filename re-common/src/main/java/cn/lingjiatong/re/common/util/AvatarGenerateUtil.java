package cn.lingjiatong.re.common.util;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * 默认头像生成工具类
 * <p>
 * 用户未上传头像时，根据用户名首字母生成纯色背景头像图片
 *
 * @author Ling, Jiatong
 */
public class AvatarGenerateUtil {

    /**
     * 生成图片的边长（px）
     */
    public static final int AVATAR_SIZE = 256;

    /**
     * 默认头像背景色板（与前端 UserAvatar 组件保持一致）
     */
    private static final String[] PALETTE = {
            "#409eff", "#67c23a", "#e6a23c", "#f56c6c", "#9b59b6",
            "#16a085", "#d35400", "#2ecc71", "#e74c3c", "#909399"
    };

    private AvatarGenerateUtil() {
    }

    /**
     * 根据用户名生成默认头像图片字节数组（PNG格式）
     *
     * @param username 用户名
     * @return PNG图片字节数组
     * @throws Exception 图片生成失败时抛出
     */
    public static byte[] generateDefaultAvatar(String username) throws Exception {
        String letter = getInitialLetter(username);
        Color background = getBackgroundColor(username);

        BufferedImage image = new BufferedImage(AVATAR_SIZE, AVATAR_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try {
            // 抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // 纯色背景
            g.setColor(background);
            g.fillRect(0, 0, AVATAR_SIZE, AVATAR_SIZE);
            // 居中绘制首字母（白色粗体）
            int fontSize = (int) (AVATAR_SIZE * 0.45);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
            g.setColor(Color.WHITE);
            FontMetrics fontMetrics = g.getFontMetrics();
            int x = (AVATAR_SIZE - fontMetrics.stringWidth(letter)) / 2;
            int y = ((AVATAR_SIZE - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent();
            g.drawString(letter, x, y);
        } finally {
            g.dispose();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return outputStream.toByteArray();
    }

    /**
     * 根据用户名生成默认头像的data URI（data:image/png;base64,...），
     * 可直接存入数据库或作为图片地址使用
     *
     * @param username 用户名
     * @return data URI字符串
     * @throws Exception 图片生成失败时抛出
     */
    public static String generateDefaultAvatarDataUri(String username) throws Exception {
        byte[] bytes = generateDefaultAvatar(username);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 获取用户名首字母（大写），用户名为空时返回"用"
     *
     * @param username 用户名
     * @return 首字母大写字符串
     */
    private static String getInitialLetter(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "用";
        }
        return username.trim().substring(0, 1).toUpperCase();
    }

    /**
     * 根据用户名哈希从色板中确定背景色（同一用户名颜色固定）
     *
     * @param username 用户名
     * @return 背景色
     */
    private static Color getBackgroundColor(String username) {
        String key = username == null ? "" : username;
        // 与前端UserAvatar组件保持同一哈希算法（无符号32位），保证同一用户名前后端颜色一致
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = hash * 31 + key.charAt(i);
        }
        return Color.decode(PALETTE[Integer.remainderUnsigned(hash, PALETTE.length)]);
    }
}
