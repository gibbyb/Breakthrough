package Breakthrough;

public class Board
{
    public int[][] Array;
    public int Size;
    public int remainingP1Pieces;
    public int remainingP2Pieces;

    public Board(int Size)
    {
        this.Size = Size;
        this.Array = new int[this.Size][this.Size];
        this.remainingP1Pieces = 2 * Size;
        this.remainingP2Pieces = 2 * Size;
        for (int i = 0; i < Size; i++)
        {
            for (int j = 0; j < Size; j++)
            {
                if (i < 2)
                    this.Array[i][j] = 2;
                else if (i > Size - 3)
                    this.Array[i][j] = 1;
                else
                    this.Array[i][j] = 0;
            }
        }
    }

    public boolean movePiece(int currentRow, int currentColumn, int targetRow, int targetColumn, Player curPlayer)
    {
        // Check if the move is valid according to the rules of Breakthrough
        if (isValidMove(currentRow, currentColumn, targetRow, targetColumn, curPlayer))
        {
            int target = this.Array[targetRow][targetColumn];
            if (target == 2) this.remainingP2Pieces--;
            if (target == 1) this.remainingP1Pieces--;

            // Update the board array to reflect the move
            this.Array[targetRow][targetColumn] = this.Array[currentRow][currentColumn];
            this.Array[currentRow][currentColumn] = 0;

            return true;
        }
        return false;
    }
    public void makeMove(Move move)
    {
        int target = this.Array[move.targetRow][move.targetColumn];
        if (target == 2) this.remainingP2Pieces--;
        if (target == 1) this.remainingP1Pieces--;

        // Update the board array to reflect the move
        this.Array[move.targetRow][move.targetColumn] = this.Array[move.currentRow][move.currentCol];
        this.Array[move.currentRow][move.currentCol] = 0;
    }

    private boolean isValidMove(int currentRow, int currentColumn, int targetRow, int targetColumn, Player curPlayer)
    {
        // Check if the target location is outside the board
        if (targetRow < 0 || targetRow >= this.Size || targetColumn < 0 || targetColumn >= this.Size)
            return false;

        // Check if the target location is occupied
        if (this.Array[targetRow][targetColumn] == curPlayer.Number)
            return false;

        // Check if the piece belongs to the current player
        if (this.Array[currentRow][currentColumn] != curPlayer.Number)
            return false;

        // Check if the piece is moving forward
        if (curPlayer.Number == 2 && targetRow <= currentRow)
            return false;

        if (curPlayer.Number == 1 && targetRow >= currentRow)
            return false;

        // Check if the piece is moving by one space only
        int rowDelta = Math.abs(targetRow - currentRow);
        int columnDelta = Math.abs(targetColumn - currentColumn);
        if (rowDelta > 1 || columnDelta > 1)
            return false;

        // Check if the piece is attacking diagonally
        if (this.Array[targetRow][targetColumn] == curPlayer.Number && columnDelta == 1)
            return false;
        else if (this.Array[targetRow][targetColumn] != 0 && columnDelta == 0)
            return false;

        // If none of the above conditions are met, the move is valid
        return true;
    }

    public boolean winCondition()
    {
        /* Check for a normal win  */
        for (int i = 0; i < this.Size; i++)
        {
            if (Array[0][i] == 1)
            {
                System.out.println("Player 1 wins!");
                return true;
            }
            if (Array[this.Size-1][i] == 2)
            {
                System.out.println("Player 1 wins!");
                return true;
            }
        }

        if (this.remainingP2Pieces == 0)
        {
            System.out.println("Player 1 wins!");
            return true;
        }
        if (this.remainingP1Pieces == 0)
        {
            System.out.println("Player 2 wins!");
            return true;
        }

        return false;
    }

    public void printArray()
    {
        System.out.println();
        for (int i = 0; i < this.Size; i++)
        {
            for (int j = 0; j < this.Size; j++)
                System.out.print(this.Array[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }

}
