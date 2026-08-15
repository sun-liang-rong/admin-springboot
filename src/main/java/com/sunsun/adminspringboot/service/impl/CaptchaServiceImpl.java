package com.sunsun.adminspringboot.service.impl;

import com.sunsun.adminspringboot.dto.response.CaptchaResult;
import com.sunsun.adminspringboot.service.CaptchaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图形验证码服务
 * <p>验证码存储于内存（ConcurrentHashMap），5 分钟过期、一次性使用；
 * 可通过配置 app.captcha.enabled=false 全局关闭（登录时不再校验）。</p>
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    /** 验证码字符集（去除易混淆的 0/O、1/l/I） */
    private static final String CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final long EXPIRE_MS = 5 * 60 * 1000L;

    @Value("${app.captcha.enabled:true}")
    private boolean captchaEnabled;

    /** captchaId -> 验证码记录 */
    private final Map<String, CaptchaRecord> store = new ConcurrentHashMap<>();

    private record CaptchaRecord(String code, long createTime) {}

    @Override
    public CaptchaResult generate() {
        String code = randomCode(4);
        String id = UUID.randomUUID().toString().replace("-", "");
        store.put(id, new CaptchaRecord(code, System.currentTimeMillis()));
        return new CaptchaResult(id, draw(code));
    }

    @Override
    public boolean verify(String captchaId, String captchaCode) {
        // 验证码关闭时直接放行（开发环境便捷开关）
        if (!captchaEnabled) return true;
        if (captchaId == null || captchaId.isEmpty() || captchaCode == null || captchaCode.isEmpty()) {
            return false;
        }
        CaptchaRecord record = store.remove(captchaId);
        if (record == null) return false;
        // 过期
        if (System.currentTimeMillis() - record.createTime() > EXPIRE_MS) return false;
        return record.code().equalsIgnoreCase(captchaCode.trim());
    }

    private String randomCode(int length) {
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    /** 绘制验证码图片并返回 base64 data URL */
    private String draw(String code) {
        try {
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            // 抗锯齿 + 背景
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(248, 245, 240));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            // 干扰线
            java.util.Random random = new java.util.Random();
            for (int i = 0; i < 6; i++) {
                g.setColor(new Color(190 + random.nextInt(50), 180 + random.nextInt(50), 160 + random.nextInt(50)));
                g.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT), random.nextInt(WIDTH), random.nextInt(HEIGHT));
            }
            // 干扰点
            for (int i = 0; i < 40; i++) {
                g.setColor(new Color(180 + random.nextInt(70), 180 + random.nextInt(70), 170 + random.nextInt(60)));
                g.fillRect(random.nextInt(WIDTH), random.nextInt(HEIGHT), 1, 1);
            }
            // 字符（随机旋转、颜色）
            int charWidth = WIDTH / (code.length() + 1);
            for (int i = 0; i < code.length(); i++) {
                g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24 + random.nextInt(4)));
                g.setColor(new Color(90 + random.nextInt(90), 80 + random.nextInt(80), 70 + random.nextInt(70)));
                double angle = (random.nextDouble() - 0.5) * 0.5;
                g.rotate(angle, charWidth * (i + 0.7), HEIGHT / 2 + 4);
                g.drawString(String.valueOf(code.charAt(i)), charWidth * i + 8, HEIGHT / 2 + 6);
                g.rotate(-angle, charWidth * (i + 0.7), HEIGHT / 2 + 4);
            }
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("验证码生成失败", e);
        }
    }
}
