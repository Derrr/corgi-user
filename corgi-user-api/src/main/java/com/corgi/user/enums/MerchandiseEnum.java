package com.corgi.user.enums;

public enum MerchandiseEnum {


    FIRST_YEAR("S01", "first"),
    FIRST_HALF_YEAR("S02", "first"),
    FIRST_SEASON("S03", "first"),
    FIRST_MONTH("S04", "first"),
    YEAR("S05", "normal"),
    HALF_YEAR("S06", "normal"),
    SEASON("S07", "normal"),
    MONTH("S08", "normal"),
    ;

    MerchandiseEnum(String code, String type) {
        this.code = code;
        this.type = type;
    }

    private final String code;
    private final String type;

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
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
}
