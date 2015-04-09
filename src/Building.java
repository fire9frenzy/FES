import java.util.*;
// import java.util.Scanner;
import java.io.*;
public class Building
{
	private ArrayList<Room> rooms = new ArrayList<Room>();
	private ArrayList<int[]> doorPairs = new ArrayList<int[]>();
	private int doorCount = 0;
	private int outsideDoors = 0;
	public Building(File file)
	{
		// fakeParse();
		fileParse(file);
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
					int pairIndex = 1;
					if(doorPairs.get(j)[1] == -1)
					{
						pairIndex = 0;
					}
					for(int k = 0; k < rooms.size(); ++k)
					{
						// System.out.println(outsideDoors);
						// System.out.println(doorPairs.get(j)[pairIndex]);
						if(rooms.get(k).hasDoor(doorPairs.get(j)[pairIndex]))
						{
							// System.out.println("inside");
							initiate(rooms.get(k),doorPairs.get(j), i);
							k = rooms.size();
						}
					}

					lastPair = j+1;
					j = doorPairs.size();
				}
			}
		}

		startFire();
		// for(int i = 0; i < rooms.size(); i++)
		// {
		// 	rooms.get(i).initiateRoom();
		// }
	}

	private void startFire()
	{
		Random random = new Random();
		int index = random.nextInt(rooms.size());
		rooms.get(index).produceFire();
	}

	private void initiate(Room room, int[] doorPair ,int index)
	{
		room.initiateRoom(doorPair, index);
		// System.out.println(doorPair[1] + "hello");
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
			// System.out.println("updating agents in " + rooms.get(i).name);
			rooms.get(i).updateRoom();
			// System.out.println("Checking doors in " + rooms.get(i).name);
			rooms.get(i).checkAllDoors();
		}
		System.out.println(this);
		// for (int i = 0; i < rooms.size(); i++)
		// {
		// }
	}

	public void addRoom(Room r)
	{
		rooms.add(r);
	}
	public void pairDoors(int f, int s)
	{
		doorPairs.add(new int[] {f, s});
	}
	public void placeAgents(int total, double percentage)
	{
		for (int i = 0; i < total; ++i)
		{
			// pick a type
			String type = "C";
			Random random = new Random();
			// System.out.println(percentage);
			if (random.nextInt(100) > percentage)
			{
				// System.out.println("in");
				type = "P";
			}
			// pick a room
			int r = (int)Math.floor(Math.random() * rooms.size());
			rooms.get(r).setAgent(type);
		}
	}
	public boolean moveAgent(Agent a, int doorID)
	{
		// find the matching door
		// System.out.println("Use a door");
		for (int i = 0; i < doorPairs.size(); ++i)
		{
			if(doorPairs.get(i)[0] == doorID)
			{
				// System.out.println("0 to 1");
				// check for open space
				for(int j = 0; j < rooms.size(); ++j)
				{
					// check for outside
					if (doorPairs.get(i)[1] == -1)
					{
						// System.out.println("hello is it me your looking for");
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
				// System.out.println("1 to 0");
				// check for open space
				for (int j = 0; j < rooms.size(); ++j)
				{
					// check for outside
					if (doorPairs.get(i)[0] == -1)
					{
						// hurray they made it out!!!
						// System.out.println("hello is it me your looking for");
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
		r.name = "big room";
		r.addDoor(9,19, new Door(++doorCount,this));
		pairDoors(doorCount,-1);
		outsideDoors++;
		// r.addAgent(new Location(5,5), "C");

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
		s.name = "small Room";

		// for(int i = 0; i < doorPairs.size(); ++i)
		// {
		// 	System.out.println("Pair");
		// 	System.out.println(doorPairs.get(i)[0]);
		// 	System.out.println(doorPairs.get(i)[1]);
		// }
	}
	public void fileParse(File file)
	{
		try
		{
			Scanner sc = new Scanner(file);
			// find first <room>
			while(sc.hasNext())
			{
				String line = sc.nextLine();
				// System.out.println(line);
				if (line.equals("<room>"))
				{
					parseRoom(sc);
					// Room temp = new Room();
					// temp.setSpace(parseRoom(sc);
					// rooms.add(temp);
					// System.out.println(temp);
					// temp.addAgent(new Location(2,2), "C");
				}
			}
			sc = new Scanner(file);
			addDoors(sc);
		}
		catch(IOException e)
		{
			System.out.println("File not Found");
		}
	}
	private ArrayList<int[]> doorSetup = new ArrayList<int[]>();
	private int searchDoorSetup(int pairNum)
	{
		for (int i = 0; i < doorSetup.size(); ++i)
		{
			if (doorSetup.get(i)[0] == pairNum)
			{
				return i;
			}
		}
		return -1;
	}
	private void addDoors(Scanner sc)
	{
		// create pairs
		for (int i = 0; i < doorSetup.size(); ++i)
		{
			// for (int j = 0; j < doorSetup.get(i).length; ++j)
			// {
			// 	System.out.print(doorSetup.get(i)[j] + ", ");
			// }
			System.out.println();
			pairDoors(doorSetup.get(i)[1], doorSetup.get(i)[2]);
			if (doorSetup.get(i)[0] == -1)
			{
				// System.out.println("outside Door");
				++outsideDoors;
			}
		}
	}
	private void parseRoom(Scanner sc)
	{
		// System.out.println("parseRoom");
		boolean foundEnd = false;
		ArrayList<Space[]> combiner = new ArrayList<Space[]>();
		ArrayList<Door> doors = new ArrayList<Door>();
		ArrayList<Location> doorPos = new ArrayList<Location>();

		while(sc.hasNext())
		{
			String line = sc.nextLine();
			if (line.equals("</room>"))
			{
				foundEnd = true;
				break;
			}
			// System.out.println("Parsing " + line);
			// get length of line
			String[] parts = line.split(" ");
			combiner.add(new Space[parts.length]);
			int index = combiner.size() - 1;
			// combiner.get(index);
			//0 -> wall
			//1 -> obstacle
			//2 -> floor
			//3 -> door
			for (int i = 0; i < parts.length; ++i)
			{
				// System.out.println(parts[i]);
				// check if door
				if (parts[i].indexOf("d") > -1)
				{
					// System.out.println("Create a door");
					combiner.get(index)[i] = new Space(3);
					++doorCount;

					if(parts[i].equals("d"))
					{
						doorSetup.add(new int[] {-1, doorCount, -1});
						doors.add(new Door(doorCount, this));
						doorPos.add(new Location(index, i));
					}
					else
					{
						int pairNum = Integer.valueOf(parts[i].substring(1, parts[i].length()));
						if (pairNum < 0)
						{
							System.out.println("Error doors cannont have negative indexs from files");
						}
						// System.out.println(pairNum);
						// search the doorsetupList
						int searResult = searchDoorSetup(pairNum);
						if (searResult >= 0)
						{
							// found match the indexes
							doorSetup.get(searResult)[2] = doorCount;
						}
						else
						{
							// not found add to the list
							doorSetup.add(new int[] {pairNum, doorCount, -1});
						}
						doors.add(new Door(doorCount, this));
						doorPos.add(new Location(index, i));
					}
				}
				// create space
				else if (parts[i].equals("0"))
				{
					// System.out.println("Create a wall");
					combiner.get(index)[i] = new Space(0);
				}
				else if (parts[i].equals("1"))
				{
					// System.out.println("Create a floor");
					combiner.get(index)[i] = new Space(2);
				}
				else if (parts[i].equals("2"))
				{
					// System.out.println("Create an obstacle");
					combiner.get(index)[i] = new Space(1);
				}
				else
				{
					System.out.println("Unknown piece killing all the program");
				}
			}
		}
		// System.out.println("Parsing over");
		// convert to arrays
		Space[][] out = new Space[combiner.size()][];
		for(int i = 0; i < combiner.size(); ++i)
		{
			out[i] = new Space[combiner.get(i).length];
			for(int j = 0; j < combiner.get(i).length; ++j)
			{
				out[i][j] = combiner.get(i)[j];
			}
		}
		Room temp = new Room();
		temp.setSpace(out);

		// add doors
		for (int i = 0; i < doors.size(); ++i)
		{
			temp.addDoor(doorPos.get(i).getX(), doorPos.get(i).getY(), doors.get(i));
		}
		// System.out.println("All the doors");
		// temp.printDoors();
		rooms.add(temp);
	}
}