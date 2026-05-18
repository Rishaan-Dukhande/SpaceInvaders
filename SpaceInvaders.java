/**
 * @tutorial Coding, Kenny Yip. “ Code Space Invaders in Java.” YouTube, 25 June 2024, www.youtube.com/watch?v=UILUMvjLEVU. Accessed 17 May 2026. 
 *
 * @author Rishaan D
 * @version 5/16/2026
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random; //for randomized color
import javax.swing.*;

/*
 * JPanel class allows for drawing - utilizes inheritance to have JPanel functions with additional functions specified to the game
 * implements ActionListener and KeyListener to track keyboard strokes and performed actions
 */
public class SpaceInvaders extends JPanel implements ActionListener, KeyListener
{
    /*
     * This class is used to create bullets, the ship, and the aliens
     */
    class Sprite
        {
            int x;
            int y;
            int width;
            int height;
            Image img;
            boolean alive = true; // only for aliens
            boolean used = false; //used before bullets shot by user
            
            /*
             * The sprite constructor initializes all of the attributes of each sprite
             */
            public Sprite(int x, int y, int width, int height, Image img)
            {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.img = img;
                
            }
        }
    //window variables for creating frame (16 x 16 grid with a unit of 32x32 px)
    int tileSize = 32;
    int rows = 16;
    int columns = 16;
    int boardWidth = tileSize * columns; //32*16 = 512 px
    int boardHeight = tileSize * rows; //32*16 = 512 px
        
    //Variables to store images
    Image shipImg;
    Image alienImg;
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    //stores all alien images in a list for randomization
    ArrayList<Image> alienImgArray;
    
    //ship variables
    int shipWidth = tileSize*2; //64px
    int shipHeight = tileSize; //32px
    int shipX = tileSize*columns/2 - tileSize;
    int shipY = boardHeight - tileSize*2;
    int shipVelocityX = tileSize; //ship moves 32 px
    Sprite ship;
    
    //aliens
    ArrayList<Sprite> alienArray; //list to store alien (type sprite)
    int alienWidth = tileSize*2;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;
    
    //create array (rows and columns)
    int alienRows = 2; 
    int alienColumns = 3;
    int alienCount = 0; //number of aliens to defeat (row * column)
    int alienVelocityX = 1; //aliens move 1 tile every time;
    
    //bullets
    ArrayList<Sprite> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVelocityY = -10; //bullets moving speed
    
    //creates a game loop timer to reload images at a constant rate
    Timer gameLoop;
    
    int score = 0;
    boolean gameOver = false;
    

    /**
     * Constructor for objects of class SpaceInvaders - intializes frame, images, game timer, and arrays to store sprites
     */
    public SpaceInvaders()
    {
        // initializes frame
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this); //gives objects a keyListener attribute to listen for key inputs
        
        //load images
        shipImg = new ImageIcon(getClass().getResource("./ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("./alien.png")).getImage();
        alienCyanImg = new ImageIcon(getClass().getResource("./alien-cyan.png")).getImage();
        alienMagentaImg = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage();
        
        //adds alien-images to list
        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);
        
        ship = new Sprite(shipX, shipY, shipWidth, shipHeight, shipImg);
        alienArray = new ArrayList<Sprite>();
        bulletArray = new ArrayList<Sprite>();
        
        //game timer
        gameLoop = new Timer(1000/60, this); //reloads every 16.6ms at 60fps
        createAliens();
        gameLoop.start();
        
    }
    /*
     * This function paints each component of the Space Invaders game by calling the paint method from the parent JPanel class and then calls the draw method 
     * specific to the SpaceInvaders class to draw ship, bullets, aliens, and score on the JPanel in the JFrame
     */
    public void paintComponent(Graphics g)
    {
            super.paintComponent(g);
            draw(g);
    }
    /*
     * The draw method draws a ship, an array of aliens, bullets that come from the ship, and the score on the screen. 
     * It utilizes arrays for aliens and bullets to draw more than one of each type of sprite
     */
    public void draw(Graphics g)
    {
        // draw ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);
        
        //aliens
        for (int i = 0; i < alienArray.size(); i++) 
        {
            Sprite alien = alienArray.get(i);
            if (alien.alive) 
            {
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }
        
        //bullets
        g.setColor(Color.white);
        for (int i = 0; i < bulletArray.size(); i++) {
            Sprite bullet = bulletArray.get(i);
            if (!bullet.used) {
                g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height);
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }
        
        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }
        else {
            g.drawString(String.valueOf(score), 10, 35);
        }
    }
    
