package com.phanhom.kob.consumer;

import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
// 检查死锁
public class Game extends Thread {
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private final int[][] g;
    private static final Integer timeDelta = 25;
    private final static int[] dx = {0, 1, 0, -1, 0}, dy = {-1, 0, 1, 0, 0};
    private final static double collision_eps = 0.5; // 碰撞检测的精度
    private final static double bullet_collision_eps = 0.3; // 子弹碰撞检测的精度
    private final Plane p1, p2;
    private Integer candy_x = -1, candy_y = -1;
    private Integer eaten = -1;
    private String status = "playing";
    private Integer nextStepA = 1, nextStepB = 1;
    private Integer totalTime = 2400, timeUsed = 0;
    private ArrayList<Bullet> bullets = new ArrayList<>();
//    private
    private ReentrantLock lock = new ReentrantLock();

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        this.createMap();
        // 类似于第四象限 x = col, y = rows
        p1 = new Plane(idA, 0, 1 + 0.5, rows - 2 + 0.5, 4, 2, "idle");
        p2 = new Plane(idB, 2, cols - 2 + 0.5, 1 + 0.5, 4, 2, "idle");
    }

    public Plane getP1() {
        lock.lock();
        try {
            return p1;
        } finally {
            lock.unlock();
        }
    }

    public Plane getP2() {
        lock.lock();
        try {
            return p2;
        } finally {
            lock.unlock();
        }
    }

    public int[][] getG() {
        return g;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            p1.setDirection(nextStepA);
            p1.setStatus("moving");
//            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            p2.setDirection(nextStepB);
            p2.setStatus("moving");
//            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public Integer getNextStepA() {
        lock.lock();
        try {
            return p1.getDirection();
//            return nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public Integer getNextStepB() {
        lock.lock();
        try {
            return p2.getDirection();
//            return nextStepB;
        } finally {
            lock.unlock();
        }
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

    public void updateMove() {
        lock.lock();
        try {
            if(p1.getStatus() == "moving") {
//                System.out.println("--------");
//                System.out.println(p1.getX());
                double moveDistance1 = p1.getSpeed() * timeDelta / 1000;
                p1.setX(p1.getX() + dx[p1.getDirection()] * moveDistance1);
                p1.setY(p1.getY() + dy[p1.getDirection()] * moveDistance1);
            }
            if(p2.getStatus() == "moving") {
                double moveDistance2 = p2.getSpeed() * timeDelta / 1000;
                p2.setX(p2.getX() + dx[p2.getDirection()] * moveDistance2);
                p2.setY(p2.getY() + dy[p2.getDirection()] * moveDistance2);
            }
//            System.out.println(p1.getX());
//            System.out.println(p2.getX());
//            System.out.println("test");
        } finally {
            lock.unlock();
        }

    }

    private boolean checkValid(boolean time_over) {
        lock.lock();
        try {
            if(!status.equals("playing")) return false;
            if(time_over) {
                if(p1.getScore() == p2.getScore()) {
                    status = "deuce";
                } else if (p1.getScore() > p2.getScore()) {
                    status = "p1";
                } else {
                    status = "p2";
                }
                return false;
            }
            // 检查飞机相撞
            if (Math.abs(p1.getX() - p2.getX()) < collision_eps && Math.abs(p1.getY() - p2.getY()) < collision_eps) {
//                System.out.println(p1.getX());
//                System.out.println(p1.getY());
//                System.out.println(p2.getX());
//                System.out.println(p2.getY());
                System.out.println("check_valid() => plane hit plane");
                status = "deuce"; // 设置游戏状态为结束
                return false;
            }

            // 检查飞机撞墙
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (g[r][c] == 1) { // 墙的位置
                        if (Math.abs(r + 0.5 - p1.getY()) < collision_eps && Math.abs(c + 0.5 - p1.getX()) < collision_eps) {
                            System.out.println("check_valid() => plane hit wall (p1)");
                            status = "p2";
                            return false;
                        }
                        if (Math.abs(r + 0.5 - p2.getY()) < collision_eps && Math.abs(c + 0.5 - p2.getX()) < collision_eps) {
                            System.out.println("check_valid() => plane hit wall (p2)");
                            status = "p1";
                            return false;
                        }
                    }
                }
            }

            // 检查子弹撞飞机
            for (Bullet bullet : bullets) {
                if (bullet.getStatus().equals("dead")) continue;

                // 检查子弹撞墙
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        if (g[r][c] == 1 && (int) bullet.getY() == r && (int) bullet.getX() == c) {
                            System.out.println("check_valid() => 子弹撞墙上了");
                            bullet.setStatus("dead");
                            break;
                        }
                    }
                    if (bullet.getStatus().equals("dead")) break;
                }
                if (bullet.getStatus().equals("dead")) break;

                // 检查子弹撞飞机
                if (bullet.getId() != p1.getId() && Math.abs(bullet.getX() - p1.getX()) < bullet_collision_eps && Math.abs(bullet.getY() - p1.getY()) < bullet_collision_eps) {
                    bullet.setStatus("dead");
                    System.out.println("check_valid() => bullet hit plane (p2)");
                    status = "p2";
                    return false;
                }
                if (bullet.getId() != p2.getId() && Math.abs(bullet.getX() - p2.getX()) < bullet_collision_eps && Math.abs(bullet.getY() - p2.getY()) < bullet_collision_eps) {
                    bullet.setStatus("dead");
                    System.out.println("check_valid() => bullet hit plane (p2)");
                    status = "p1";
                    return false;
                }
            }
        } finally {
            lock.unlock();
        }
        return true;
    }

    private void updateBullets() {
        lock.lock();
        try {
            for (Bullet bullet : bullets) {
                if (bullet.getStatus().equals("dead")) continue;

                double moveDistance = bullet.getSpeed() * timeDelta / 1000.0;
                bullet.setX(bullet.getX() + dx[bullet.getDirection()] * moveDistance);
                bullet.setY(bullet.getY() + dy[bullet.getDirection()] * moveDistance);
//                System.out.println(bullet.getX());
//                System.out.println(bullet.getY());
            }
        } finally {
            lock.unlock();
        }
    }

    private void checkEat() {
        lock.lock();
        try {
            if((int) p1.getX() == candy_x && (int) p1.getY() == candy_y) {
                p1.setScore(p1.getScore() + 1);
                eaten = 1;
                System.out.println("eaten");
            } else if ((int) p2.getX() == candy_x && (int) p2.getY() == candy_y) {
                p2.setScore(p2.getScore() + 1);
                eaten = 2;
            }
        } finally {
            lock.unlock();
        }
    }

    private void updateCandy() {
        lock.lock();
        try {
            if(eaten != -1 || (candy_x == -1 && candy_y == -1)) {
                while (true) {
                    int x = (int) (Math.random() * cols);
                    int y = (int) (Math.random() * rows);
                    // 检查新位置是否合法
                    if (x == 0 || x == cols - 1 || y == 0 || y == rows - 1) {
                        continue; // 边界
                    }
                    if ((y == rows - 2 && x == 1) || (y == 1 && x == cols - 2)) {
                        continue; // 玩家出生点
                    }
                    if (g[y][x] == 1) {
                        continue; // 墙壁
                    }
                    if (x == candy_x && y == candy_y) {
                        continue; // 不能和之前的位置一样
                    }
                    candy_x = x;
                    candy_y = y;
                    eaten = 5;
//                    System.out.println("updateupdateupdate");
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void sendCandy() {
        lock.lock();
        try {
            if (eaten != -1) {
                JSONObject resp = new JSONObject();
                JSONObject candy = new JSONObject();
                resp.put("event", "candy");
                candy.put("candy_x", candy_x);
                candy.put("candy_y", candy_y);
                System.out.println(candy_x);
                resp.put("candy", candy);
                sendMessage(resp.toJSONString());
                eaten = -1;
            }
        } finally {
            lock.unlock();
        }
    }

    public void whiteFlag(Integer id) {
        lock.lock();
        try {
            if (id.equals(p1.getId())) status = "p2";
            if (id.equals(p2.getId())) status = "p1";
        } finally {
            lock.unlock();
        }
    }

    public void sendBullet(Integer userId) {
        lock.lock();
        try {
            Bullet bullet = null;
            if(userId.equals(p1.getId())) {
                if(p1.getScore() <= 0) return;
                p1.setScore(p1.getScore() - 0.1);
                bullet = new Bullet(p1.getId(), p1.getX(), p1.getY(), 2 * p1.getSpeed(), p1.getDirection(), "alive");
            } else {
                if(p2.getScore() <= 0) return;
                p2.setScore(p2.getScore() - 0.1);
                bullet = new Bullet(p2.getId(), p2.getX(), p2.getY(), 2 * p2.getSpeed(), p2.getDirection(), "alive");
            }
//            System.out.println(bullet);
            bullets.add(bullet);
            JSONObject resp = new JSONObject();
            resp.put("event", "shoot");
            JSONObject blt = new JSONObject();
            blt.put("id", bullet.getId());
            blt.put("x", bullet.getX());
            blt.put("y", bullet.getY());
            blt.put("speed", bullet.getSpeed());
            blt.put("direction", bullet.getDirection());
            blt.put("status", bullet.getStatus());
            resp.put("bullet", blt);
            sendMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private void sendMessage(String message) {
        if(WebSocketServer.users.get(p1.getId()) != null) {
            WebSocketServer.users.get(p1.getId()).sendMessage(message);
        }
        if(WebSocketServer.users.get(p2.getId()) != null) {
            WebSocketServer.users.get(p2.getId()).sendMessage(message);
        }
    }

    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            JSONObject gamedata = new JSONObject();
            resp.put("event", "move");
            gamedata.put("p1_id", p1.getId());
            gamedata.put("p1_score", p1.getScore());
            gamedata.put("p1_direction", p1.getDirection());
            gamedata.put("p1_x", p1.getX());
            gamedata.put("p1_y", p1.getY());
//            System.out.println("p1x    " + p1.getX());
//            System.out.println("p1y    " + p1.getY());
            gamedata.put("p2_id", p2.getId());
            gamedata.put("p2_direction", p2.getDirection());
            gamedata.put("p2_score", p2.getScore());
            gamedata.put("p2_x", p2.getX());
            gamedata.put("p2_y", p2.getY());
            resp.put("gamedata", gamedata);
//            System.out.println(resp.toJSONString());
            sendMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private void sendResult() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
//            JSONObject result = new JSONObject();
            resp.put("event", "game_over");
//            result.put("result", status);
            resp.put("result", status);
//            System.out.println(resp.toJSONString());
            sendMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private void sendTime() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            JSONObject time = new JSONObject();
            resp.put("event", "time_update");
            time.put("total", totalTime);
            time.put("used", timeUsed);
            resp.put("time", time);
            sendMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < totalTime; i++) {
            checkEat();
            updateCandy();
            sendCandy();
            updateBullets();
            updateMove();
            sendMove();
            timeUsed = i + 1;
            sendTime();
            if (!checkValid(false)) {
                System.out.println("游戏结束--------");
                sendResult();
                break;
            }
            try {
                Thread.sleep(timeDelta);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if(!checkValid(true)) {
            // 游戏结束
            System.out.println("计时结束");
            sendResult();
        }
        super.run();
    }
}
