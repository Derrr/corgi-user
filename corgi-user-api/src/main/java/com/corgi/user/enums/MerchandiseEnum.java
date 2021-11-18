package com.corgi.user.enums;

public enum MerchandiseEnum {


    FIRST_YEAR("S01", "first", 12),
    FIRST_HALF_YEAR("S02", "first", 6),
    FIRST_SEASON("S03", "first", 3),
    FIRST_MONTH("S04", "first", 1),
    YEAR("S05", "normal", 12),
    HALF_YEAR("S06", "normal", 6),
    SEASON("S07", "normal", 3),
    MONTH("S08", "normal", 1),
    ;

    MerchandiseEnum(String code, String type, Integer months) {
        this.code = code;
        this.type = type;
        this.months = months;
    }

    private final String code;
    private final String type;
    private final int months;

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public Integer getMonths() {
        return months;
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
