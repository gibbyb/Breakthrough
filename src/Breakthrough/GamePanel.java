package Breakthrough;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, ComponentListener
{
    public Board board;
    public Player player1, player2;
    public Player curPlayer;
    public int ScreenLength = 400;
    public int Border = ScreenLength / 20;
    public int UnitSize;
    public boolean running = false;

    // Integers to track mouse click movement for player moves.
    private int currentRow, currentCol;

    // Constructor
    public GamePanel(Board board, Player player1, Player player2)
    {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.UnitSize = (ScreenLength - (Border * 2)) / this.board.Size;
        this.curPlayer = this.player1;

        this.setPreferredSize(new Dimension(ScreenLength, ScreenLength));
        this.setFocusable(true);
        startGame();

        this.addComponentListener(this);
        // Add a mouse listener to listen for mouse clicks
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check which piece was clicked on and store its row and column
                currentRow = (e.getY() - Border) / UnitSize;
                currentCol = (e.getX() - Border) / UnitSize;
            }
        });

        // Add a mouse listener to listen for mouse releases
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e) {
                // Check the target location and call the movePiece method to move the piece
                int targetRow = (e.getY() - Border) / UnitSize;
                int targetColumn = (e.getX() - Border) / UnitSize;
                // This if statement is what literally moves the piece
                if (board.movePiece(currentRow,currentCol,targetRow,targetColumn,curPlayer))
                {
                    // Update player's turn
                    curPlayer = (curPlayer == player1) ? player2 : player1;
                    repaint();
                }
            }
        });
    }

    public void startGame()
    {
        running = true;
    }

    public void paintComponent(Graphics g)
    {
        // Literally all this code is just to create the chessboard and make it look pretty.
        if (running)
        {
            g.setColor(new Color(93,67,44));
            g.fillRect(0,0, ScreenLength, Border); // top border
            g.fillRect(0,0,Border,ScreenLength); // left border
            g.fillRect(0,(UnitSize*8) + Border,ScreenLength,Border + 10); // bottom border
            g.fillRect((UnitSize*8) + Border,0,Border+10,ScreenLength); // right border

            g.setColor(new Color(223,208,183));
            // top border
            g.fillRect((int)(Border * 0.8),(int)(Border * 0.8),(int)(UnitSize*8 + 2*(Border * 0.2)), (int)(Border*0.2));
            //left border
            g.fillRect((int)(Border * 0.8),(int)(Border * 0.8), (int)(Border*0.2),(int)(UnitSize*8 + 2*(Border * 0.2)));
            // bottom border
            g.fillRect((int)(Border * 0.8), (UnitSize*8) + Border, (int)(UnitSize*8 + 2*(Border * 0.2)), (int)(Border*0.2));
            // right border
            g.fillRect((UnitSize*8) + Border,(int)(Border * 0.8), (int)(Border*0.2), (int)(UnitSize*8 + 2*(Border * 0.2)));

            // This nested for loop is what prints our checker board as well as adds in the pieces.
            for (int i = 0; i < board.Size; i++)
            {
                for (int j = 0; j < board.Size; j++)
                {
                    if ((i + j) % 2 == 1)
                    {
                        g.setColor(new Color(93,67,44));
                        g.fillRect(j*UnitSize + Border, i *UnitSize + Border, UnitSize, UnitSize);
                    }
                    else if ((i + j) % 2 == 0)
                    {
                        g.setColor(new Color(223,208,183));
                        g.fillRect(j*UnitSize + Border, i *UnitSize + Border, UnitSize, UnitSize);
                    }

                    int diameter = (int)(UnitSize * 0.4);
                    int x = j * UnitSize + Border + (UnitSize - diameter) / 2;
                    int y = i * UnitSize + Border + (UnitSize - diameter) / 2;

                    if (this.board.Array[i][j] == 2)
                    {
                        // Draw a white piece at (x, y)
                        g.setColor(Color.WHITE);
                        g.fillOval(x, y, diameter, diameter);
                    }
                    else if (this.board.Array[i][j] == 1)
                    {
                        // Draw a black piece at (x, y)
                        g.setColor(Color.BLACK);
                        g.fillOval(x, y, diameter, diameter);
                    }
                }
            }
            if (board.winCondition())
                running = false;
            else if (!curPlayer.isHuman)
            {
                curPlayer.headNode.checkMoves(curPlayer,board);
                curPlayer.headNode.defensiveHeuristic(curPlayer);
                board.makeMove(curPlayer.headNode.bestNextMove.move);
                curPlayer.setHeadNode(board);
                // Update player's turn
                curPlayer = (curPlayer == player1) ? player2 : player1;
                repaint();
            }
        }
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
        // Update the ScreenLength variable to be the new width of the GamePanel
        ScreenLength = Math.min(e.getComponent().getWidth(), e.getComponent().getHeight());
        Border = ScreenLength / 20;
        UnitSize = (ScreenLength - (Border * 2)) / this.board.Size;
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
