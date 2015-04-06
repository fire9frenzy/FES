public class CalmAgent extends Agent
{
	public CalmAgent()
	{
		super();
		setPriority(1);
		setType("C");
	}

	public Location getNextMove(int doors,Space[][] room)
	{

		Location position = super.getPosition();
		int doorToGo = 0;
		int valueOfDoor = room[position.getX()][position.getY()].getValueAt(doorToGo);

		for(int i = 1; i < doors; i++)
		{
			if(valueOfDoor >= room[position.getX()][position.getY()].getValueAt(i))
			{
				valueOfDoor = room[position.getX()][position.getY()].getValueAt(i);
				doorToGo = i;
			}
		}

		int x = position.getX();
		int y = position.getY();

		System.out.println("location= " +position + " door=" + doorToGo + " with value = " +valueOfDoor);
		return null;
	}
}