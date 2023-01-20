package Breakthrough;

public class Move
{
    public Player player;
    public int currentRow, currentCol, targetRow, targetColumn, remainingP1Pieces, remainingP2Pieces;

    public Move(int currentRow, int currentCol, int targetRow, int targetColumn, Board board, Player player)
    {
        this.currentRow = currentRow;
        this.currentCol = currentCol;
        this.targetRow = targetRow;
        this.targetColumn = targetColumn;
        this.player = player;
        this.remainingP1Pieces = board.remainingP1Pieces;
        this.remainingP2Pieces = board.remainingP2Pieces;
        if (board.Array[targetRow][targetColumn] == 1)
            this.remainingP1Pieces--;
        else if (board.Array[targetRow][targetColumn] == 2)
            this.remainingP2Pieces--;
    }

}
