package com.corgi.user.entity;

import com.corgi.entity.CorgiPic;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserPic extends CorgiPic {
    private String userId;

    @Override
    public String getPicUrl() {
        if ("check".equals(this.getStatus()) && super.getPicUrl() != null && !super.getPicUrl().contains("?x-oss-process")) {
            return super.getPicUrl() + "?x-oss-process=style/mask";
        }
        if ("check".equals(this.getStatus())) {
            return super.getPicUrl();
        }
        if (super.getPicUrl() != null) {
            return super.getPicUrl().split("\\?")[0];
        }
        return super.getPicUrl();
    }
}
