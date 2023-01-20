/* Breakthrough Game by GibbyB */

package Breakthrough;

public class Solution
{
    public static void main(String[] args)
    {
        // Start a game by passing a board and 2 players!
        new GameFrame(new Board(8),new Player(1,true),new Player(2, false));
    }
}
