/**
 * @tutorial 
 *
 * @author Rishaan D
 * @version 5/16/2026
 */

import javax.swing.*;
public class Game
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class App
     */
    public Game()
    {
        // initialise instance variables
        x = 0;
    }

    public static void main(String[]args) throws Exception
    {
        //window variables for creating frame (16 x 16 grid with a unit of 32x32 px)
        int tileSize = 32;
        int rows = 16;
        int columns = 16;
        int boardWidth = tileSize * columns; //32*16 = 512 px
        int boardHeight = tileSize * rows; //32*16 = 512 px
        
        //Create Frame
        JFrame frame = new JFrame("Space Invaders");
        frame.setSize(boardWidth, boardHeight);
        //opens window at center of screen
        frame.setLocationRelativeTo(null);
        //Disables ability to resize screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        SpaceInvaders spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus(); //directs keyboard input to specific component
        //set visibility after adding elements to frame
        frame.setVisible(true);
        
    }
}