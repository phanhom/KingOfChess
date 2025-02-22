import { GameObject } from "./GameObject.js";
import { Wall } from "./Wall.js";

export class GameMap extends GameObject {
    constructor(ctx, parent) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;
        this.rows = 13;
        this.cols = 13;

        this.inner_walls_count = 40;
        this.walls = [];
    }

    check_connectivity(g, sx, sy, tx, ty) {
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for(let i = 0; i < 4; i++) {
            let x = dx[i] + sx, y = dy[i] + sy;
            if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty)) {
                return true;
            }
        }
        return false;
    }

    init_walls() {
        const g = [];
        // init
        for(let r = 0; r < this.rows; r++) {
            g[r] = [];
            for(let c = 0; c < this.cols; c++) {
                g[r][c] = false;
            }
        }

        // borders
        for(let r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = true;
        }
        for(let c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = true;
        }

        // random create walls(cnt)
        for(let i = 0; i < this.inner_walls_count / 2; i++) {
            for(let j = 0; j < 1000; j++) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if(g[r][c] || g[c][r]) {
                    continue;
                }
                if((r == this.rows - 2 && c == 1) || (r == 1 || c == this.cols - 2)) {
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
        for(let r = 0; r < this.rows; r++) {
            for(let c = 0; c < this.cols; c++) {
                if(g[r][c] == true) {
                    this.walls.push(new Wall(r, c, this))
                }
            }
        }
        return true;
    }

    start() {
        // this.init_walls();
        for(let i = 0; i < 500000; i++)
            if(this.init_walls()) {
                console.log(i);
                break;
            }
    }

    // update size after reframe the web
    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
        this.render();
    }

    render() {
        // this.ctx.fillStyle = "green";
        // this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
        const color_even = "#AAD751", color_odd = "#BBE862";
        for(let i = 0; i < this.rows; i ++) {
            for(let j = 0; j < this.cols; j ++) {
                if((i + j) & 1) {
                    this.ctx.fillStyle = color_odd;
                } else {
                    this.ctx.fillStyle = color_even;
                }
                this.ctx.fillRect(j * this.L, i * this.L, this.L, this.L);
            }
        }
    }
}