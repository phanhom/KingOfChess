export class Bullet {
    constructor(x, y, d, plane_speed, id) {
        this.x = x;
        this.y = y;
        this.c = Math.floor(x);
        this.r = Math.floor(y);
        this.speed = 2 * plane_speed;
        this.id = id;

        this.direction = d;
        this.status = "alive";  // alive, dead
    }
}
