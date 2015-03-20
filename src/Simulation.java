import java.util.*;
public class Simulation
{
	public static void main(String[] args)
	{
		Building lab = new Building();
		lab.addRoom(new Room());
		lab.addRoom(new Room());
		lab.placeAgents(5);
		// Room testRoom = new Room();
		System.out.println(lab);
	}
}