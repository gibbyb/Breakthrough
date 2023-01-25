package Breakthrough;

import javafx.scene.image.Image;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
    public GameFrame(Board board, Player player1, Player player2)
    {
        GamePanel gamePanel = new GamePanel(board,player1,player2);
        this.add(gamePanel);
        this.setTitle("Breakthrough");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(400,440));
        ImageIcon img = new ImageIcon("Icon/BreakthroughIcon256.png");
        this.setIconImage(img.getImage());
    }
}
