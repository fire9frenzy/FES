public class Agent
{
	private Location target = null;
	private Location position = null;
	private String type = "C";
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
	public Location move()
	{
		return null;
	}
	public void updatePosition(Location p)
	{
		position = p;
	}
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
}