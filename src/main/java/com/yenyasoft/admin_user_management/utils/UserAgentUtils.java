package com.yenyasoft.admin_user_management.utils;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserAgentUtils.java
 */
public final class UserAgentUtils {

    private UserAgentUtils() {
    }

    public static String resolveBrowser(String userAgent) {
        if (isBlank(userAgent)) {
            return "Unknown";
        }
        String agent = userAgent.toLowerCase();
        if (agent.contains("edg")) {
            return "Edge";
        }
        if (agent.contains("opr") || agent.contains("opera")) {
            return "Opera";
        }
        if (agent.contains("chrome") && !agent.contains("chromium")) {
            return "Chrome";
        }
        if (agent.contains("firefox")) {
            return "Firefox";
        }
        if (agent.contains("safari")) {
            return "Safari";
        }
        return "Unknown";
    }

    public static String resolveOperatingSystem(String userAgent) {
        if (isBlank(userAgent)) {
            return "Unknown";
        }
        String agent = userAgent.toLowerCase();
        if (agent.contains("windows")) {
            return "Windows";
        }
        if (agent.contains("android")) {
            return "Android";
        }
        if (agent.contains("iphone") || agent.contains("ipad") || agent.contains("ios")) {
            return "iOS";
        }
        if (agent.contains("mac os") || agent.contains("macintosh")) {
            return "macOS";
        }
        if (agent.contains("linux")) {
            return "Linux";
        }
        return "Unknown";
    }

    public static String resolveDevice(String userAgent) {
        if (isBlank(userAgent)) {
            return "Unknown";
        }
        String agent = userAgent.toLowerCase();
        if (agent.contains("mobile") || agent.contains("android") || agent.contains("iphone")) {
            return "Mobile";
        }
        if (agent.contains("ipad") || agent.contains("tablet")) {
            return "Tablet";
        }
        return "Desktop";
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
