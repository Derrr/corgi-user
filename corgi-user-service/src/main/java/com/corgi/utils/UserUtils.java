package com.corgi.utils;

import com.corgi.user.entity.UserQuery;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author tairanliu
 */
public class UserUtils {

    public static String getConByBirthDay(String birthday) {
        if (birthday == null) {
            return null;
        }
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

    public static boolean hasQuery(UserQuery query) {
        if (!CollectionUtils.isEmpty(query.getDateStatus())) {
            return true;
        }
        if (!CollectionUtils.isEmpty(query.getGroup())) {
            return true;
        }
        if (!CollectionUtils.isEmpty(query.getRole())) {
            return true;
        }
        if (query.getStartAge() != null && query.getStartAge() > 18) {
            return true;
        }
        if (query.getEndAge() != null && query.getEndAge() < 70) {
            return true;
        }
        if ("verify".equals(query.getType())) {
            return true;
        }
        if(!CollectionUtils.isEmpty(query.getInterests())){
            return true;
        }
        if(!CollectionUtils.isEmpty(query.getTags())){
            return true;
        }
        if(!CollectionUtils.isEmpty(query.getXp())){
            return true;
        }
        if(!CollectionUtils.isEmpty(query.getAim())){
            return true;
        }
        if(!CollectionUtils.isEmpty(query.getEducation())){
            return true;
        }
        if(!CollectionUtils.isEmpty(query.getIncome())){
            return true;
        }
        if(!CollectionUtils.isEmpty(query.getProfession())){
            return true;
        }

        return false;
    }

    public static void buildQueryString(UserQuery query) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if (!CollectionUtils.isEmpty(query.getDateStatus())) {
            sb.append(" and d.date_status in('").append(String.join("','", query.getDateStatus()));
            if (query.getDateStatus().contains("想聊天")) {
                sb.append("','");
            }
            sb.append("') ");
        }
        if (!CollectionUtils.isEmpty(query.getGroup())) {
            sb.append(" and d.group in('").append(String.join("','", query.getGroup())).append("') ");
        }
        if (!CollectionUtils.isEmpty(query.getRole())) {
            sb.append(" and d.role in('").append(String.join("','", query.getRole())).append("') ");
        }
        if (query.getStartAge() != null && query.getStartAge() > 18) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -1 * query.getStartAge());
            sb.append(" and d.birthday < '").append(sdf.format(calendar.getTime())).append("' ");
        }
        if (query.getEndAge() != null && query.getEndAge() < 70) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -1 * query.getEndAge());
            sb.append(" and d.birthday > '").append(sdf.format(calendar.getTime())).append("' ");
        }
        if ("verify".equals(query.getType())) {
            sb.append(" and d.avatar_check_status = 'verified' ");
        }
        query.setResult(sb.toString());
    }

    public static String getIndex(String userId) {
        return Math.floorMod(Integer.valueOf(userId), 8) + "";
    }
}