    /*
     * The move method uses for-loops to iterate through the alien list to move all of them to either side
     * Once the aliens touch a border of the JFrame, the aliens move the other way and go down by one row
     * 
     * The method moves the bullets at a constant velocity from the ship towards the aliens
     * Once bullets are out of screen, they are removed from the bulletArray
     * Once all the aliens are defeated, the program moves to the next level by incrementing columns and rows of aliens by 1
     */
    public void move()
    {
        //moves aliens
        for (int i = 0; i < alienArray.size(); i++) 
        {
            Sprite alien = alienArray.get(i);
            if (alien.alive) 
            {
                alien.x += alienVelocityX;
                
                //if alien touches the borders, switch direction aliens move
                if(alien.x + alien.width >= boardWidth || alien.x <=0) {
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX*2;
                    
                    //move all aliens down one row
                    for (int j = 0; j < alienArray.size(); j++){
                        alienArray.get(j).y += alienHeight;
                    }
                }
                
                if (alien.y >= ship.y) {
                    gameOver = true;
                }
            }
        }
        
        //moves bullets
        for (int i = 0; i < bulletArray.size(); i++){
            Sprite bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;
            
            for (int j = 0; j < alienArray.size(); j++){
                Sprite alien = alienArray.get(j);
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)){
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
        }
        
        //clear unncesseary bullets that are out of screen
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)){
            bulletArray.remove(0); //removes the first element of the array (unncessessary bullet)
        }
        
        //next level
        if (alienCount == 0) {
            score += alienColumns * alienRows * 100; //bonus points for clearing each level
            //increase the number of aliens in columns and rows by 1
            alienColumns = Math.min(alienColumns + 1, columns/2 - 2); //cap column at 16/2 -2 = 6
            alienRows = Math.min(alienRows + 1, rows - 6); //cap row at 16-6 = 10
            //clears aliens and bullets so that bullets don't destroy aliens before they are constructed
            alienArray.clear();
            bulletArray.clear();
            alienVelocityX = 1; //resets velocity to go right once all aliens are reloaded
            createAliens();
            
        }
    }
    /*
     * Creates Aliens in an array formation by iterating through two for-loops to place the aliens in specific areas side-by-side of the 16x16 grid frame
     * The aliens are then added to the alienArray
     */
    public void createAliens() 
    {
        Random random = new Random();
        for(int r = 0; r < alienRows; r++)
        {
            for (int c = 0; c < alienColumns; c++)
            {
                int randomImgIndex = random.nextInt(alienImgArray.size()); //upperbound round - random numbers 0 - size - 1
                Sprite alien = new Sprite(
                    alienX + c*alienWidth, //determines where to place alien to make sure they are side by side, not for alien at index 0
                    alienY + r*alienHeight,
                    alienWidth,
                    alienHeight,
                    alienImgArray.get(randomImgIndex)
                );
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size(); 
    }
    /*
     * This method detects if a bullet collides with an alien 
     * This method works by making sure all portions of an edge overlap by comparing the borders of a bullet and alien
     * I learned about the derivation of this formula from: 
     * Dev, Aristurtle. “ Axis Aligned Bounding Box Collision Detection | MonoGame.” YouTube, 10 Mar. 2024, www.youtube.com/watch?v=UOfbGeq0ZkM&t=94s. 
     * Accessed 17 May 2026. 
     */
    public boolean detectCollision(Sprite a, Sprite b) {
        return a.x < b.x + b.width &&
        a.x + a.width > b.x &&
        a.y < b.y + b.height &&
        a.y + a.height > b.y;
    }
    /*
     * Unimplemented method for ActionListener
     * repaints the frame
     */
    @Override
    public void actionPerformed(ActionEvent e){
        move(); //moves 60 px every 1 second
        repaint(); //built in function to redraw objects
        if (gameOver) {
            gameLoop.stop();
        }
    }
    
    /*
     * Unimplemented method for KeyListener
     * Knows if the key has been pressed (like if a key is continously being pressed = shoot)
     */
    @Override
    public void keyPressed(KeyEvent e) {}

    /*
     * Unimplemented method for KeyListener
     * Finds out what key is pressed
     */
    @Override
    public void keyTyped(KeyEvent e) {}
    
    /*
     * Unimplemented method for KeyListener
     * Knows if the key has been released
     * 
     * If the game is over and any key is pressed, then it will restart the game and reset all of the variables associated with the inital sprites.
     * If the left arrow key is pressed, the ship moves one tile left
     * If the right arrow key is pressed, the ship moves one tile right
     * If the space key is pressed, a bullet is shot from the ship and accelerates towards the array of aliens
     */
    @Override
    public void keyReleased(KeyEvent e) 
    {
        if (gameOver) { //restarts game if any key is pressed
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            score = 0;
            alienVelocityX = 1;
            alienColumns = 3;
            alienRows = 2;
            gameOver = false;
            createAliens();
            gameLoop.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0)
        {
            ship.x -= shipVelocityX; //moves ship left one tile
        } 
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVelocityX <= boardWidth){ //added to account for right side of ship
            ship.x += shipVelocityX; //moves ship right one tile
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) 
        {
            Sprite bullet = new Sprite(ship.x + shipWidth * 15/32, ship.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet);
        }
    }
    
}