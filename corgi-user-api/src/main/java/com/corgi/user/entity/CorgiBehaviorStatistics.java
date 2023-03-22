package com.corgi.user.entity;

import lombok.Data;

@Data
public class CorgiBehaviorStatistics {
    private Integer userCount;
    private Double totalSum;
    private Double userAvg;

    public Double getUserAvg() {
        if (totalSum == null || userCount == null) {
            return 0.0;
        }
        if (userCount == 0) {
            return 0.0;
        }
        return totalSum / userCount;
    }
}
