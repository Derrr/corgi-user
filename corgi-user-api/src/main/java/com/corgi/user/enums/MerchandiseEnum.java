package com.corgi.user.enums;

public enum MerchandiseEnum {


    FIRST_YEAR("S01", "first", 365,"首购 VIP/年 ¥150"),
    FIRST_HALF_YEAR("S02", "first", 181,"首购 VIP/半年 ¥125"),
    FIRST_SEASON("S03", "first", 90,"首购 VIP/季 ¥87"),
    FIRST_MONTH("S04", "first", 30, "首购 VIP/月 ¥33.2"),
    YEAR("S05", "normal", 365,"VIP/年 ¥498"),
    HALF_YEAR("S06", "normal", 181,"VIP/半年 ¥249"),
    SEASON("S07", "normal", 90,"VIP/季 ¥124"),
    MONTH("S08", "normal", 30,"VIP/月 ¥41"),
    ;

    MerchandiseEnum(String code, String type, Integer days, String desc) {
        this.code = code;
        this.type = type;
        this.days = days;
        this.desc = desc;
    }

    private final String code;
    private final String type;
    private final int days;
    private final String desc;

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public Integer getDays() {
        return days;
    }

    public String getDesc(){
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
