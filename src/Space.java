public class Space
{
	//Type indicates the type of space
	//0 -> wall
	//1 -> obstacle
	//2 -> floor
	//3 -> door
 	private int type = 0;

 	public Space(int type)
 	{
 		this.type = type;
 	}

 	public void setType(int type)
 	{
 		this.type = type;
 	}

 	public int getType()
 	{
 		return type;
 	}

}