package Breakthrough;

import javax.swing.*;
import java.awt.*;
public class GameFrame extends JFrame
{
    public GameFrame(Board board)
    {
        GamePanel gamePanel = new GamePanel(board);
        this.add(gamePanel);
        this.setTitle("Breakthrough");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(400, 400));
        this.setIconImage(new ImageIcon("resources/BreakthroughIcon256.png").getImage());
    }
}
