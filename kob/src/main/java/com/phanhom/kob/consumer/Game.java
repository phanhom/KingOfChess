package com.phanhom.kob.consumer;

import java.util.Random;

public class Game {
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private final int[][] g;
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
//    private final Plane p1, p2;

    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        this.createMap();
//        p1 = new Plane(idA, rows / 2, cols / 2);
//        p2 = new Plane(idB, rows / 2 + 1, cols / 2 + 1);
    }

    public int[][] getG() {
        return g;
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (createMapImpl()) {
                break;
            }
        }
    }

    private boolean createMapImpl() {
        Random random = new Random();

        // 初始化地图，所有位置默认无墙
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                g[r][c] = 0;
            }
        }

        // 设置边界墙
        for (int r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        // 随机创建内部墙
        for (int i = 0; i < inner_walls_count / 2; i++) {
            for (int j = 0; j < 1500; j++) { // 允许尝试 1500 次以找到合适位置
                int r = random.nextInt(rows);
                int c = random.nextInt(cols);
                if (g[r][c] == 1 || g[c][r] == 1) {
                    continue;
                }
                if ((r == rows - 2 && c == 1) || (r == 1 && c == cols - 2)) {
                    continue; // 避免封堵起点和终点
                }
                g[r][c] = g[c][r] = 1;
                break;
            }
        }

        return checkConnectivity(rows - 2, 1, 1, cols - 2);
    }


    private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < rows && y >= 0 && y < cols && g[x][y] == 0) {
                if (checkConnectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }
}
