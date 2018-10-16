package com.gregei.potato8.desktop;

import com.gregei.potato8.core.Chip8;
import com.gregei.potato8.desktop.views.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Potato8Desktop {
    /**
     * The width and height of the emulator window.
     */
    private final int WIDTH = 64, HEIGHT = 32;

    /**
     * The scale of the emulator window.
     */
    private int scale;

    /**
     * The title of the emulator.
     */
    private final String TITLE = "Potato-8";

    /**
     * The path of the rom to be loaded.
     */
    private String romPath;

    /**
     * Instance of the Chip8 class
     */
    private Chip8 chip8 = new Chip8();

    /**
     * Instance of the MainView class
     */
    private MainView mainView;

    /**
     * The flag to represent if the emulator is running
     */
    private boolean emulatorRunning = false;

    /**
     * Initialize the emulator.
     */
    private void init(){
        mainView = new MainView(WIDTH, HEIGHT, scale);
        mainView.setTitle(TITLE);
        mainView.setVisible(true);
        mainView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainView.getContentPane().setFocusable(true);
        mainView.getContentPane().requestFocusInWindow();
        mainView.setResizable(false);

        mainView.getContentPane().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_1:
                        chip8.setKey(0, true);
                        break;

                    case KeyEvent.VK_2:
                        chip8.setKey(1, true);
                        break;

                    case KeyEvent.VK_3:
                        chip8.setKey(2, true);
                        break;

                    case KeyEvent.VK_4:
                        chip8.setKey(3, true);
                        break;

                    case KeyEvent.VK_Q:
                        chip8.setKey(4, true);
                        break;

                    case KeyEvent.VK_W:
                        chip8.setKey(5, true);
                        break;

                    case KeyEvent.VK_E:
                        chip8.setKey(6, true);
                        break;

                    case KeyEvent.VK_R:
                        chip8.setKey(7, true);
                        break;

                    case KeyEvent.VK_A:
                        chip8.setKey(8, true);
                        break;

                    case KeyEvent.VK_S:
                        chip8.setKey(9, true);
                        break;

                    case KeyEvent.VK_D:
                        chip8.setKey(10, true);
                        break;

                    case KeyEvent.VK_F:
                        chip8.setKey(11, true);
                        break;

                    case KeyEvent.VK_Z:
                        chip8.setKey(12, true);
                        break;

                    case KeyEvent.VK_X:
                        chip8.setKey(13, true);
                        break;

                    case KeyEvent.VK_C:
                        chip8.setKey(14, true);
                        break;

                    case KeyEvent.VK_V:
                        chip8.setKey(15, true);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_1:
                        chip8.setKey(0, false);
                        break;

                    case KeyEvent.VK_2:
                        chip8.setKey(1, false);
                        break;

                    case KeyEvent.VK_3:
                        chip8.setKey(2, false);
                        break;

                    case KeyEvent.VK_4:
                        chip8.setKey(3, false);
                        break;

                    case KeyEvent.VK_Q:
                        chip8.setKey(4, false);
                        break;

                    case KeyEvent.VK_W:
                        chip8.setKey(5, false);
                        break;

                    case KeyEvent.VK_E:
                        chip8.setKey(6, false);
                        break;

                    case KeyEvent.VK_R:
                        chip8.setKey(7, false);
                        break;

                    case KeyEvent.VK_A:
                        chip8.setKey(8, false);
                        break;

                    case KeyEvent.VK_S:
                        chip8.setKey(9, false);
                        break;

                    case KeyEvent.VK_D:
                        chip8.setKey(10, false);
                        break;

                    case KeyEvent.VK_F:
                        chip8.setKey(11, false);
                        break;

                    case KeyEvent.VK_Z:
                        chip8.setKey(12, false);
                        break;

                    case KeyEvent.VK_X:
                        chip8.setKey(13, false);
                        break;

                    case KeyEvent.VK_C:
                        chip8.setKey(14, false);
                        break;

                    case KeyEvent.VK_V:
                        chip8.setKey(15, false);
                        break;
                }
            }
        });

        chip8.setCpuRunning(chip8.loadGame(romPath));

        boolean loaded = chip8.loadGame(romPath);
        chip8.setCpuRunning(loaded);

        if (loaded){
            System.out.println("Rom Loaded Successfully!");
            chip8.setCpuRunning(true);
            startLoop();
        }
        else {
            System.out.println("Rom Load Error!");
        }
    }

    /**
     * Starts the game loop.
     */
    private void startLoop(){
        if (emulatorRunning){
            //System.out.println("Emulator Already Started");
            return;
        }

        emulatorRunning = true;

        while (emulatorRunning){
            if (chip8.isCpuRunning()){
                chip8.runCycle();
            }

            if (chip8.isDrawReady()){
                render();
                chip8.setDraw(false);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the game loop.
     */
    private void stopLoop(){
        emulatorRunning = false;
    }

    /**
     * Renders the emulator frame buffer.
     */
    private void render(){
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        int[] buffer = chip8.getFrameBuffer();

        for (int y = 0; y < 32; ++y)
        {
            for (int x = 0; x < 64; ++x)
            {
                int color = buffer[(y * 64) + x];

                if (color == 1){
                    image.setRGB(x, y, new Color(255, 255, 255, 255).getRGB());
                }
                else{
                    image.setRGB(x, y, new Color(0, 0, 0, 255).getRGB());
                }
            }
        }

        mainView.repaint(image);
    }

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2){
            System.out.println("Wrong Argument Length!");
            System.out.print("Usage: java -jar potato8.jar ROM_PATH [OPTIONAL SCALE]");
            System.exit(0);
        }

        String scaleParam = args[1];

        for (int i = 0; i < scaleParam.length(); ++i){
            if (!Character.isDigit(scaleParam.charAt(i))){
                System.out.println("Scale Parameter is not a valid number!");
                System.exit(0);
            }
        }

        int scale = Integer.parseInt(args[1]);

        if (scale == 0 || scale < 0){
            scale = 10;
        }



        Potato8Desktop desktop = new Potato8Desktop();
        desktop.romPath = args[0];


        desktop.scale = scale;

        desktop.init();
        desktop.stopLoop();
    }
}