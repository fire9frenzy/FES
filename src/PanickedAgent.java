import java.util.*;
public class PanickedAgent extends Agent 
{
	public PanickedAgent()
	{
		super();
		setPriority(5);
		setType("P");
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
		
		for(int i = -1; i < 2; i++)
		{
			for(int j = -1; j < 2; j++)
			{
				if((((x+i) >= 0) && ((x+i) < room.length)))
				{
					if((((y+j) >= 0) && ((y+j) < room[x+i].length)))
					{
						if(room[x+i][y+j].getType() != room[x][y].getWall())
						{
							possibles.add(new Location(x+i,y+j));	
						}
					}
				}				
			}
		}

		if((((x-2) >= 0) && ((x-2) < room.length)) && room[x-2][y].getType() != room[x][y].getWall())
		{
			possibles.add(new Location(x-2,y));
		}
		if((((x+2) >= 0) && ((x+2) < room.length)) && room[x+2][y].getType() != room[x][y].getWall())
		{
			possibles.add(new Location(x+2,y));
		}
		if((((y-2) >= 0) && ((y-2) < room[x].length)) && room[x][y-2].getType() != room[x][y].getWall())
		{
			possibles.add(new Location(x,y-2));
		}
		if((((y+2) >= 0) && ((y+2) < room[x].length)) && room[x][y+2].getType() != room[x][y].getWall())
		{
			possibles.add(new Location(x,y+2));
		}

		return possibles;
	}
}