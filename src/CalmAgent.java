import java.util.*;

public class CalmAgent extends Agent
{
	public CalmAgent()
	{
		super();
		setPriority(1);
		setType("C");
	}

	public Location getNextMove(Space[][] room)
	{

		Location position = super.getPosition();
		int x = position.getX();
		int y = position.getY();

		ArrayList<Location> possible = getPossibles(x,y,room);
		int min = room[x][y].getValueAt();
		Location best = position;

		// System.out.println(super.ableToMove());
		if(!super.ableToMove())
		{
			// System.out.println("C Agent could not Move");
			super.move();
			return best;
		}
		// System.out.println("C Agent could Move");

		Iterator<Location> iterator = possible.iterator();

		while(iterator.hasNext())
		{
			Location temp = iterator.next();
			if(room[temp.getX()][temp.getY()].getValueAt() <= min)
			{
				if(!room[temp.getX()][temp.getY()].hasAgent())
				{
					min = room[temp.getX()][temp.getY()].getValueAt();
					best = temp;	
				}
			}
			// System.out.println("check");
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