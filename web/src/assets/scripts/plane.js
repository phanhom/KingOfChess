import { GameObject } from "./GameObject.js";
import { Bullet } from "./Bullet.js";

export class plane extends GameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;
        this.r = info.r;    // current pos r
        this.c = info.c;    // current pos c
        this.x = this.c + 0.5;
        this.y = this.r + 0.5;

        this.speed = 5;
        this.direction = 0;    // 0,1,2,3 上右下左
        if (this.id == 1) this.direction = 2;
        this.status = "idle";   //idle 表示静止, moving 表示移动, dead 表示死亡
        // this.next_pos = null;

        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];

        this.step = 0;  // 回合数
        this.bullets = [];
    }

    start() {

    }

    set_direction(d) {
        this.direction = d;
        this.status = "moving";
        // this.next_pos = [this.r + this.dr[d], this.c + this.dc[d]];
    }

    shoot() {
        this.bullets.push(new Bullet(this.x, this.y, this.direction, this.speed, this.id));
    }

    // 将飞机状态变成下一步
    next_step() {
        const d = this.direction;
        this.facing = d;
        // this.next_pos = [this.r + this.dr[d], this.c + this.dc[d]];
        this.status = "moving"; // ?
        this.step++;

        if (!this.gamemap.check_valid(this.r, this.c, this.id)) {
            this.status = "dead";
        }
    }

    update_bullets_move() { // 还没检查dead, 子弹撞人 子弹撞墙
        for(let bullet of this.bullets) {
            if(bullet.status == "dead") continue;
            const move_distance = bullet.speed * this.timedelta / 1000;
            bullet.x += this.dc[bullet.direction] * move_distance;
            bullet.y += this.dr[bullet.direction] * move_distance;
            bullet.r = Math.floor(bullet.x);
            bullet.c = Math.floor(bullet.y);
        }

        if (!this.gamemap.check_valid(this.x, this.y, this.id)) {
            this.status = "dead";
            console.log("dead" + this.x);
        }
    }

    update_move() {
        const move_distance = this.speed * this.timedelta / 1000;
        this.x += this.dc[this.direction] * move_distance;
        this.y += this.dr[this.direction] * move_distance;
        this.r = Math.floor(this.y);
        this.c = Math.floor(this.x);

        if (!this.gamemap.check_valid(this.x, this.y, this.id)) {
            this.status = "dead";
            console.log("dead" + this.x);
        }
    }

    update() {
        this.update_bullets_move();
        if (this.status == "moving") { // ?
            this.update_move();
        }

        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        // 渲染飞机
        ctx.fillStyle = this.color;
        ctx.fillRect((this.x - 0.5) * L, (this.y - 0.5) * L, L, L);
        // ctx.drawImage(this.gamemap.plane_img, (this.x - 0.5) * L, (this.y - 0.5) * L, L, L);

        // 渲染子弹
        ctx.fillStyle = "black";
        for(let bullet of this.bullets) {
            if(bullet.status == "dead") continue;
            ctx.fillRect(bullet.x * L - 2, bullet.y * L - 2, 4, 4)
        }
    }
}