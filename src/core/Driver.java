package core;

import commons.Globals;

import javax.swing.*;
import java.io.IOException;

public class Driver {

    public static void main(String[] args) throws IOException {
        TankWorld tankWorld = new TankWorld();
        JFrame window = new JFrame();
        window.setTitle("Tank War!!!");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(tankWorld);
	    window.setBounds(50, 50, Globals.BOARD_SIZE + 18, Globals.BOARD_SIZE + 47);
        window.setResizable(true);
        window.setLocationByPlatform(true);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        tankWorld.start();
        System.out.println("Done");
    }
}
