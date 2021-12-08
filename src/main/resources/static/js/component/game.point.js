class Point {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.plus = (p) => {
            return new Point(
                this.x + p.x,
                this.y + p.y
            );
        };
        this.minus = (p) => {
            return new Point(
                this.x - p.x,
                this.y - p.y
            );
        };
        this.normSq = () => { return x * x + y * y; };
    }
}