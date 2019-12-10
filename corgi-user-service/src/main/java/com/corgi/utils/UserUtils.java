package com.corgi.utils;

import org.springframework.util.StringUtils;

/**
 * @author tairanliu
 */
public class UserUtils {

    public static String getConByBirthDay(String birthday) {
        if (StringUtils.isEmpty(birthday)) {
            return "";
        }
        String monthDay = birthday.substring(5);
        String sep = "/";
        if (monthDay.compareTo("03" + sep + "21") >= 0 && monthDay.compareTo("04" + sep + "20") <= 0) {
            return "白羊座";
        }
        if (monthDay.compareTo("04" + sep + "21") >= 0 && monthDay.compareTo("05" + sep + "20") <= 0) {
            return "金牛座";
        }
        if (monthDay.compareTo("05" + sep + "21") >= 0 && monthDay.compareTo("06" + sep + "21") <= 0) {
            return "双子座";
        }
        if (monthDay.compareTo("06" + sep + "22") >= 0 && monthDay.compareTo("07" + sep + "22") <= 0) {
            return "巨蟹座";
        }
        if (monthDay.compareTo("07" + sep + "23") >= 0 && monthDay.compareTo("08" + sep + "22") <= 0) {
            return "狮子座";
        }
        if (monthDay.compareTo("08" + sep + "23") >= 0 && monthDay.compareTo("09" + sep + "22") <= 0) {
            return "处女座";
        }
        if (monthDay.compareTo("09" + sep + "23") >= 0 && monthDay.compareTo("10" + sep + "22") <= 0) {
            return "天秤座";
        }
        if (monthDay.compareTo("10" + sep + "23") >= 0 && monthDay.compareTo("11" + sep + "21") <= 0) {
            return "天蝎座";
        }
        if (monthDay.compareTo("11" + sep + "22") >= 0 && monthDay.compareTo("12" + sep + "21") <= 0) {
            return "射手座";
        }
        if (monthDay.compareTo("12" + sep + "22") >= 0 || monthDay.compareTo("01" + sep + "19") <= 0) {
            return "摩羯座";
        }
        if (monthDay.compareTo("01" + sep + "20") >= 0 && monthDay.compareTo("02" + sep + "18") <= 0) {
            return "水瓶座";
        }
        if (monthDay.compareTo("02" + sep + "19") >= 0 && monthDay.compareTo("03" + sep + "20") <= 0) {
            return "双鱼座";
        }
        return "";
    }
}
