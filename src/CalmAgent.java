import java.util.*;

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

		ArrayList<Location> possible = getPossibles(x,y,room);
		int min = room[possible.get(0).getX()][possible.get(0).getY()].getValueAt(doorToGo);
		Location best = possible.get(0);

		Iterator<Location> iterator = possible.iterator();

		while(iterator.hasNext())
		{
			Location temp = iterator.next();
			if(room[temp.getX()][temp.getY()].getValueAt(doorToGo) <= min)
			{
				min = room[temp.getX()][temp.getY()].getValueAt(doorToGo);
				best = temp;	
			}
		}

		return best;
	}

	private ArrayList<Location> getPossibles(int x, int y, Space[][] room)
	{
		ArrayList<Location> possibles = new ArrayList<Location>();
		if((((x-1) >= 0) && ((x-1) < room.length)) && room[x-1][y].getType() != room[x][y].getWall())
		{
			possibles.add(new Location(x-1,y));
		}
		if((((x+1) >= 0) && ((x+1) < room.length)) && room[x+1][y].getType() != room[x][y].getWall())
		{
			possibles.add(new Location(x+1,y));
		}
		if((((y-1) >= 0) && ((y-1) < room[x].length)) && room[x][y-1].getType() != room[x][y].getWall())
		{
			possibles.add(new Location(x,y-1));
		}
		if((((y+1) >= 0) && ((y+1) < room[x].length)) && room[x][y+1].getType() != room[x][y].getWall())
		{
			possibles.add(new Location(x,y+1));
		}

		return possibles;
	}
}