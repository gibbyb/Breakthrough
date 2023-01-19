package Breakthrough;

public class Solution
{
    public static void main(String[] args)
    {
        Board board = new Board(8);
        Player player1 = new Player(1,board,true);
        Player player2 = new Player(2,board, false);
        new GameFrame(board,player1,player2);
    }
}
