package com.corgi.user.enums;

public enum MerchandiseEnum {


    FIRST_YEAR("S01", "first", 365),
    FIRST_HALF_YEAR("S02", "first", 181),
    FIRST_SEASON("S03", "first", 90),
    FIRST_MONTH("S04", "first", 30),
    YEAR("S05", "normal", 365),
    HALF_YEAR("S06", "normal", 181),
    SEASON("S07", "normal", 90),
    MONTH("S08", "normal", 30),
    ;

    MerchandiseEnum(String code, String type, Integer days) {
        this.code = code;
        this.type = type;
        this.days = days;
    }

    private final String code;
    private final String type;
    private final int days;

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public Integer getDays() {
        return days;
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
