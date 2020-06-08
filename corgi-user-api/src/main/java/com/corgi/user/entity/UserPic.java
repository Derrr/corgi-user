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
        if ("check".equals(super.getStatus())) {
            return super.getPicUrl() + "?x-oss-process=style/mask";
        }
        return super.getPicUrl();
    }
}
