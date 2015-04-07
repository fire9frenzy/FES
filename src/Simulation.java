import java.util.*;
public class Simulation
{
	public static void main(String[] args)
	{
		Building lab = new Building();
		lab.addRoom(new Room());
		// lab.addRoom(new Room());
		lab.placeAgents(2);
		lab.initiate();

		do
		{
			System.out.println("-------------------------------------------------------");
			System.out.println(lab);
			lab.update();
			// System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
		while(!lab.isEmpty());
		System.out.println(lab);

	}
}