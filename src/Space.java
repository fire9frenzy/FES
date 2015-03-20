public class Space
{
	//Type indicates the type of space
	//0 -> wall
	//1 -> obstacle
	//2 -> floor
	//3 -> door
 	private int type = 0;
 	private int dimension = 1;
 	Agent agent = null;
 	//dimension 1x1 meters
 	public Space(int type)
 	{
 		this.type = type;
 	}

 	public void setAgent(Agent agent)
 	{
 		this.agent= agent;
 	}

 	public Agent getAgent()
 	{
 		return agent;
 	}

 	public boolean hasAgent()
 	{
 		if(agent == null)
 		{
 			return false;
 		}
 		return true;
 	}

 	public void setType(int type)
 	{
 		this.type = type;
 	}

 	public int getType()
 	{
 		return type;
 	}

 	public String toString()
 	{
 		if(agent != null)
 		{
 			return agent.getType();
 		}
 		if(type == 0)
 		{
 			return "W";
 		}
 		else if(type == 1)
 		{
 			return "O";
 		}
 		else if(type == 3)
 		{
 			return "D";
 		}

 		return " ";
 	}



}