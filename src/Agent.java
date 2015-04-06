// import java.util.ArrayList;
public class Agent
{
	private Location target = null;
	private Location position = null;
	private String type = "C";
	private int priority = 1;
	public Agent()
	{

	}
	public void update()
	{

	}
	// when an agent first enters a room a target is set for the agent to path to
	// this target is the location of the next door it needs to use
	public void setTarget(Location t)
	{
		target = t;
	}
	public void setType(String t)
	{
		type = t;
	}
	public void setPriority(int i)
	{
		priority = i;
	}
	// this is not final
	public Location getNextMove(int doors,Space[][] room)
	{
		return null;
	}
	// if the room moves the agent update its Position
	public void updatePosition(Location p)
	{
		position = p;
	}
	// this is where pathfinding goes. It will probably be really hard
	public void ceatePath()
	{

	}
	public String toString()
	{
		return "Agent: \n target: " + target + "\n position: " + position;
	}
	public String getType()
	{
		return type;
	}
	public int getPriority()
	{
		return priority;
	}

	public Location getPosition()
	{
		return position;
	}
}