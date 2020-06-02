package com.corgi.support;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author tairanliu
 */
public class MatchSupporter {
    private static List<String> CON_LIST = Arrays.asList("白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座");

    public static List<String> CON_MATCH_LIST = Arrays.asList("76", "62", "90", "34", "100", "20", "48", "20", "100", "34", "90", "62");
    public static List<String> INT_MATCH_LIST = Arrays.asList("50", "10");

    public static Double CON_FACTOR = 0.1;
    public static Double ROLE_FACTOR = 0.25;
    public static Double CHARA_FACTOR = 0.25;
    public static Double PREFER_FACTOR = 0.25;
    public static Double FACTOR = 0.15;

    //private static List<String> ROLE_LIST = Arrays.asList("0", "0.5-", "0.5", "0.5+", "1");


    public static double getConMatch(String con1, String con2) {
        if (StringUtils.isEmpty(con1) || StringUtils.isEmpty(con2)) {
            return 0;
        }
        int index1 = CON_LIST.indexOf(con1);
        int index2 = CON_LIST.indexOf(con2);
        if (index1 < 0 || index2 < 0) {
            return 0;
        }
        int dis = Math.abs(index1 - index2);
        return Integer.valueOf(CON_MATCH_LIST.get(dis)) * CON_FACTOR;
    }

    public static double getFactorMatch(String character1, String character2) {
        double match = 0.0;
        if (StringUtils.isEmpty(character1) || StringUtils.isEmpty(character2)) {
            return match;
        }
        String m1 = convertFactor(character1.charAt(4) + "");
        String m2 = convertFactor(character2.charAt(4) + "");
        if (m1.equals(m2)) {
            match = Integer.valueOf(INT_MATCH_LIST.get(0)) * FACTOR;
        } else {
            match = Integer.valueOf(INT_MATCH_LIST.get(1)) * FACTOR;
        }

        String m3 = convertFactor(character1.charAt(5) + "");
        String m4 = convertFactor(character2.charAt(5) + "");
        if (m3.equals(m4)) {
            match += Integer.valueOf(INT_MATCH_LIST.get(0)) * FACTOR;
        } else {
            match += Integer.valueOf(INT_MATCH_LIST.get(1)) * FACTOR;
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
