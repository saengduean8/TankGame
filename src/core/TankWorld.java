package core;

import commons.AudioPlayer;
import commons.Globals;
import commons.MapReader;
import commons.TankOrientation;
import components.Bullet;
import components.Explosion;
import components.KeysControl;
import components.TankObject;
import components.CollisionDetector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


@SuppressWarnings("serial")
public class TankWorld extends JComponent implements Runnable {

    private String[][] map;

    private TankObject tank1;

    private TankObject tank2;

    private Thread thread;

    private KeysControl keysControl;

    private int count = 0, frame = 5;

    public static boolean tank2movingup,tank2movingdown,tank2movingleft,tank2movingright,tank1movingup,tank1movingdown
           ,tank1movingleft,tank1movingright;

    private AudioPlayer playMusic,explosionSound;

    private CollisionDetector collision;

    private ArrayList<Bullet> bullets;

    public ArrayList<Explosion> explosions;

    private BufferedImage bufferImage;

    public TankWorld() throws IOException {
        initializeTankWorld();
    }

    public void initializeTankWorld() throws IOException {
        this.map = MapReader.readMap(Globals.MAP1_FILENAME);
        this.bullets = new ArrayList<Bullet>(1000);
        this.explosions = new ArrayList<Explosion>(1000);
        this.bufferImage = new BufferedImage(Globals.BOARD_SIZE, Globals.BOARD_SIZE, BufferedImage.TYPE_INT_RGB);

        setFocusable(true);
        playMusic = new AudioPlayer(this, "resources/backgroundTune.wav");
        playMusic.play();
        playMusic.loop();

        setInitialTankLocation();

        explosionSound = new AudioPlayer(this,"resources/snd_explosion1.wav");

        collision = new CollisionDetector(map);
        this.keysControl = new KeysControl(collision,this.tank1,this.tank2,bullets, this);
        addKeyListener(keysControl);
    }

    public void restart() throws IOException {
        removeKeyListener(keysControl);
        initializeTankWorld();
    }

    private void setInitialTankLocation() {
        for (int row = 0; row < Globals.MAX_NUMBER_OF_BLOCKS; row++) {
            for (int col = 0; col < Globals.MAX_NUMBER_OF_BLOCKS; col++) {
                String value = map[row][col];
                int y = row * Globals.BLOCK_SIZE;
                int x = col * Globals.BLOCK_SIZE;
                if (value.equals(MapReader.TANK_1)) {
                    this.tank1 = new TankObject(x, y, 1, "Player 1",this, TankObject.TANK_1_NAME);
                    map[row][col] = MapReader.SPACE;
                    continue;
                }
                if (value.equals(MapReader.TANK_2)) {
                    this.tank2 = new TankObject(x, y, 2, "Player 2",this, TankObject.TANK_2_NAME);
                    map[row][col] = MapReader.SPACE;
                    continue;
                }
            }
        }
    }

    public void paint(Graphics g) {

        Graphics2D g2Component = (Graphics2D) g;

        Graphics graphicsBufferImage = bufferImage.getGraphics();
        Graphics2D g2 = (Graphics2D) graphicsBufferImage;

        renderBackground(g2);
        renderMap(g2);
        renderTankCurrentLocation(g2);
        renderBullets(g2);
        moveBullets(tank1.orientation, tank2.orientation);
        renderExplosion(g2);
        handleMovement(g2);

        if(collision.isGameOver()) {
            renderGameOver(g2);
        }

        // At this point all the rendered image is in 'bufferImage'
        // Draw mini map i.e the bufferImage scaled down in itself
        renderMiniMap(g2);

        // Then finally show the bufferImage in JComponent
        g2Component.drawImage(bufferImage, 0, 0, Globals.BOARD_SIZE, Globals.BOARD_SIZE, this);
        g2Component.finalize();
    }

    private void renderMiniMap(Graphics2D g2) {
        g2.drawImage(bufferImage, 0, 823, Globals.MINI_MAP_SIZE, Globals.MINI_MAP_SIZE, this);
        g2.finalize();
    }

