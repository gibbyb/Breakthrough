package Breakthrough;

public class Player
{
    public final int Number;
    public Node headNode;
    public boolean isHuman;

    public Player(int Number, boolean isHuman)
    {
        this.Number = Number;
        this.headNode = new Node();
        this.isHuman = isHuman;
    }

    public void checkMoves(Player player, Board board)
    {
        this.headNode = new Node();
        this.headNode.checkMoves(player, board);
    }

}
