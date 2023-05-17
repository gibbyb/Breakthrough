package Breakthrough;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board
{
    public int[][] array;
    public int size;
    public int p1 =1, p2 =2, curPlayer, p1Pieces, p2Pieces;

    // Initial board constructor. Makes a fresh board & initializes variables.
    public Board(int size)
    {
        this.size = size;
        this.array = new int[size][size];
        this.curPlayer = 1;
        this.p1Pieces = size * 2;
        this.p2Pieces = size * 2;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.array[i][j] = (i < 2) ? 2 : ((i > size - 3) ? 1 : 0);
    }

    // Copy constructor. Used to make a copy of the board for the AI to use.
    public Board(Board parentBoard)
    {
        this.size = parentBoard.size;
        this.array = new int[size][size];
        this.curPlayer = parentBoard.curPlayer;
        this.p1Pieces = parentBoard.p1Pieces;
        this.p2Pieces = parentBoard.p2Pieces;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.array[i][j] = parentBoard.array[i][j];
    }

    // Function to make moves used by both the user via the action listener and the
    // computer.
    public boolean makeMove(int targetRow, int targetCol, int curRow, int curCol)
    {
        if (isValidMove(targetRow, targetCol, curRow, curCol))
        {
            int target = this.array[targetRow][targetCol];
            if (target == 2) this.p2Pieces--;
            if (target == 1) this.p1Pieces--;
            // Update the board array to reflect the move
            this.array[targetRow][targetCol] = this.array[curRow][curCol];
            this.array[curRow][curCol] = 0;
            curPlayer = (curPlayer == p1) ? p2 : p1;
            return true;
        }
        return false;
    }

    // Check if the move is valid according to the rules of Breakthrough. I have literally never played the game so
    // the rules could certainly be more nuanced and I will look into that one day.
    public boolean isValidMove(int targetRow, int targetCol, int curRow, int curCol)
    {
        // Check if the target location is outside the board
        if (targetRow < 0 || targetRow >= this.size || targetCol < 0 || targetCol >= this.size)
            return false;
        // Check if piece belongs to current player & target location isn't occupied by current player
        if (this.array[curRow][curCol] != curPlayer || this.array[targetRow][targetCol] == curPlayer)
            return false;
        // Make sure piece is moving forward
        if ((curPlayer == 2 && targetRow <= curRow) || (curPlayer == 1 && targetRow >= curRow))
            return false;
        // Make sure piece is moving by one space only
        int rowDelta = Math.abs(targetRow - curRow);
        int columnDelta = Math.abs(targetCol - curCol);
        if (rowDelta > 1 || columnDelta > 1)
            return false;
        // if target is not diagonal, make sure it is not occupied by anyone
        else if (this.array[targetRow][targetCol] != 0 && columnDelta == 0)
            return false;
        // If none of the above conditions are met, the move is valid
        return true;
    }

    public List<int[]> getPossibleMoves()
    {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < this.size; i++)
            for (int j = 0; j < this.size; j++)
                if (this.array[i][j] == curPlayer)
                    for (int dx = -1; dx <= 1; dx++)
                        for (int dy = -1; dy <= 1; dy++)
                            if (isValidMove(i + dy, j + dx, i, j))
                                moves.add(new int[]{i + dy, j + dx, i, j});
        return moves;
    }

    public int evaluate()
    {
        int offensiveHeuristic = p1Pieces - p2Pieces;
        int defensiveHeuristic = p2Pieces - p1Pieces;
        int distanceHeuristic = 0;
        for (int i = 0; i < this.size; i++)
        {
            if (this.array[size - 1][i] == curPlayer)
                distanceHeuristic += 10;
            else if (this.array[size - 2][i] == curPlayer)
                distanceHeuristic += 1;
        }
    return Math.max(offensiveHeuristic+distanceHeuristic, defensiveHeuristic+distanceHeuristic);
    }

    public int minimax(Board board, int depth, boolean maximizingPlayer)
    {
        if (depth == 0 || board.winCondition())
            return board.evaluate();
        if (maximizingPlayer)
        {
            int maxEval = Integer.MIN_VALUE;
            List<int[]> moves = board.getPossibleMoves();
            for (int[] move : moves)
            {
                Board childBoard = new Board(board);
                for (int i = 0; i < board.size; i++)
                    childBoard.array[i] = board.array[i].clone();
                childBoard.makeMove(move[0], move[1], move[2], move[3]);
                int eval = minimax(childBoard, depth - 1, false);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        }
        else
        {
            int minEval = Integer.MAX_VALUE;
            List<int[]> moves = board.getPossibleMoves();
            for (int[] move : moves)
            {
                Board childBoard = new Board(board);
                childBoard.array = board.array.clone();
                childBoard.makeMove(move[0], move[1], move[2], move[3]);
                int eval = minimax(childBoard, depth - 1, true);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    public int[] bestMove(int depth)
    {
        int bestScore = Integer.MIN_VALUE;
        List<int[]> bestMoves = new ArrayList<>();
        List<int[]> moves = getPossibleMoves();
        Random random = new Random();

        for (int[] move: moves)
        {
            Board childBoard = new Board(this);
            childBoard.makeMove(move[0], move[1], move[2], move[3]);
            int score = minimax(childBoard, depth, false);
            if (score > bestScore)
            {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(move);
            }
            else if (score == bestScore)
                bestMoves.add(move);
        }
        if (!bestMoves.isEmpty())
            return bestMoves.get(random.nextInt(bestMoves.size()));
        return null;
    }
    
    // Boolean function that returns true when a player wins the game.
    public boolean winCondition()
    {
        /* Check for a normal win  */
        for (int i = 0; i < this.size; i++)
            if (array[0][i] == 1 || array[this.size-1][i] == 2)
                return true;
        if (this.p2Pieces == 0 || this.p1Pieces == 0)
            return true;
        return false;
    }
}