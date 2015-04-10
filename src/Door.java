public class Door
{
	private int id = 0;
	private Building build = null;
	public Door(int doorID, Building b)
	{
		id = doorID;
		build = b;
	}
	public boolean leave(Agent a)
	{
		return build.moveAgent(a, id);
	}
	public int getID()
	{
		return id;
	}
	public int getOtherValue()
	{
		return build.getDoorValue(id);
	}
}