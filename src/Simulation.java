import java.util.*;
public class Simulation
{
	int doorCount = 0;
	public static void main(String[] args)
	{
		Building lab = new Building();
		// lab.addRoom(new Room());
		// lab.addRoom(new Room());
		// lab.addRoom(new Room());
		// lab.placeAgents(1);
		lab.initiate();
		
		// System.out.println(lab);
		do
		{
			lab.update();
			// System.out.println(lab);
			// System.out.println("-------------------------------------------------------");
			// System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
		while(!lab.isEmpty());
	}
}