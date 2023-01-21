package Breakthrough;

public class Player
{
    public final int Number;
    public Node headNode;
    public boolean isHuman;

    public Player(int Number, boolean isHuman)
    {
        this.Number = Number;
        this.headNode = new Node(this);
        this.isHuman = isHuman;
    }

    public void checkMoves(Board board)
    {
        this.headNode = new Node(this);
        this.headNode.checkMoves(board);
    }

}
