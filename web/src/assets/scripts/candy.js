import { GameObject } from "./GameObject.js";

export class candy extends GameObject {
    constructor(r, c, gamemap, store) {
        super();
        this.store = store;
        this.r = r;
        this.c = c;
        this.gamemap = gamemap;
        // this.just_eaten = false;

        this.candy_image = new Image();
        this.candy_image.src = "/images/coin.png";
    }

    refresh_candy_pos() {
        // if (!this.just_eaten) return;
        // console.log(this.store.state.pk.new_candy_arrive == true);
        if(this.store.state.pk.new_candy_arrive == true) {
            this.store.state.pk.new_candy_arrive = false;
            this.c = this.store.state.pk.candy_x;
            this.r = this.store.state.pk.candy_y;
            // console.log(this.c, this.r);
        }
        return;
        while (true) {
            let r = parseInt(Math.random() * this.gamemap.rows);
            let c = parseInt(Math.random() * this.gamemap.cols);
            if (r == 0 || r == this.gamemap.rows - 1 || c == 0 || c == this.gamemap.cols - 1) {
                continue;
            }
            if ((r == 1 && c == this.gamemap.cols - 2) || (r == this.gamemap.rows - 2 && c == 1)) {
                continue;
            }
            if (r == this.r && c == this.c) {
                continue;
            }
            let place = true;
            for (const wall of this.gamemap.walls) {
                if (wall.r == r && wall.c == c) {
                    place = false;
                    break;
                }
            }
            if (place == true) {
                this.c = c;
                this.r = r;
                break;
            }
        }
    }

    // check_eat(pos_x, pos_y, plane_id) {
    //     if (parseInt(pos_x) == this.c && parseInt(pos_y) == this.r) {
    //         console.log("candy eaten");
    //         this.gamemap.planes[plane_id].score++;
    //         console.log("plane" + plane_id + "eat candy")
    //         return true;
    //     }
    //     return false;
    // }

    start() {
        this.refresh_candy_pos();
    }

    update() {
        // console.log("candy update");
        // if (this.check_eat(this.gamemap.planes[0].x, this.gamemap.planes[0].y, 0)) {
        //     this.refresh_candy_pos();
        // } else if (this.check_eat(this.gamemap.planes[1].x, this.gamemap.planes[1].y, 1)) {
        //     this.refresh_candy_pos();
        // }
        this.refresh_candy_pos();
        this.render();
    }

    render() {
        const ctx = this.gamemap.ctx;
        const L = this.gamemap.L;
        if(this.store.state.pk.p1_id == this.store.state.user.id) {
            ctx.drawImage(this.candy_image, this.c * L, this.r * L, L, L);
        } else {
            ctx.drawImage(this.candy_image, this.r * L, this.c * L, L, L);
        }
    }

}
