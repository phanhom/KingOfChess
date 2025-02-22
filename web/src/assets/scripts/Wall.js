import { GameObject } from "./GameObject.js";

export class Wall extends GameObject {
    constructor(r, c, gamemap) {
        super();

        this.r = r;
        this.c = c;
        this.color = "#cc7734"
        this.gamemap = gamemap;
    }

    update() {
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;
        ctx.fillStyle = this.color;
        ctx.fillRect(this.c * L, this.r * L, L, L);
    }
}