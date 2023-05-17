package Breakthrough;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GamePanel extends JPanel implements ActionListener, ComponentListener
{
    public Board board;

    private static int DEPTH = 3;
    public int screenLength = 400;
    public int border = screenLength / 20;
    public int unitSize;
    public boolean run;
    private int curRow, curCol;

    public GamePanel(Board board)
    {
        this.board = board;
        this.unitSize = (screenLength - 2 * border) / board.size;
        this.setPreferredSize(new Dimension(screenLength, screenLength));
        this.setFocusable(true);
        run = true;

        this.addComponentListener(this);
        // Add a mouse listener to listen for mouse clicks
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check which piece was clicked on and store its row and column
                curRow = (e.getY() - border) / unitSize;
                curCol = (e.getX() - border) / unitSize;
            }
        });

        // Add a mouse listener to listen for mouse releases
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e) {
                // Check the target location and call the movePiece method to move the piece
                int targetRow = (e.getY() - border) / unitSize;
                int targetColumn = (e.getX() - border) / unitSize;
                // This if statement is what literally moves the piece
                if (board.makeMove(targetRow, targetColumn, curRow, curCol))
                {
                    makeComputerMove();
                    repaint();
                }
            }
        });
    }
    
    public void makeComputerMove() {
    if (board.curPlayer == 2) {
        int[] move = board.bestMove(DEPTH);
        board.makeMove(move[0], move[1], move[2], move[3]);
        repaint();
    }
}

    public void gameOver(Graphics g)
    {
        run = false;
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String winner = "Player " + (board.curPlayer % 2 + 1) + " Wins!";
        g.drawString(winner, (screenLength - metrics.stringWidth(winner))/2, screenLength/2);
    }

    public void paintComponent(Graphics g)
    {
        g.setColor(new Color(93,67,44));
        g.fillRect(0,0, screenLength, border); // top border
        g.fillRect(0,0,border, screenLength); // left border
        g.fillRect(0, (unitSize*8) + border,screenLength, border + 10); // bottom border
        g.fillRect((unitSize*8) + border,0, border+10, screenLength); // right border

        g.setColor(new Color(223,208,183));
        // top border
        g.fillRect((int)(border * 0.8),(int)(border * 0.8),(int)(unitSize*8 + 2*(border * 0.2)), (int)(border*0.2));
        //left border
        g.fillRect((int)(border * 0.8),(int)(border * 0.8), (int)(border*0.2),(int)(unitSize*8 + 2*(border * 0.2)));
        // bottom border
        g.fillRect((int)(border * 0.8), (unitSize*8) + border, (int)(unitSize*8 + 2*(border * 0.2)), (int)(border*0.2));
        // right border
        g.fillRect((unitSize*8) + border,(int)(border * 0.8), (int)(border*0.2), (int)(unitSize*8 + 2*(border * 0.2)));

        // This nested for loop is what prints our checkerboard as well as adds in the pieces.
        for (int i = 0; i < board.size; i++)
        {
            for (int j = 0; j < board.size; j++)
            {
                if ((i + j) % 2 == 1)
                {
                    g.setColor(new Color(93,67,44));
                    g.fillRect(j * unitSize + border, i * unitSize + border, unitSize, unitSize);
                }
                else if ((i + j) % 2 == 0)
                {
                    g.setColor(new Color(223,208,183));
                    g.fillRect(j*unitSize + border, i * unitSize + border, unitSize, unitSize);
                }

                int diameter = (int)(unitSize * 0.4);
                int x = j * unitSize + border + (unitSize - diameter) / 2;
                int y = i * unitSize + border + (unitSize - diameter) / 2;

                if (this.board.array[i][j] == 2)
                {
                    // Draw a white piece at (x, y)
                    g.setColor(Color.WHITE);
                    g.fillOval(x, y, diameter, diameter);
                }
                else if (this.board.array[i][j] == 1)
                {
                    // Draw a black piece at (x, y)
                    g.setColor(Color.BLACK);
                    g.fillOval(x, y, diameter, diameter);
                }
            }
        }
        if (board.winCondition()) gameOver(g);
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
        // Update the ScreenLength variable to be the new width of the GamePanel
        screenLength = Math.min(e.getComponent().getWidth(), e.getComponent().getHeight());
        border = screenLength / 20;
        unitSize = (screenLength - (border * 2)) / this.board.size;
    }
    @Override
    public void actionPerformed(ActionEvent e) {}
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
}
