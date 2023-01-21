package Breakthrough;

import java.util.ArrayList;

public class Node
{
    public Player player;
    public Node parent;
    public ArrayList<Node> nextMoves;
    public Node bestNextMove;
    public Move move;

    /* Constructor specifically for head node */
    public Node(Player player)
    {
        this.player = player;
        this.parent = null;
        this.move = null;
        this.nextMoves = new ArrayList<>();
    }

    /* Constructor for all available moves nodes. */
    public Node(Move move, Node parent)
    {
        this.player = parent.player;
        this.move = move;
        this.parent = parent;
        this.nextMoves = new ArrayList<>();
    }

    public void checkMoves(Board board)
    {
        for (int i = 0; i < board.Size; i++)
        {
            for (int j = 0; j < board.Size; j++)
            {
                if (board.Array[i][j] == player.Number)
                {
                    int offset = player.Number == 1 ? -1 : 1;
                    // Check for moving straight up
                    if (i + offset >= 0 && i + offset < board.Size && board.Array[i + offset][j] != player.Number && board.Array[i + offset][j] != (player.Number % 2) + 1)
                        this.nextMoves.add(new Node(new Move(i, j, i + offset, j, board, player), this));
                    // Check for up and to the right
                    if (i + offset >= 0 && i + offset < board.Size && j + 1 < 8 && board.Array[i + offset][j + 1] != player.Number)
                        this.nextMoves.add(new Node(new Move(i, j, i + offset, j + 1, board, player), this));
                    // Check for up and to the left
                    if (i + offset >= 0 && i + offset < board.Size && j - 1 >= 0 && board.Array[i + offset][j - 1] != player.Number)
                        this.nextMoves.add(new Node(new Move(i, j, i + offset, j - 1, board, player), this));
                }
            }
        }
    }

    public void checkOpponentMoves(Board board)
    {
        Player player = new Player(this.player.Number%2 +1, false);
        for (int i = 0; i < board.Size; i++)
        {
            for (int j = 0; j < board.Size; j++)
            {
                if (board.Array[i][j] == player.Number)
                {
                    int offset = player.Number == 1 ? -1 : 1;
                    // Check for moving straight up
                    if (i + offset >= 0 && i + offset < board.Size && board.Array[i + offset][j] != player.Number && board.Array[i + offset][j] != (player.Number % 2) + 1)
                        this.nextMoves.add(new Node(new Move(i, j, i + offset, j, board, player), this));
                    // Check for up and to the right
                    if (i + offset >= 0 && i + offset < board.Size && j + 1 < 8 && board.Array[i + offset][j + 1] != player.Number)
                        this.nextMoves.add(new Node(new Move(i, j, i + offset, j + 1, board, player), this));
                    // Check for up and to the left
                    if (i + offset >= 0 && i + offset < board.Size && j - 1 >= 0 && board.Array[i + offset][j - 1] != player.Number)
                        this.nextMoves.add(new Node(new Move(i, j, i + offset, j - 1, board, player), this));
                }
            }
        }
    }

    public void defensiveHeuristic()
    {
        this.bestNextMove = this.nextMoves.get(0);
        for (int i = 1; i < this.nextMoves.size(); i++)
        {
            if (player.Number == 1)
            {
                if (this.nextMoves.get(i).move.remainingP1Pieces > this.bestNextMove.move.remainingP1Pieces)
                    this.bestNextMove = this.nextMoves.get(i);
            }
            else if (player.Number == 2)
            {
                if (this.nextMoves.get(i).move.remainingP2Pieces > this.bestNextMove.move.remainingP2Pieces)
                    this.bestNextMove = this.nextMoves.get(i);
            }
        }
    }

    public void offensiveHeuristic()
    {
        this.bestNextMove = this.nextMoves.get(0);
        for (int i = 1; i < this.nextMoves.size(); i++)
        {
            if (player.Number == 1)
            {
                if (this.nextMoves.get(i).move.remainingP2Pieces < this.bestNextMove.move.remainingP2Pieces)
                    this.bestNextMove = this.nextMoves.get(i);
            }
            else if (player.Number == 2)
            {
                if (this.nextMoves.get(i).move.remainingP1Pieces < this.bestNextMove.move.remainingP1Pieces)
                    this.bestNextMove = this.nextMoves.get(i);
            }
        }
    }


}


