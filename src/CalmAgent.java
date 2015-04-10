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
		int x = position.getX();
		int y = position.getY();
		Location door = getBestDoor(position,doors,room);

		ArrayList<Location> possible = getPossibles(x,y,room);

		Location best = position;
		if(!super.ableToMove())
		{
			super.move();
			return best;
		}

		Iterator<Location> iterator = possible.iterator();

		while(iterator.hasNext())
		{
			Location temp = iterator.next();
			if(!room[temp.getX()][temp.getY()].hasAgent())
			{
				// System.out.println("check");
				// System.out.println("C " + door + " " + temp + " " + best);
				if(distance(temp,door) <= distance(best,door))
				{
					best = temp;	
				}
			}
		}

		return best;
	}

	private Location getBestDoor(Location position,int doors,Space[][] room)
	{
		int min = Integer.MAX_VALUE;
		ArrayList<Location> temp = new ArrayList<Location>();
		for(int i = 0; i < room.length; ++i)
		{
			for(int j = 0;  j < room[i].length; ++j)
			{
				if(room[i][j].getType() == room[i][j].getDoor())
				{
					for(int k = 0; k < doors; ++k)
					{
						if(room[i][j].getDoorValueAt(k) == min)
						{
							min = room[i][j].getDoorValueAt(k);
							temp.add(new Location(i,j));
						}
						else if(room[i][j].getDoorValueAt(k) < min)
						{
							min = room[i][j].getDoorValueAt(k);
							temp = new ArrayList<Location>();
							temp.add(new Location(i,j));
						}
					}
				}
			}
		}

		int index = 0;
		for(int i = 0; i < temp.size(); i++)
		{
			if(distance(position,temp.get(i)) <= distance(position,temp.get(index)))
			{
				index = i;
			}
		}

		return temp.get(index);
	}

	private double distance(Location from, Location to)
	{
		return Math.sqrt((Math.pow((from.getX()-to.getX()),2) + Math.pow((from.getY()-to.getY()),2)) );
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