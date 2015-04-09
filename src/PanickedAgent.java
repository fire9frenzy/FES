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
		int min = room[x][y].getValueAt(doorToGo);
		Location best = position;
		if(!super.ableToMove())
		{
			// System.out.println("Agent could not Move");

			super.move();
			return best;
		}
		// System.out.println("Agent could Move");


		Iterator<Location> iterator = possible.iterator();

		while(iterator.hasNext())
		{
			Location temp = iterator.next();
			if(room[temp.getX()][temp.getY()].getValueAt(doorToGo) <= min)
			{
				if(!room[temp.getX()][temp.getY()].hasAgent())
				{
					min = room[temp.getX()][temp.getY()].getValueAt(doorToGo);
					best = temp;	
				}
			}
		}

		return best;
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