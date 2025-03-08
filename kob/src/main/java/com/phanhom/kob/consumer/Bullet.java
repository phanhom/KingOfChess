package com.phanhom.kob.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bullet {
    private Integer id;
    private double x, y;
    private double speed;
    private Integer direction;
    private String status; // false 代表死亡 true 代表存活
}
