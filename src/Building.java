import java.util.ArrayList;
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
		}
		return out;
	}
}