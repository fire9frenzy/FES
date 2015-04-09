import java.util.*;
public class Building
{
	private ArrayList<Room> rooms = new ArrayList<Room>();
	private ArrayList<int[]> doorPairs = new ArrayList<int[]>();
	private int doorCount = 0;
	private int outsideDoors = 0;
	public Building()
	{
		fakeParse();
	}

	public void initiate()
	{

		for(int i = 0; i < rooms.size(); i++)
		{
			rooms.get(i).setTotalLayers(outsideDoors);
		}	

		// for(int i = 0; i < doorPairs.size(); i++)
		// {
		// 	if((doorPairs.get(i)[0] == -1) || (doorPairs.get(i)[1] == -1))
		// 	{
		// 		System.out.println("worked");
		// 	}
		// }

		int lastPair = 0;
		for(int i = 0; i < outsideDoors; ++i)
		{
			// System.out.println(outsideDoors);
			// System.out.println(lastPair);
			for(int j = lastPair; j < doorPairs.size(); j++)
			{

				// System.out.println(doorPairs.size());
				// System.out.println(doorPairs.get(j)[0] == -1);
				// System.out.println(doorPairs.get(j)[1] == -1);
				if((doorPairs.get(j)[0] == -1) || (doorPairs.get(j)[1] == -1))
				{
					// System.out.println("inside");
					int pairIndex = 1;
					if(doorPairs.get(j)[1] == -1)
					{
						pairIndex = 0;
					}
					for(int k = 0; k < rooms.size(); ++k)
					{
						// System.out.println(doorPairs.get(j)[pairIndex]);
						if(rooms.get(k).hasDoor(doorPairs.get(j)[pairIndex]))
						{
							initiate(rooms.get(k),doorPairs.get(j), i);
							k = rooms.size();
						}
					}

					lastPair = j+1;
					j = doorPairs.size();
				}
			}
		}

		// for(int i = 0; i < rooms.size(); i++)
		// {
		// 	rooms.get(i).initiateRoom();
		// }
	}

	private void initiate(Room room, int[] doorPair ,int index)
	{
		room.initiateRoom(doorPair, index);
		// System.out.println(doorPair[1]);
		for(int i = 0; i < room.doorAmount(); i++)
		{
			int tempID = room.getDoorID(i);
			if(tempID == doorPair[0] || tempID == doorPair[1])
			{
				// System.out.println(tempID);
				continue;
			}
			int[] pair = getDoorPair(tempID);
			if(pair[0] == -1 || pair[1] == -1)
			{
				continue;
			}
			int other = pair[0];
			if(pair[0] ==  tempID)
			{
				other = pair[1];
			}

			Room next = findRoomWithID(other);
			initiate(next,pair,index);
		}
	}

	private Room findRoomWithID(int id)
	{
		int temp = 0;
		for(int i = 0; i < rooms.size(); ++i)
		{
			if(rooms.get(i).hasDoor(id))
			{
				temp = i;
				i = rooms.size();
			}
		}
		return rooms.get(temp);
	}

	private int[] getDoorPair(int id)
	{
		for(int i = 0; i < doorPairs.size(); ++i)
		{
			if(doorPairs.get(i)[0] == id || doorPairs.get(i)[1] == id)
			{
				return doorPairs.get(i);
			}
		}
		int[] temp = {-1,-1};
		return temp;
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
			if(doorPairs.get(i)[0] == doorID)
			{
				System.out.println("0 to 1");
				// check for open space
				for(int j = 0; j < rooms.size(); ++j)
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
			// out += rooms.get(i) + "\n";
			// System.out.println("------------------------------------------------------------------");
			rooms.get(i).printValues();
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
	// build.getDoorValue(id, layerIndex);
	public int getDoorValue(int doorID, int layerIndex)
	{
		// find door pair
		for (int i = 0; i < doorPairs.size(); ++i)
		{
			if (doorPairs.get(i)[0] == doorID)
			{
				for (int j = 0; j < rooms.size(); ++j)
				{
					if (rooms.get(j).hasDoor(doorPairs.get(i)[1]))
					{
						return rooms.get(j).getDoorValueAt(doorPairs.get(i)[1], layerIndex);
					}
				}
			}
			else if (doorPairs.get(i)[1] == doorID)
			{
				for (int j = 0; j < rooms.size(); ++j)
				{
					if (rooms.get(j).hasDoor(doorPairs.get(i)[0]))
					{
						return rooms.get(j).getDoorValueAt(doorPairs.get(i)[0], layerIndex);
					}
				}
			}
		}
		return -1;
		// find other door
		// find room
		// get value
	}
	public void fakeParse()
	{
		Room r = new Room();
		r.setSize(20,20);
		r.addDoor(9,19, new Door(++doorCount,this));
		pairDoors(doorCount,-1);
		outsideDoors++;
		r.addDoor(9, 0, new Door(++doorCount, this));
		rooms.add(r);
		Room s = new Room();
		s.setSize(5,5);
		s.addDoor(2,4, new Door(++doorCount, this));
		
		pairDoors(doorCount, doorCount - 1);

		s.addDoor(2,0, new Door(++doorCount, this));
		pairDoors(doorCount, -1);
		outsideDoors++;
		rooms.add(s);

		// for(int i = 0; i < doorPairs.size(); ++i)
		// {
		// 	System.out.println("Pair");
		// 	System.out.println(doorPairs.get(i)[0]);
		// 	System.out.println(doorPairs.get(i)[1]);
		// }
	}
}