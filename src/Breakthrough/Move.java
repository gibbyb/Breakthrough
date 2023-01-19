package Breakthrough;

public class Move
{
    public int currentRow, currentCol, targetRow, targetColumn, playerNumber, remainingP1Pieces, remainingP2Pieces;

    public Move(int currentRow, int currentCol, int targetRow, int targetColumn, Board board)
    {
        this.currentRow = currentRow;
        this.currentCol = currentCol;
        this.targetRow = targetRow;
        this.targetColumn = targetColumn;
        this.playerNumber = board.Array[currentRow][currentCol];
        this.remainingP1Pieces = board.remainingP1Pieces;
        this.remainingP2Pieces = board.remainingP2Pieces;
        if (board.Array[targetRow][targetColumn] == 1)
            this.remainingP1Pieces--;
        else if (board.Array[targetRow][targetColumn] == 2)
            this.remainingP2Pieces--;
    }

}
