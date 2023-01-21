package Breakthrough;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.util.Random;

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

                Move userMove = new Move(currentRow,currentCol,targetRow,targetColumn,board,curPlayer);
                // This if statement is what literally moves the piece
                if (board.makeMove(userMove))
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

    public void computerMove()
    {
        if (!curPlayer.isHuman)
        {
            Random rand = new SecureRandom();
            // Check for all available player moves. Note: One of the moves found here WILL be chosen.
            curPlayer.checkMoves(board);
            // First lets run an offensive heuristic on our available moves
            // and find a move that takes the opponents piece if available.
            curPlayer.headNode.offensiveHeuristic();

            // if we are player 2 and our best move does not end with us taking any pieces, lets dig a bit deeper.
            if (curPlayer.Number == 2 )//&& curPlayer.headNode.bestNextMove.move.remainingP1Pieces >= board.remainingP1Pieces)
            {
                // create a node holder for our opponents theoretical best move that they can make
                Node bestMove = curPlayer.headNode.bestNextMove;
                Node bestFoundMove = bestMove;

                // for all our available moves,
                for (Node P2Move1: curPlayer.headNode.nextMoves)
                {
                    Board P2Move1Board = new Board(board,P2Move1.move);
                    // lets check all the opponents available moves.
                    P2Move1.checkOpponentMoves(P2Move1Board);
                    // run through offensive heuristic from opponents pov, meaning the
                    // best move in each p2move1 should be one in which our opponent takes one of our pieces
                    P2Move1.offensiveHeuristic();
                    Board P1Move1Board = new Board(P2Move1Board,P2Move1.bestNextMove.move);

                    // Check all available moves to you from opponents best move.
                    Node predictedOpponentMove = P2Move1.bestNextMove;
                    predictedOpponentMove.checkOpponentMoves(P1Move1Board);
                    // Figure out your best move
                    predictedOpponentMove.offensiveHeuristic();
                    if (predictedOpponentMove.bestNextMove.move.remainingP1Pieces < bestFoundMove.move.remainingP1Pieces)
                    {
                        bestFoundMove = predictedOpponentMove.bestNextMove;
                        bestMove = P2Move1;
                    }
                }
                if (bestFoundMove.move.remainingP1Pieces == board.remainingP1Pieces)
                    board.makeMove(curPlayer.headNode.nextMoves.get(rand.nextInt(curPlayer.headNode.nextMoves.size())).move);
                else
                    board.makeMove(bestMove.move);
            }
            else if (curPlayer.Number == 1 && curPlayer.headNode.bestNextMove.move.remainingP2Pieces == board.remainingP2Pieces)
            {
                curPlayer.headNode.defensiveHeuristic();
                if (curPlayer.headNode.bestNextMove.move.remainingP2Pieces == board.remainingP2Pieces)
                    board.makeMove(curPlayer.headNode.nextMoves.get(rand.nextInt(curPlayer.headNode.nextMoves.size())).move);
            }
            else
                board.makeMove(curPlayer.headNode.bestNextMove.move);

            // Update player's turn
            curPlayer = (curPlayer == player1) ? player2 : player1;
            repaint();
        }
    }

    public void gameOver(Graphics g)
    {
            running = false;
            g.setColor(Color.RED);
            g.setFont(new Font("TimesRoman", Font.BOLD, 50));
            FontMetrics metrics = getFontMetrics(g.getFont());
            int weiner = (curPlayer.Number % 2) + 1;
            String winner = "Player " + weiner + " Wins!";
            g.drawString(winner, (ScreenLength - metrics.stringWidth(winner))/2, ScreenLength/2);
    }

    public void paintComponent(Graphics g)
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

        // This nested for loop is what prints our checkerboard as well as adds in the pieces.
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
        if (board.winCondition()) gameOver(g);
        else computerMove();
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
