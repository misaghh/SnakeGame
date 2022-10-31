package main.Java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Graphics extends JPanel implements ActionListener {
    //Next I want to try and add some stationary objects as obstacles

    static final int WIDTH = 800;
    static final int HEIGHT = 800;
    static final int TICK_SIZE = 30;
    static final int BOARD_SIZE = (WIDTH * HEIGHT) / (TICK_SIZE * TICK_SIZE);

    //font of text popping up
    final Font font =new Font("TimesRoman", Font.BOLD, 30);

    //position of snake (X,Y)
    int[] snakePosX = new int[BOARD_SIZE];
    int[] snakePosY = new int[BOARD_SIZE];
    int snakeLength;

    char direction = 'R';
    boolean isMoving = false;

    Food food;
    int foodEaten;

    final Timer timer = new Timer(150, this);
    public Graphics (){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isMoving){
                    switch (e.getKeyCode()){
                        case KeyEvent.VK_LEFT:
                            if(direction != 'R'){
                                direction = 'L';
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if(direction != 'L'){
                                direction = 'R';
                            }
                            break;
                        case KeyEvent.VK_UP:
                            if(direction != 'D'){
                                direction = 'U';
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                            if(direction != 'U'){
                                direction = 'D';
                            }
                            break;
                    }
                } else{
                    start();
                }
            }
        });

        start();
    }

    protected void start(){
        snakePosX = new int[BOARD_SIZE];
        snakePosY = new int[BOARD_SIZE];
        snakeLength = 5;
        foodEaten = 0;
        direction = 'R';
        isMoving = true;
        spawnFood();
        timer.start();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        // random color
        int R = (int)(Math.random()*256);
        int G = (int)(Math.random()*256);
        int B= (int)(Math.random()*256);
        Color color = new Color(R, G, B); //random color, but can be bright or dull

        //to get rainbow, pastel colors
        Random random = new Random();
        final float hue = random.nextFloat();
        final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
        final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
        color = Color.getHSBColor(hue, saturation, luminance);

        if(isMoving){
            g.setColor(Color.BLUE);
            g.fillOval(food.getPosX(), food.getPosY(), TICK_SIZE, TICK_SIZE);


            g.setColor(color);
            for (int i = 0; i < snakeLength; i++){
                //g.fillRect(snakePosX[i], snakePosY[i], TICK_SIZE, TICK_SIZE);
                g.fill3DRect(snakePosX[i], snakePosY[i], TICK_SIZE,TICK_SIZE, false);

            }
        }else {
            String scoreText = String.format("The End... Score: %d... Press any Key to play again!", foodEaten);


            g.setColor(Color.BLACK);
            g.setFont(font);

            g.drawString(scoreText, (WIDTH - getFontMetrics(g.getFont()).stringWidth(scoreText)) / 2, HEIGHT/2);
        }
    }

    protected void move(){
        for(int i = snakeLength; i > 0; i--){
            snakePosX[i] = snakePosX[i-1];
            snakePosY[i] = snakePosY[i-1];
        }

        //direction of the snake head
        switch (direction){
            case 'U' -> snakePosY[0] -= TICK_SIZE;
            case 'D' -> snakePosY[0] += TICK_SIZE;
            case 'L' -> snakePosX[0] -= TICK_SIZE;
            case 'R' -> snakePosX[0] += TICK_SIZE;
        }
    }

    protected void spawnFood(){
        food = new Food();
    }

    protected void eatFood(){
        if((snakePosX[0] == food.getPosX()) && snakePosY[0] == food.getPosY()){
            snakeLength++;
            foodEaten++;
            spawnFood();
        }
    }
    protected void collisionTest(){
        for(int i = snakeLength; i>0; i--){
            if((snakePosX[0] == snakePosX[i]) && (snakePosY[0] == snakePosY[i])){
                isMoving = false;
                break;
            }
        }
        if(snakePosX[0] < 0 || snakePosX[0] > WIDTH - TICK_SIZE || snakePosY[0] < 0 ||snakePosY[0] > HEIGHT - TICK_SIZE){
            isMoving = false;
        }

        if(!isMoving){
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isMoving){
            move();
            collisionTest();
            eatFood();
        }
        repaint();
    }
}