    public void handleMovement(Graphics g){
        int newX, newY;
        int oldX, oldY ;
        
        count++;
        if(count == frame){
            count = 0;
        } else{
            return;
        }

        if (this.tank2movingup) {
            newX = tank2.x;
            newY = tank2.y - Globals.BLOCK_SIZE;
            oldY = tank2.y;
            if (collision.validateCollision(newX, newY, tank1)) {
                tank2.y = oldY;
            }else {
                tank2.y = newY;
            }
            tank2.orientation = TankOrientation.TOP;
        }

        if (this.tank2movingdown){
            newX = tank2.x;
            newY = tank2.y + Globals.BLOCK_SIZE ;
            oldY = tank2.y;
            if (collision.validateCollision(newX, newY, tank1)) {
               tank2.y = oldY;
            }else {
                tank2.y = newY;
            }
            tank2.orientation = TankOrientation.DOWN;
        }

        if (this.tank2movingleft){
            newX = tank2.x - Globals.BLOCK_SIZE;
            oldX = tank2.x;
            newY = tank2.y;
            if (collision.validateCollision(newX, newY, tank1)) {
                tank2.x = oldX;
            }else {
                tank2.x = newX;
            }
            tank2.orientation = TankOrientation.LEFT;
        }

        if (this.tank2movingright){
            newX = tank2.x + Globals.BLOCK_SIZE;
            oldX = tank2.x;
            newY = tank2.y;
            if (collision.validateCollision(newX, newY, tank1)) {
                tank2.x = oldX;
            }else {
                tank2.x = newX;
            }
            tank2.orientation = TankOrientation.RIGHT;
        }

        if (this.tank1movingup) {
            newX = tank1.x;
            newY = tank1.y - Globals.BLOCK_SIZE;
            oldY = tank1.y;
            if (collision.validateCollision(newX, newY, tank2)) {
                tank1.y = oldY;
            }else {
                tank1.y = newY;
            }
            tank1.orientation = TankOrientation.TOP;
        }

        if (this.tank1movingdown){
            newX = tank1.x;
            newY = tank1.y + Globals.BLOCK_SIZE;
            oldY = tank1.y;
            if (collision.validateCollision(newX, newY, tank2)) {
                tank1.y = oldY;
            }else {
                tank1.y = newY;
            }
            tank1.orientation = TankOrientation.DOWN;
        }

        if (this.tank1movingleft){
            newX = tank1.x - Globals.BLOCK_SIZE;
            oldX = tank1.x;
            newY = tank1.y;
            if (collision.validateCollision(newX, newY, tank2)) {
                tank1.x = oldX;
            }else {
                tank1.x = newX;
            }
            tank1.orientation = TankOrientation.LEFT;
        }

        if (this.tank1movingright){
            newX = tank1.x + Globals.BLOCK_SIZE;
            oldX = tank1.x;
            newY = tank1.y;
            if (collision.validateCollision(newX, newY, tank2)) {
                tank1.x = oldX;
            }else {
                tank1.x = newX;
            }
            tank1.orientation = TankOrientation.RIGHT;
        }
    }

    private void renderTankCurrentLocation(Graphics2D g2) {
            tank1.drawTank(g2);
            tank2.drawTank(g2);
    }

    private void renderMap(Graphics2D g2) {
        for (int row = 0; row < Globals.MAX_NUMBER_OF_BLOCKS; row++) {
            for (int col = 0; col < Globals.MAX_NUMBER_OF_BLOCKS; col++) {
                String value = map[row][col];
                int y = row * Globals.BLOCK_SIZE;
                int x = col * Globals.BLOCK_SIZE;
                if (value.equals(MapReader.WALL)) {
                    renderUnBreakableWall(g2, x, y);
                    continue;
                }
                if (value.equals(MapReader.BREAKABLE_WALL)) {
                    renderBreakableWall(g2, x, y);
                    continue;
                }
                if (value.equals(MapReader.SPACE)) {
                    // Do nothing
                    continue;
                }
                if (value.equals(MapReader.TANK_1)) {
                    tank1.drawTank(g2);
                    continue;
                }
                if (value.equals(MapReader.TANK_2)) {
                    tank2.drawTank(g2);
                    continue;
                }
            }
        }
    }

    private void renderUnBreakableWall(Graphics2D g2, int x, int y) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/UnbreakableWall.png");
        g2.drawImage(image, x, y, Globals.BLOCK_SIZE, Globals.BLOCK_SIZE, this);
        g2.finalize();
    }

    private void renderBreakableWall(Graphics2D g2, int x, int y) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/BreakableWall.png");
        g2.drawImage(image, x, y, Globals.BLOCK_SIZE, Globals.BLOCK_SIZE, this);
        g2.finalize();
    }

    private void renderBackground(Graphics2D g2) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/Background.png");
        g2.drawImage(image, 0, 0, Globals.BOARD_SIZE, Globals.BOARD_SIZE, this);
        g2.finalize();
    }

    public void renderGameOver(Graphics2D g2) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/gameover/gameover_" + collision.getTankWon().tankName +".png");

        playMusic.stop();

        g2.drawImage(image, 100, 300, 824, 400, this);
        g2.finalize();
    }

    private void renderBullets(Graphics2D g2) {
        for (Bullet b : bullets) {
            Image image = Toolkit.getDefaultToolkit().getImage("resources/bullets/bullets_"
                    + b.getOrientation().name().toLowerCase() + ".png");
            g2.drawImage(image, (int)b.getX(), (int)b.getY(), this);
            g2.finalize();
        }
    }

    public void moveBullets(TankOrientation tankOrientation1, TankOrientation tankOrientation2) {
        assert tankOrientation1 != null : "tank1 orientation cannot be null";
        assert tankOrientation2 != null : "tank2 orientation cannot be null";
        Iterator<Bullet> iter = bullets.iterator();

        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            if (collision.validateBullettoWallCollision(bullet) ||
                    collision.validateBullettoTankCollision(bullet, tank1, tank2)) {
                Explosion explosion = new Explosion(bullet.getX(),bullet.getY());
                explosions.add(explosion);
                explosionSound.play();
                iter.remove();
            } else {
                bullet.moveBullet();
            }
        }
    }

    public void renderExplosion(Graphics2D g2){
        for(int i = 0; i < explosions.size();i++) {
            Explosion exp = explosions.get(i);
            Image image = exp.nextImageOrNull();
            if (image == null) {
                explosions.remove(exp);
                exp = null;
            } else {
                g2.drawImage(image,(int) exp.getX(),(int) exp.getY(), this);
                g2.finalize();
            }
        }
    }

    public void run() {
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
            try {
                thread.sleep(15);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

}