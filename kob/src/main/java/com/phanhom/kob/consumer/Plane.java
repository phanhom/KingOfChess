package com.phanhom.kob.consumer;

import java.util.ArrayList;
import java.util.List;

public class Plane {
    private Integer id;
    private Integer r, c;
    private double x, y;
    private Integer direction;
    private double speed;
    private double score;
    private String status; // "idle", "moving", "dead"
    private List<Integer> steps;

    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    public Plane(int id, int r, int c) {
        this.id = id;
        this.r = r;
        this.c = c;
        this.x = c + 0.5;
        this.y = r + 0.5;
        this.speed = 4.0;
        this.score = 1.0;
        this.status = "idle";
        this.direction = (id == 1) ? 2 : 0;
    }

}
