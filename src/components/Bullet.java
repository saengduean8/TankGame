package components;

import commons.Globals;
import commons.TankOrientation;

public class Bullet {

    private float x;

    private float y;

    private final TankOrientation orientation;

    public Bullet(int x, int y, TankOrientation orientation) {
        this.x = (float) x;
        this.y = (float) y;
        this.orientation = orientation;
    }

    public void moveBullet() {
        Point p = getNextPosition();
        this.x = (float)p.x;
        this.y = (float)p.y;
    }

    public Point getNextPosition() {
        float newX = this.x;
        float newY = this.y;
        if (orientation == TankOrientation.LEFT) {
            newX -= Globals.BULLET_SPEED;
        }
        if (orientation == TankOrientation.RIGHT) {
            newX += Globals.BULLET_SPEED;
        }
        if (orientation == TankOrientation.TOP) {
            newY -= Globals.BULLET_SPEED;
        }
        if (orientation == TankOrientation.DOWN) {
            newY += Globals.BULLET_SPEED;
        }
        return new Point(Math.round(newX), Math.round(newY));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public TankOrientation getOrientation() {
        return orientation;
    }
}
