package Breakthrough;

public class Main
{
    private static final int SIZE = 8;
    public static void main(String[] args)
    {
        // Start a game
        new GameFrame(new Board(SIZE));
    }
}
