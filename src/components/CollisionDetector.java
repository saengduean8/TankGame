package components;

import commons.Globals;
import commons.MapReader;
import commons.TankOrientation;

import java.io.IOException;

public class CollisionDetector{

    private String[][] map;

    private boolean gameOver;

    /**
     * When gameOver is false, the value of tankWon is null
     * When gameOver is true, the value of tankWon is the TankObject that Won the game
     */
    private TankObject tankWon;

    public CollisionDetector(String[][] map) throws IOException{
        this.map = map;
        this.gameOver = false;
        this.tankWon = null;
    }

    /**
     * Returns true if there is a collision
     * @param newX new x tank coordinate
     * @param newY new y tank coordinate
     * @return
     */
    public boolean validateCollision(int newX, int newY, TankObject otherTank) {
        return validateMapCollision(newX, newY) || validateTankCollision(newX, newY, otherTank);
    }

    public boolean validateTankCollision(int newX, int newY, TankObject otherTank) {
        if(newX == otherTank.x && newY == otherTank.y){
            return true;
        }
        return false;
    }

    public boolean validateMapCollision(int newX, int newY){
        int mapX = newX / Globals.BLOCK_SIZE;
        int mapY = newY / Globals.BLOCK_SIZE;

        String value = map[mapY][mapX];

        if(value.equals(MapReader.WALL) || value.equals(MapReader.BREAKABLE_WALL)) {
            return true;
        }
        return false;
    }

    public boolean validateBullettoWallCollision(Bullet bullet){
        Point p = bullet.getNextPosition();
        int newX = p.x;
        int newY = p.y;


       int bulletX = newX / (Globals.BLOCK_SIZE );
       int bulletY = newY / (Globals.BLOCK_SIZE );

        switch(bullet.getOrientation()) {
            case LEFT:
                // Do nothing
                break;
            case RIGHT:
                // The calculation: bulletX = (bulletX + Globals.BLOCK_SIZE) / Globals.BLOCK_SIZE;
                bulletX += 1;
                break;
            case TOP:
                // Do nothing
                break;
            case DOWN:
                // The calculation: bulletY = (bulletY + Globals.BLOCK_SIZE) / Globals.BLOCK_SIZE;
                bulletY += 1;
                break;
        }

        String value = map[bulletY][bulletX];

        if(value.equals(MapReader.WALL)) {
            return true;
        }
        else if(value.equals(MapReader.BREAKABLE_WALL)){
            map[bulletY][bulletX] = MapReader.SPACE;
            return true;
        }
        return false;
    }

    public boolean validateBullettoTankCollision(Bullet bullet, TankObject tank1, TankObject tank2) {
        if (validateBullettoTankCollision(bullet, tank1)){
            tank1.health--;
            if(tank1.health == 0){
                tank1.health = 16;
                tank1.x = 4 * Globals.BLOCK_SIZE;
                tank1.y = 4 * Globals.BLOCK_SIZE;
                tank1.orientation = TankOrientation.LEFT;
                if(tank1.lives == 0){
                    System.out.println("Player 2 Won");
                    this.gameOver = true;
                    this.tankWon = tank2;
                } else {
                    tank1.lives--;
                }
            }
            return true;
        }
        else if ( validateBullettoTankCollision(bullet, tank2)){
            tank2.health--;
            if(tank2.health == 0){
                tank2.health = 16;
                tank2.x = 27 * Globals.BLOCK_SIZE;
                tank2.y = 27 * Globals.BLOCK_SIZE;
                tank2.orientation = TankOrientation.LEFT;
                if(tank2.lives == 0){
                    System.out.println("Player 1 Won");
                    this.gameOver = true;
                    this.tankWon = tank1;
                } else {
                    tank2.lives--;
                }
            }
            return true;
        }
        return false;
    }

    private boolean validateBullettoTankCollision(Bullet bullet, TankObject tank) {
        Point p = bullet.getNextPosition();
        int newMinX = p.x;
        int newMinY = p.y;
        int newMaxX = p.x + Globals.BLOCK_SIZE;
        int newMaxY = p.y + Globals.BLOCK_SIZE;

        int minTankX = tank.x;
        int maxTankX = tank.x + Globals.BLOCK_SIZE;
        int minTankY = tank.y;
        int maxTankY = tank.y + Globals.BLOCK_SIZE;

        switch (bullet.getOrientation()) {
                case LEFT: return (minTankX < newMinX && newMinX < maxTankX && newMinY == minTankY);
                case RIGHT: return (minTankX < newMaxX && newMaxX < maxTankX && newMinY == minTankY);
                case TOP: return (minTankY < newMinY && newMinY < maxTankY && newMinX == minTankX);
                case DOWN: return (minTankY < newMaxY && newMaxY < maxTankY && newMinX == minTankX);
        }
        return false;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public TankObject getTankWon() {
        return this.tankWon;
    }

}