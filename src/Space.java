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
 	private int[] doorValues;
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
 		doorValues = new int[sizeValues];
 		for(int i = 0; i < doorValues.length; i++)
 		{
 			doorValues[i] = -1;
 		}
 	}

 	public void setDoorValueAt(int value, int index)
 	{
 		doorValues[index] = value;
 	}

 	public int getDoorValueAt(int index)
 	{
 		return doorValues[index];
 	}


 	public boolean isDoorValueSet(int index)
 	{
 		return !(doorValues[index]==-1);
 	}

 	public int getDoorLayerAmount()
 	{
 		return doorValues.length;
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