package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tairanliu
 */
@Data
public class UserExtra implements Serializable {
    private String xp = "";
    private String income = "";
    private String profession = "";
    private String education = "";
    private String aim = "";
    private String interests = "";
    private String tags = "";

    private String hideXp = "0";
    private String hideIncome = "0";
    private String hideProfession = "0";
    private String hideEducation = "0";
    private String hideAim = "0";
    private String hideInterests = "0";
    private String hideTags = "0";



    public String getInterests() {
        if(interests != null) {
            return interests.replaceAll("[\\]\\[]", "");
        }
        return "";
    }

    public String getTags() {
        if(tags != null) {
            return tags.replaceAll("[\\]\\[]", "");
        }
        return "";

    }

}
