import java.util.*;
public class Building
{
	private ArrayList<Room> rooms = new ArrayList<Room>();
	private ArrayList<int[]> doorPairs = new ArrayList<int[]>();
	private int doorCount = 0;
	public Building()
	{
		fakeParse();
	}

	public void initiate()
	{
		for(int i = 0; i < rooms.size(); i++)
		{
			rooms.get(i).initiateRoom();
		}
	}

	public void update()
	{
		for(int i = 0; i < rooms.size(); i++)
		{
			rooms.get(i).updateRoom();
		}		
	}

	public void addRoom(Room r)
	{
		rooms.add(r);
	}
	public void pairDoors(int f, int s)
	{
		doorPairs.add(new int[] {f, s});
	}
	public void placeAgents(int total)
	{
		for (int i = 0; i < total; ++i)
		{
			// pick a type
			String type = "C";
			if (Math.random() > 0.5)
				type = "P";
			// pick a room
			int r = (int)Math.floor(Math.random() * rooms.size());
			rooms.get(r).setAgent(type);
		}
	}
	public boolean moveAgent(Agent a, int doorID)
	{
		// find the matching door
		for (int i = 0; i < doorPairs.size(); ++i)
		{
			if (doorPairs.get(i)[0] == doorID)
			{
				System.out.println("0 to 1");
				// check for open space
				for (int j = 0; j < rooms.size(); ++j)
				{
					// check for outside
					if (doorPairs.get(i)[1] == -1)
					{
						// hurray they made it out!!!
						return true;
					}
					//find the room
					if(rooms.get(j).hasDoor(doorPairs.get(i)[1]))
					{
						// ask if there is space
						return rooms.get(j).placeByDoor(a, doorPairs.get(i)[1]);
						// yes place and return true
					}
				}
			}
			else if (doorPairs.get(i)[1] == doorID)
			{
				System.out.println("1 to 0");
				// check for open space
				for (int j = 0; j < rooms.size(); ++j)
				{
					// check for outside
					if (doorPairs.get(i)[0] == -1)
					{
						// hurray they made it out!!!
						return true;
					}
					//find the room
					if(rooms.get(j).hasDoor(doorPairs.get(i)[0]))
					{
						// ask if there is space
						return rooms.get(j).placeByDoor(a, doorPairs.get(i)[0]);
						// yes place and return true
					}
				}
			}
		}
		return false;
	}
	public String toString()
	{
		String out = "";
		for (int i = 0; i < rooms.size(); ++i)
		{
			out += rooms.get(i) + "\n";
			// System.out.println("------------------------------------------------------------------");
			// rooms.get(i).printValues();
		}
		return out;
	}

	public boolean isEmpty()
	{
		Iterator<Room> iterator = rooms.iterator();
		// System.out.println(rooms.size());
		boolean empty = true;
		while(iterator.hasNext())
		{
			Room temp = iterator.next();
			empty = (empty && temp.isEmpty());
			// System.out.println(iterator.hasNext());
		}
		// System.out.println(empty);
		return empty;
	}
	public void fakeParse()
	{
		Room r = new Room();
		r.setSize(20,20);
		r.addDoor(9, 0, new Door(++doorCount, this));
		rooms.add(r);
		Room s = new Room();
		s.setSize(5,5);
		s.addDoor(2,4, new Door(++doorCount, this));
		
		pairDoors(doorCount, doorCount - 1);

		s.addDoor(2,0, new Door(++doorCount, this));
		pairDoors(doorCount, -1);
		rooms.add(s);
	}
}