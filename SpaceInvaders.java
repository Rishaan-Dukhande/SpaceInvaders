/**
 * Write a description of class SpaceInvaders here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random; //for randomized color
import javax.swing.*;

//JPanel class allows for drawing - utilizes inheritance to 
//have JPanel functions with additional functions specified to the game
public class SpaceInvaders extends JPanel implements ActionListener, KeyListener
{
    class Sprite
        {
            int x;
            int y;
            int width;
            int height;
            Image img;
            boolean alive = true; // only for aliens
            boolean used = false; //used before bullets shot by user
            
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
    
    Timer gameLoop;
        

    /**
     * Constructor for objects of class SpaceInvaders
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
        
        //game timer
        gameLoop = new Timer(1000/60, this); //reloads every 16.6ms at 60fps
        createAliens();
        gameLoop.start();
        
    }
    public void paintComponent(Graphics g)
    {
            super.paintComponent(g);
            draw(g);
    }
    
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
        
    }
    
    /*
     * iterates through all aliens to move them
     */
    public void move()
    {
        for (int i = 0; i < alienArray.size(); i++) 
        {
            Sprite alien = alienArray.get(i);
            if (alien.alive) 
            {
                alien.x += alienVelocityX;
            }
        }
    }
    /*
     * Creates Aliens in a array formation by iterating through two for-loops to place the aliens in specific areas of the 16x16 grid frame
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
     * Unimplemented method for ActionListener
     * repaints the frame
     */
    @Override
    public void actionPerformed(ActionEvent e){
        move(); //moves 60 px every 1 second
        repaint(); //built in function to redraw objects
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
     */
    @Override
    public void keyReleased(KeyEvent e) 
    {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0)
        {
            ship.x -= shipVelocityX; //moves ship left one tile
        } 
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVelocityX <= boardWidth){ //added to account for right side of ship
            ship.x += shipVelocityX; //moves ship right one tile
        }
    }
    
}