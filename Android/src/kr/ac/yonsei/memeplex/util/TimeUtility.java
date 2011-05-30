package kr.ac.yonsei.memeplex.util;

public class TimeUtility {
    public static String getTimePassedString(int seconds) {
        if (seconds >= 60 * 60 * 24 * 30)
            return seconds / (60 * 60 * 24 * 30) + "개월 전";
        else if (seconds >= 60 * 60 * 24)
            return seconds / (60 * 60 * 24) + "일 전";
        else if (seconds >= 60 * 60)
            return seconds / (60 * 60) + "시간 전";
        else if (seconds >= 60)
            return seconds / 60 + "분 전";
        return "방금 전";
    }
}
