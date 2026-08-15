package com.sunsun.adminspringboot.util;

/**
 * 简单的 User-Agent 解析（浏览器 / 操作系统）
 */
public class UserAgentUtil {

    /** 解析浏览器 */
    public static String parseBrowser(String ua) {
        if (ua == null || ua.isBlank()) return "未知";
        ua = ua.toLowerCase();
        if (ua.contains("edg/")) return "Edge";
        if (ua.contains("chrome")) return "Chrome";
        if (ua.contains("firefox")) return "Firefox";
        if (ua.contains("safari")) return "Safari";
        if (ua.contains("opera") || ua.contains("opr/")) return "Opera";
        if (ua.contains("msie") || ua.contains("trident")) return "IE";
        return "其他";
    }

    /** 解析操作系统 */
    public static String parseOs(String ua) {
        if (ua == null || ua.isBlank()) return "未知";
        ua = ua.toLowerCase();
        if (ua.contains("windows")) return "Windows";
        if (ua.contains("mac os")) return "Mac OS";
        if (ua.contains("android")) return "Android";
        if (ua.contains("iphone") || ua.contains("ipad")) return "iOS";
        if (ua.contains("linux")) return "Linux";
        return "其他";
    }
}
