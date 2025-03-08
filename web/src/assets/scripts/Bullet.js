export class Bullet {
    constructor(x, y, d, speed, id) {
        this.x = x;
        this.y = y;
        this.c = Math.floor(x);
        this.r = Math.floor(y);
        this.speed = speed;
        this.id = id;

        this.direction = d;
        this.status = "alive";  // alive, dead
    }
}
