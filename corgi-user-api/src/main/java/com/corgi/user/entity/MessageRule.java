package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageRule implements Serializable{
    private String messageId;
    private String ruleKey;
    private String ruleValue;
}
