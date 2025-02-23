import { GameObject } from "./GameObject.js";

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
        this.direction = -1;    // 0,1,2,3 上右下左
        this.status = "idle";   //idle 表示静止, moving 表示移动, dead 表示死亡 // ?
        this.next_pos = null;
        //只有moving了 才有next_pos
        //另外directon -1会不会超出边界？

        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];

        this.step = 0;  // 回合数
        // this.eps = 0.5;
        this.bullets = [];
    }

    start() {

    }

    set_direction(d) {
        // console.log("set_direction", d);
        this.direction = d;
        this.status = "moving";
        this.next_pos = [this.r + this.dr[d], this.c + this.dc[d]];
    }

    // 将飞机状态变成下一步
    next_step() {
        const d = this.direction;
        this.facing = d;
        // this.direction = -1;
        this.next_pos = [this.r + this.dr[d], this.c + this.dc[d]];
        this.status = "moving"; // ?
        this.step++;

        // this.r = this.next_pos[0];
        // this.c = this.next_pos[1];

        if (!this.gamemap.check_valid(this.r, this.c)) {
            this.status = "dead";
            console.log("dead-wertyuioiytrewertyuioiuytrerty");
        }
    }

    update_move() {
        /*
        // console.log("update_move");
        const dx = this.next_pos[0] + 0.5 - this.y;
        const dy = this.next_pos[1] + 0.5 - this.x;
        // const distance = Math.sqrt(dx * dx + dy * dy);
        const distance = Math.max(Math.abs(dx), Math.abs(dy))
        // console.log(this.next_pos[0] + "distance")

        if (distance < this.eps) {
            this.r = this.next_pos[0];
            this.c = this.next_pos[1];
            // this.next_pos = null;
            // this.status = "idle";
        }
        */

        this.r = Math.floor(this.y);
        this.c = Math.floor(this.x);
        const move_distance = this.speed * this.timedelta / 1000;
        this.x += this.dc[this.direction] * move_distance;
        this.y += this.dr[this.direction] * move_distance;
        // this.x += dx * move_distance / distance;
        // this.y += dy * move_distance / distance;
        console.log("currentpos: " + this.next_pos[0] + " " + this.next_pos[1] + " " + move_distance);
        console.log(this.x + " " + this.y);
        if (!this.gamemap.check_valid(this.r, this.c)) {
            this.status = "dead";
            console.log("dead-wertyuioiytrewertyuioiuytrerty");
        }
    }

    update() {
        if (this.status == "moving") { // ?
            // console.log("updateupdateupdateupdate");
            this.update_move();
        }

        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        ctx.fillRect((this.x - 0.5) * L, (this.y - 0.5) * L, L, L);
    }
}