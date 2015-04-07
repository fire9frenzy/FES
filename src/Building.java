import java.util.*;
public class Building
{
	public ArrayList<Room> rooms = new ArrayList<Room>();
	public Building()
	{

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
}