package com.corgi.user.enums;

public enum MerchandiseEnum {


    FIRST_YEAR("S01", "first", 365, "首购 VIP/年 ¥150", "连续包年"),
    FIRST_HALF_YEAR("S02", "first", 181, "首购 VIP/半年 ¥125", "连续包半年"),
    FIRST_SEASON("S03", "first", 90, "首购 VIP/季 ¥87", "连续包季"),
    FIRST_MONTH("S04", "first", 30, "首购 VIP/月 ¥33.2", "连续包月"),
    YEAR("S05", "normal", 365, "VIP/年 ¥498", "连续包年"),
    HALF_YEAR("S06", "normal", 181, "VIP/半年 ¥249", "连续包半年"),
    SEASON("S07", "normal", 90, "VIP/季 ¥124", "连续包季"),
    MONTH("S08", "normal", 30, "VIP/月 ¥41", "连续包月"),
    READ_1("A01", "normal", 0, "付费阅读 ¥1", "付费阅读"),
    READ_3("A02", "normal", 0, "付费阅读 ¥3", "付费阅读"),
    READ_6("A03", "normal", 0, "付费阅读 ¥6", "付费阅读"),
    READ_12("A04", "normal", 0, "付费阅读 ¥12", "付费阅读"),
    READ_18("A05", "normal", 0, "付费阅读 ¥18", "付费阅读"),
    READ_30("A06", "normal", 0, "付费阅读 ¥30", "付费阅读"),
    MATCH_30("M01", "normal", 30, "15¥", "当日+30次"),
    MATCH_50("M02", "normal", 50, "25¥", "当日+50次"),
    MATCH_100("M03", "normal", 100, "45¥", "当日+100次"),
    ;

    MerchandiseEnum(String code, String type, Integer days, String desc, String title) {
        this.code = code;
        this.type = type;
        this.days = days;
        this.desc = desc;
        this.title = title;
    }

    private final String code;
    private final String type;
    private final int days;
    private final String desc;
    private final String title;

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public Integer getDays() {
        return days;
    }

    public String getDesc() {
        return desc;
    }

    public static boolean isFirst(String code) {
        MerchandiseEnum[] enums = MerchandiseEnum.values();
        for (int i = 0; i < enums.length; i++) {
            MerchandiseEnum e = enums[i];
            if (e.getCode().equals(code) && "first".equals(e.getType())) {
                return true;
            }
        }
        return false;
    }

    public static MerchandiseEnum getByCode(String code) {
        MerchandiseEnum[] enums = MerchandiseEnum.values();
        for (int i = 0; i < enums.length; i++) {
            MerchandiseEnum e = enums[i];
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
