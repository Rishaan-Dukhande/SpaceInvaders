/**
 * @tutorial Coding, Kenny Yip. “ Code Space Invaders in Java.” YouTube, 25 June 2024, www.youtube.com/watch?v=UILUMvjLEVU. Accessed 17 May 2026. 
 *
 * @author Rishaan D
 * @version 5/16/2026
 */

import javax.swing.*;
/*
 * This class creates a JFrame for the SpaceInvaders game
 * The JFrame is non-resizeable and 512 x 512 px
 */
public class Game
{
    // instance variables
    private int x;

    /**
     * Constructor for objects of class Game
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