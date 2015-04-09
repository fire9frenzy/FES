import java.util.*;

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
 	private int[] values;
 	private boolean onFire = false;
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
 		if(onFire)
 		{
 			return "F";
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

 	public void setPossibleDoors(int sizeValues)
 	{
 		values = new int[sizeValues];
 		for(int i = 0; i < values.length; i++)
 		{
 			values[i] = -1;
 		}
 	}

 	public void setValueAt(int value, int index)
 	{
 		values[index] = value;
 	}

 	public int getValueAt(int index)
 	{
 		return values[index];
 	}


 	public boolean isValueSet(int index)
 	{
 		return !(values[index]==-1);
 	}

 	public int getLayerAmount()
 	{
 		return values.length;
 	}

 	public int getWall()
 	{
 		return 0;
 	}

 	public int getObstacle()
 	{
 		return 1;
 	}

	public int getFloor()
 	{
 		return 2;
 	}

 	public void setOnFire()
 	{
 		onFire = true;
 	}

	public int getDoor()
 	{
 		return 3;
 	}
}