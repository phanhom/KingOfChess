import { GameObject } from "./GameObject.js";
import { Wall } from "./Wall.js";
import { plane } from "./plane.js";
import { candy } from "./candy.js";

// 要吃奖励得分 ????
export class GameMap extends GameObject {
    constructor(ctx, parent, store) {
        super();

        this.ctx = ctx;
        this.parent = parent
        this.store = store;
        this.L = 0;
        this.rows = 16;
        this.cols = 16;

        this.inner_walls_count = 20;
        this.walls = [];
        this.collision_eps = 0.6;
        this.bullet_collision_eps = 0.8;

        this.candy = new candy(0, 0, this);

        // 吃了就更新,不能在墙里面?
        // 吃了加1分，倒计时1min，谁分高谁赢，然后打子弹消耗0.1分?
 
        //记得刷新分数和重置?
        //飞机撞了就重生并且减去1分?

        this.planes = [
            new plane({
                id: 0,
                color: "#4876FF",
                r: this.rows - 2,
                c: 1
            }, this, store),

            new plane({
                id: 1,
                color: "#FF4864",
                r: 1,
                c: this.cols - 2
            }, this, store)
        ];
    }

    init_walls() {
        const g = this.store.state.pk.game_map;

        // render
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c] == true) {
                    this.walls.push(new Wall(r, c, this))
                }
            }
        }
        return true;
    }

    next_step() {
        for (const plane of this.planes) {
            plane.next_step();
        }
    }

    check_ready() {
        for (const plane of this.planes) {
            if (plane.status != "moving") return false;
            if (plane.direction == -1) return false;
        }
        return true;
    }

    check_valid(pos_x, pos_y, plane_id) {
        // console.log("check_valid => start");
        // 结束界面 + 倒计时 + 重新开始
        // 检查飞机相撞
        // if (Math.abs(this.planes[0].x - this.planes[1].x) < this.collision_eps && Math.abs(this.planes[0].y - this.planes[1].y) < this.collision_eps) {
        //     console.log(this.planes[0].x)
        //     console.log(this.planes[0].y)
        //     console.log(this.planes[1].x)
        //     console.log(this.planes[1].y)
        //     console.log("check_valid() => plane hit plane");
        //     return false;
        // }

        // 检查飞机撞墙
        // for (const wall of this.walls) {
        //     if ((Math.abs(wall.r + 0.5 - pos_y) < this.collision_eps) && (Math.abs(wall.c + 0.5 - pos_x) < this.collision_eps)) {
        //         console.log("check_valid() => plane hit wall");
        //         return false;
        //     }
        // }

        // 检查子弹撞飞机
        for (let plane of this.planes) {
            for (let bullet of plane.bullets) {
                if (bullet.status == "dead") continue;
                for (const wall of this.walls) {
                    if (wall.r == bullet.r && wall.c == bullet.c) {
                        bullet.status = "dead";
                        // console.log("check_vaild() => bullet hit wall");
                    }
                }
                // 另一架飞机的子弹撞到我了, 所以如果是自己就跳过
                if (plane.id == plane_id) continue;
                if ((Math.abs(bullet.x - pos_x) < this.bullet_collision_eps) && (Math.abs(bullet.y - pos_y) < this.bullet_collision_eps)) {
                    bullet.status = "dead";
                    console.log("check_valid() => bullet hit plane");
                    // return false;
                    // 把这个移动到plane类里面来只更新子弹的位置
                }
            }
        }
        // console.log(this.store.state.pk.status);
        if(this.store.state.pk.result === "deuce") return false;
        if(this.store.state.pk.result === "p1" && this.id === 1) return false;
        if(this.store.state.pk.result === "p2" && this.id === 0) return false;
        // console.log("check_valid => end");
        return true;
    }

    add_listening_events() {
        this.ctx.canvas.focus();

        // const [plane0, plane1] = this.planes;
        // this.ctx.canvas.addEventListener("keydown", e => {
        //     e.preventDefault();
        //     if (e.key === "w") plane0.set_direction(0);
        //     else if (e.key == "d") plane0.set_direction(1);
        //     else if (e.key == "s") plane0.set_direction(2);
        //     else if (e.key == "a") plane0.set_direction(3);
        //     else if (e.key == "j") plane0.shoot();
        //     else if (e.key == "ArrowUp") plane1.set_direction(0);
        //     else if (e.key == "ArrowRight") plane1.set_direction(1);
        //     else if (e.key == "ArrowDown") plane1.set_direction(2);
        //     else if (e.key == "ArrowLeft") plane1.set_direction(3);
        //     else if (e.key == "1") plane1.shoot();
        //     // console.log("add_listening_events => " + e.key);
        // });
        this.ctx.canvas.addEventListener("keydown", e => {
            let d = -1;
            if (e.key === "w") d = 0;
            else if (e.key == "d") d = 1;
            else if (e.key == "s") d = 2;
            else if (e.key == "a") d = 3;
            else if (e.key == "j") {
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "shoot",
                }));
                this.planes[0].shoot();
                this.planes[1].shoot();
                console.log("shoot");
            }
            if (d >= 0) {
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: d,
                }));
            }
            console.log(d);
        });
    }

    start() {
        this.init_walls();
        this.add_listening_events();
    }

    // update size after reframe the web
    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
        // if (this.check_ready()) {
        //     this.next_step();
        // }
        this.next_step();
        this.render();
    }

    render() {
        const color_even = "#AAD751", color_odd = "#BBE862";
        for (let i = 0; i < this.rows; i++) {
            for (let j = 0; j < this.cols; j++) {
                if ((i + j) & 1) {
                    this.ctx.fillStyle = color_odd;
                } else {
                    this.ctx.fillStyle = color_even;
                }
                this.ctx.fillRect(j * this.L, i * this.L, this.L, this.L);
            }
        }
    }
}