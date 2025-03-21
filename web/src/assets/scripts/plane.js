import { GameObject } from "./GameObject.js";
import { Bullet } from "./Bullet.js";

export class plane extends GameObject {
    constructor(info, gamemap, store) {
        super();

        this.id = info.id;
        this.store = store;
        this.color = info.color;
        this.score = 20;
        this.gamemap = gamemap;
        this.r = info.r;    // current pos r
        this.c = info.c;    // current pos c
        this.x = this.c + 0.5;
        this.y = this.r + 0.5;
        this.speed = 4;
        this.direction = 0;    // 0,1,2,3 上右下左
        if (this.id == 1) this.direction = 2;
        this.status = "idle";   //idle 表示静止, moving 表示移动, dead 表示死亡
        this.img = new Image();
        this.img.src = "/images/BlueJet.png";
        if (this.id == 1) this.img.src = "/images/RedJet.png";

        this.dr = [-1, 0, 1, 0, 0];
        this.dc = [0, 1, 0, -1, 0];
        this.angle = [-Math.PI / 2, 0, Math.PI / 2, Math.PI]
        this.angle2 = [Math.PI, Math.PI / 2, 0, -Math.PI / 2]

        this.step = 0;  // 回合数
        this.bullets = [];
    }

    start() {

    }

    set_direction(d) {
        this.direction = d;
        this.status = "moving";
    }

    shoot() {
        if(this.store.state.pk.new_bullet_arrive == true) {
            if(this.score <= 0) return;
            let bullet = this.store.state.pk.new_bullet;
            if(bullet.pid == this.id) {
                this.score -= 1;
                // console.log(bullet)
                this.bullets.push(new Bullet(bullet.x, bullet.y, bullet.direction, bullet.speed, bullet.id));
                this.store.state.pk.new_bullet_arrive = false;
            }
        }
    }

    // 将飞机状态变成下一步
    next_step() {
        const d = this.direction;
        this.status = "moving";
        this.step++;

        if (!this.gamemap.check_valid(this.x, this.y, this.id)) {
            this.status = "dead";
        }
    }

    update_bullets_move() {
        for(let bullet of this.bullets) {
            if(bullet.status == "dead") continue;
            const move_distance = bullet.speed * this.timedelta / 1000;
            bullet.x += this.dc[bullet.direction] * move_distance;
            bullet.y += this.dr[bullet.direction] * move_distance;
            bullet.r = Math.floor(bullet.x);
            bullet.c = Math.floor(bullet.y);
            // console.log(bullet.x, bullet.y);
        }

        if (!this.gamemap.check_valid(this.x, this.y, this.id)) {
            this.status = "dead";
        }
    }

    update_move() {
        if(this.id == 0) {
            this.x = this.store.state.pk.p1_x;
            this.y = this.store.state.pk.p1_y;
        } else {
            this.x = this.store.state.pk.p2_x;
            this.y = this.store.state.pk.p2_y;
        }
        this.r = Math.floor(this.y);
        this.c = Math.floor(this.x);
        if(this.id == 0) {
            // console.log(this.x, this.y);
        }

        if (!this.gamemap.check_valid(this.x, this.y, this.id)) {
            this.status = "dead";
        }
    }

    update() {
        this.shoot();//?
        this.update_bullets_move();
        // if (this.status == "moving") {
        //     this.update_move();
        // }
        this.update_move();

        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;
        // 看不见对面的子弹，而且老是先消耗蓝的分数
        // 渲染飞机
        if(this.store.state.pk.p1_id == this.store.state.user.id) {
            ctx.save();
            ctx.translate(this.x * L, this.y * L);
            ctx.rotate(this.angle[this.direction]);
            ctx.fillStyle = this.color;
            ctx.drawImage(this.img, -0.5 * L, -0.5 * L, L, L);
            ctx.restore();
            ctx.fillStyle = "black";
            for(let bullet of this.bullets) {
                if(bullet.status == "dead") continue;
                ctx.fillRect(bullet.x * L - 2, bullet.y * L - 2, 4, 4)
            }
        } else {
            ctx.save();
            ctx.translate(this.y * L, this.x * L);
            ctx.rotate(this.angle2[this.direction]);
            ctx.fillStyle = this.color;
            ctx.drawImage(this.img, -0.5 * L, -0.5 * L, L, L);
            ctx.restore();
            ctx.fillStyle = "black";
            for(let bullet of this.bullets) {
                if(bullet.status == "dead") continue;
                ctx.fillRect(bullet.y * L - 2, bullet.x * L - 2, 4, 4)
            }
        }
        // 渲染子弹
    }
}