package com.corgi.user.entity;

import com.corgi.entity.CorgiPic;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserPic extends CorgiPic{
    private static String MASK = "?x-oss-process=style/stylename";
    private String userId;

    @Override
    public String getPicUrl(){
        if(NEED_CHECK.equals(super.getStatus())){
            return super.getPicUrl()+MASK;
        }
        return super.getPicUrl();
    }
}
