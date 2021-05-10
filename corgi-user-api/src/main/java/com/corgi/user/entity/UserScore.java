package com.corgi.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tairanliu
 */
@Data
public class UserScore implements Serializable {
    private String userId;
    private Double base;
    private Double operation;
    private Double evaluation;
    private Double total;

    public Double getTotal() {
        total = 0.0;
        if (base != null) {
            total += base * 0.1;
        }
        if (operation != null) {
            total += operation * 0.2;
        }
        if (evaluation != null) {
            total += evaluation * 0.7;
        }
        return total;
    }


}
