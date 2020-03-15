package com.corgi.support;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author tairanliu
 */
public class MatchSupporter {
    private static List<String> CON_LIST = Arrays.asList("白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座");

    private static List<Integer> CON_MATCH_LIST = Arrays.asList(80, 70, 90, 50, 100, 40, 60, 40, 100, 50, 90, 70);

    private static Double CON_FACTOR = 0.25;

    private static List<String> ROLE_LIST = Arrays.asList("0", "0.5-", "0.5", "0.5+", "1");

    private static Double ROLE_FACTOR = 0.25;

    public static double getConMatch(String con1, String con2) {
        if (StringUtils.isEmpty(con1) || StringUtils.isEmpty(con2)) {
            return 0;
        }
        int index1 = CON_LIST.indexOf(con1);
        int index2 = CON_LIST.indexOf(con2);
        int dis = Math.abs(index1 - index2);
        return CON_MATCH_LIST.get(dis) * CON_FACTOR;
    }

    public static double getRoleMatch(String role1, String role2) {
        if (StringUtils.isEmpty(role1) || StringUtils.isEmpty(role2)) {
            return 0;
        }
        int index1 = ROLE_LIST.indexOf(role1);
        int index2 = ROLE_LIST.indexOf(role2);
        int dis = Math.abs(index1 + index2 - 4);
        return (100 - dis * 25) * ROLE_FACTOR;
    }

    public static double getFactorMatch(String character1, String character2) {
        double match = 0.0;
        if (StringUtils.isEmpty(character1) || StringUtils.isEmpty(character2)) {
            return match;
        }
        String m1 = convertFactor(character1.charAt(4) + "");
        String m2 = convertFactor(character2.charAt(4) + "");
        if (m1.equals(m2)) {
            match = 7.5;
        } else {
            match = 6;
        }

        String m3 = convertFactor(character1.charAt(5) + "");
        String m4 = convertFactor(character2.charAt(5) + "");
        if (m3.equals(m4)) {
            match += 7.5;
        } else {
            match += 6;
        }
        return match;
    }

    private static String convertFactor(String m) {
        switch (m) {
            case "4":
                return "2";
            case "3":
                return "1";
            case "7":
                return "5";
            case "8":
                return "6";
            default:
                break;
        }
        return m;
    }
}
