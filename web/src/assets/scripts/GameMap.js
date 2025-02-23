import { GameObject } from "./GameObject.js";
import { Wall } from "./Wall.js";
import { Fighter } from "./fighter.js";

export class GameMap extends GameObject {
    constructor(ctx, parent) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;
        this.rows = 15;
        this.cols = 15;

        this.inner_walls_count = 50;
        this.walls = [];

        this.fighters = [
            new Fighter({
                id: 0,
                color: "#4876FF",
                r: this.rows - 2,
                c: 1
            }, this),

            new Fighter({
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
        for (const fighter of this.fighters) {
            fighter.next_step();
        }
    }

    check_valid(rr, cc) { // 判断两个飞机的nextstep是否相同 （子弹）
        // console.log("-------------")
        // console.log(rr, cc);
        if(this.fighters[0].next_pos == this.fighters[1].next_pos) return false;
        for(const wall of this.walls) {
            if(wall.r == rr && wall.c == cc) return false;
        }
        return true;
    }

    add_listening_events() {
        this.ctx.canvas.focus();

        const [fighter0, fighter1] = this.fighters;
        // console.log(fighter0, fighter1);
        this.ctx.canvas.addEventListener("keydown", e => {
            e.preventDefault();
            if (e.key === "w") fighter0.set_direction(0);
            else if (e.key == "d") fighter0.set_direction(1);
            else if (e.key == "s") fighter0.set_direction(2);
            else if (e.key == "a") fighter0.set_direction(3);
            else if (e.key == "ArrowUp") fighter1.set_direction(0);
            else if (e.key == "ArrowRight") fighter1.set_direction(1);
            else if (e.key == "ArrowDown") fighter1.set_direction(2);
            else if (e.key == "ArrowLeft") fighter1.set_direction(3);
            // console.log("xxxxxxxxxxxxxx");
            // console.log(e.key);

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
        this.next_step();
        this.render();
    }

    render() {
        // this.ctx.fillStyle = "green";
        // this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
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