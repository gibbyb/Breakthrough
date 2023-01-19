package Breakthrough;

public class Player
{
    public final int Number;
    public Node headNode;
    public boolean isHuman;

    public Player(int Number, Board board, boolean isHuman)
    {
        this.Number = Number;
        this.headNode = new Node(board);
        this.isHuman = isHuman;
    }

    public void setHeadNode(Board board) {this.headNode = new Node(board);}

}
