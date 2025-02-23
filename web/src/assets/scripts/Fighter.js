import { GameObject } from "./GameObject.js";

export class Fighter extends GameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;
        this.r = info.r;    // current pos r
        this.c = info.c;    // current pos c
        this.next_pos = null;

        this.speed = 5;
        this.direction = -1;    // 0,1,2,3 上右下左
        this.status = "idle";   //idle 表示静止, moving 表示移动, dead 表示死亡 // ?
        //只有moving了 才有next_pos
        //另外directon -1会不会超出边界？

        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];

        this.step = 0;  // 回合数
        this.eps = 1e-2;
        this.facing = 0;
        this.bullets = [];
        if (this.id == 1) this.facing = 2;
    }

    start() {

    }

    set_direction(d) {
        // console.log("set_direction", d);
        this.direction = d;
    }

    // 将飞机状态变成下一步
    next_step() {
        const d = this.direction;
        this.facing = d;
        this.direction = -1;
        this.next_pos = [this.r + this.dr[d], this.c + this.dc[d]];
        this.status = "moving"; // ?
        this.step++;

        // this.r = this.next_pos[0];
        // this.c = this.next_pos[1];

        if (!this.gamemap.check_valid(this.r, this)) {
            this.status = "dead";
        }
    }

    update_move() {
        console.log("update_move");
        const dx = this.next_pos[0] - this.r;
        const dy = this.next_pos[1] - this.c;
        const distance = Math.sqrt(dx * dx + dy * dy);
        console.log(this.next_pos[0] + "distance")

        if (distance < this.eps) {
            this.r = this.next_pos[0];
            this.c = this.next_pos[1];
            this.next_pos = null;
            this.status = "idle";
        } else {
            const move_distance = this.speed * this.timedelta / 1000;
            this.next_pos[0] += dx * move_distance / distance;
            this.next_pos[1] += dy * move_distance / distance;
        }
    }

    update() {
        if(this.status == "moving") { // ?
            this.update_move();
        }

        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        ctx.fillRect(this.r * L, this.c * L, L, L);
    }
}