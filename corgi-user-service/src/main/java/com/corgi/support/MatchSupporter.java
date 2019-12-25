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

    private static Double CON_FACTOR = 0.05;

    private static List<String> ROLE_LIST = Arrays.asList("BTM", "VERSBTM", "VERS", "VERSTOP", "TOP");

    private static Double ROLE_FACTOR = 0.45;

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
        char m1 = character1.charAt(4);
        char m2 = character2.charAt(4);
        if (m1 == m2) {
            match = 0.1;
        }

        char m3 = character1.charAt(5);
        char m4 = character2.charAt(5);
        if (m3 == m4 && ("7".equals(m3 + "") || "8".equals(m3 + ""))) {
            match += 0.1;
        } else if ("5".equals(m3 + "") || "6".equals(m3 + "")) {
            match += 0.1;
        }
        return match;
    }
}
