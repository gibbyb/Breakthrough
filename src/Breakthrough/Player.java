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

    public void newHeadNode() {this.headNode = new Node();}

}
