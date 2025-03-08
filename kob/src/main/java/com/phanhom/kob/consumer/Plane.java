package com.phanhom.kob.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plane {
    private Integer id;
    private Integer direction;
    private double x, y;
    private double speed;
    private double score;
    private String status; // "idle", "moving", "dead"
}
