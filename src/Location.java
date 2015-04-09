public class Location
{
	private int x;
	private int y;
	public Location(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}

	public String toString()
	{
		return "("+x+","+y+")";
	}
	public boolean equals(Location other)
	{
		if (x == other.getX() && y == other.getY())
		{
			System.out.println(this + ": " + other);
			return true;
		}
		return false;
	}
}