import { GameObject } from "./GameObject.js";
import { Wall } from "./Wall.js";
import { plane } from "./plane.js";
// 要吃奖励得分 ????
export class GameMap extends GameObject {
    constructor(ctx, parent) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;
        this.rows = 16;
        this.cols = 16;

        this.inner_walls_count = 10;
        this.walls = [];
        this.collision_eps = 0.6;
        this.bullet_collision_eps = 0.8;

        this.planes = [
            new plane({
                id: 0,
                color: "#4876FF",
                r: this.rows - 2,
                c: 1
            }, this),

            new plane({
                id: 1,
                color: "#FF4864",
                r: 1,
                c: this.cols - 2
            }, this)
        ];
    }

    check_connectivity(g, sx, sy, tx, ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for (let i = 0; i < 4; i++) {
            let x = dx[i] + sx, y = dy[i] + sy;
            if (!g[x][y] && this.check_connectivity(g, x, y, tx, ty)) {
                return true;
            }
        }
        return false;
    }

    init_walls() {
        const g = [];
        // init
        for (let r = 0; r < this.rows; r++) {
            g[r] = [];
            for (let c = 0; c < this.cols; c++) {
                g[r][c] = false;
            }
        }

        // borders
        for (let r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = true;
        }
        for (let c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = true;
        }

        // random create walls(cnt)
        for (let i = 0; i < this.inner_walls_count / 2; i++) {
            for (let j = 0; j < 1000; j++) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if (g[r][c] || g[c][r]) {
                    continue;
                }
                if ((r == this.rows - 2 && c == 1) || (r == 1 || c == this.cols - 2)) {
                    continue;
                }
                g[r][c] = g[c][r] = true;
                break;
            }
        }

        // check connectivity
        const copy_g = JSON.parse(JSON.stringify(g));
        if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) {
            return false;
        }

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
        if(Math.abs(this.planes[0].x - this.planes[1].x) < this.collision_eps && Math.abs(this.planes[0].y - this.planes[1].y) < this.collision_eps)
            return false;

        // 检查飞机撞墙
        for(const wall of this.walls) {
            if((Math.abs(wall.r + 0.5 - pos_y) < this.collision_eps) && (Math.abs(wall.c + 0.5 - pos_x) < this.collision_eps)) {
                return false;
            }
        }

        // 检查子弹撞飞机
        for(let plane of this.planes) {
            for(let bullet of plane.bullets) {
                if(bullet.status == "dead") continue;
                for(const wall of this.walls) {
                    if(wall.r == bullet.r && wall.c == bullet.c) {
                        bullet.status = "dead";
                        // console.log("check_vaild() => bullet hit wall");
                    }
                }
                // 另一架飞机的子弹撞到我了, 所以如果是自己就跳过
                if(plane.id == plane_id) continue;
                if((Math.abs(bullet.x - pos_x) < this.bullet_collision_eps) && (Math.abs(bullet.y - pos_y) < this.bullet_collision_eps)) {
                    bullet.status = "dead";
                    console.log("check_valid() => bullet hit plane");
                    return false;
                }
            }
        }
        // console.log("check_valid => end");
        return true;
    }

    add_listening_events() {
        this.ctx.canvas.focus();

        const [plane0, plane1] = this.planes;
        this.ctx.canvas.addEventListener("keydown", e => {
            e.preventDefault();
            if (e.key === "w") plane0.set_direction(0);
            else if (e.key == "d") plane0.set_direction(1);
            else if (e.key == "s") plane0.set_direction(2);
            else if (e.key == "a") plane0.set_direction(3);
            else if (e.key == "j") plane0.shoot();
            else if (e.key == "ArrowUp") plane1.set_direction(0);
            else if (e.key == "ArrowRight") plane1.set_direction(1);
            else if (e.key == "ArrowDown") plane1.set_direction(2);
            else if (e.key == "ArrowLeft") plane1.set_direction(3);
            else if (e.key == "1") plane1.shoot();
            // console.log("add_listening_events => " + e.key);
        });
    }

    start() {
        // this.init_walls();
        for (let i = 0; i < 500000; i++)
            if (this.init_walls()) {
                break;
            }
        
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
        if(this.check_ready()) {
            this.next_step();
        }
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