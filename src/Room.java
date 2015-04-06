import java.util.*;

public class Room
{
	private int wall = 0;
	private int obstacle = 1;
	private int floor = 2;
	private int door = 3;
	private int agentsPlaced = 0;
	ArrayList<Agent> queue = new ArrayList<Agent>();
	private ArrayList<Location> doorLocation = new ArrayList<Location>();
	private int totalFloors = 0;
	//    0 1 2 3
	//    _ _ _ _
	// 0 | | | | |  x = (1,2)
	// 1 | | | | |
	// 2 | |x| | |
	// 3 | | | | |
	private Space[][] space;
	public Room()
	{	
		test();
	}

	public void initiateRoom()
	{
		ArrayList<Integer> priorities = new ArrayList<Integer>();

		for(int i = 0; i < space.length; i++)
		{
			for(int j = 0; j < space[i].length; j++)
			{
				if(space[i][j].hasAgent())
				{
					int temp = space[i][j].getAgent().getPriority();
					if(!priorities.contains(temp))
					{
						priorities.add(temp);
					}
				}
			}
		}

		while(!priorities.isEmpty())
		{
			int highest = getHighest(priorities);
			for(int i = 0; i < space.length; i++)
			{
				for(int j = 0; j < space[i].length; j++)
				{
					if(space[i][j].hasAgent())
					{
						if(space[i][j].getAgent().getPriority() == highest)
						{
							queue.add(space[i][j].getAgent());
						}
					}
				}
			}	
		}

		setValues();

	}

	private int getHighest(ArrayList<Integer> in)
	{
		Iterator<Integer> iterator = in.iterator();
		int temp = 0;
		while(iterator.hasNext())
		{
			int tempNext = iterator.next();
			if(tempNext >= temp)
			{
				temp = tempNext;
			}
		}

		in.remove((Integer)(temp));
		return temp;
	}

	public void updateRoom()
	{
		for(int i = 0; i < space.length; i++)
		{
			for(int j = 0; j < space[i].length; j++)
			{
				if(space[i][j].hasAgent())
				{
					space[i][j].getAgent().getNextMove(doorLocation.size(),space);
				}
			}
		}
	}

	private void test()
	{
		space = new Space[20][20];

		for(int i = 0; i < space.length; i++)
		{
			space[0][i] = new Space(wall);
			space[space.length - 1][i] = new Space(wall);
		}
		for(int i = 0; i < space.length; i++)
		{
			space[i][space.length - 1] = new Space(wall);
		}

		for(int i = 0; i < space.length; i++)
		{
			if(i == 5)
			{
				space[i][0] = new Space(door);
				doorLocation.add(new Location(i,0));
			}
			if(i == 5)
			{
				space[i][19] = new Space(door);
				doorLocation.add(new Location(i,19));
			}
			else
			{
				space[i][0] = new Space(wall);
			}
		}

		for(int i = 1; i < space.length -1; i++)
		{
			for(int j = 1; j < space[i].length -1; j++)
			{
				totalFloors++;
				space[i][j] = new Space(floor);
			}
		}
	}

	public void setAgent(String agentType)
	{
		agentsPlaced++;
		Random random = new Random();
		ArrayList<Location> available = new ArrayList<Location>();

		for(int i = 0; i < space.length; i++)
		{
			for(int j = 0; j < space[i].length; j++)
			{
				if((space[i][j].getType() == 1 || space[i][j].getType() == 2) && (!space[i][j].hasAgent()))
				{
					available.add(new Location(i,j));
				}
			}
		}

		int index = random.nextInt(available.size());
		Location location = available.get(index);
		Agent temp = createAgent(agentType);
		temp.updatePosition(location);
		space[available.get(index).getX()][available.get(index).getY()].setAgent(temp);
	}

	private Agent createAgent(String agentType)
	{
		if(agentType.compareToIgnoreCase("C") == 0)
		{
			return new CalmAgent();
		}
		else
		{
			return new PanickedAgent();	
		}
	}

	public boolean full()
	{
		return (agentsPlaced == totalFloors);
	}

	public String toString()
	{
		String test = "";
		for(int i = 0; i < space.length; i++)
		{
			for(int j = 0;  j < space.length; j++)
			{
				test = test + space[i][j];
			}
			test = test +"\n";
		}
		return test;
	}

	//private Function setValues()
	//input: non
	//output: non
	//description: it will set the values for all the spaces within the room
	//the door will have a value of 0 (zero) if it leads to the outside, else will have the value of the door 
	//connecting to the room +1. the values will increase by 1 for all adjecent spaces (not including walls)
	//and so on
	//obstacles will have an increase value of +2
	//eg:
	//WWWWWWWWWWW
	//W|4|4|4|4|W
	//W|3|3|3|4|W
	//W|2|2|3|4|W
	//W|1|2|3|4|W
	//0|1|2|3|4|W
	//W|1|2|3|4|W
	//W|2|2|3|4|W
	//W|3|3|3|4|W
	//W|4|4|4|4|W
	//WWWWWWWWWWW

	private void setValues()
	{
		for(int i = 0; i < space.length; i++)
		{
			for(int j = 0; j < space[i].length; j++)
			{
				space[i][j].setPossibleDoors(doorLocation.size());
			}
		}

		for(int i = 0; i < doorLocation.size(); i++)
		{
			Location tempLoc = doorLocation.get(i);
			space[tempLoc.getX()][tempLoc.getY()].setValueAt(0,i);
			ArrayList<Location> temp = new ArrayList<Location>();
			temp.add(tempLoc);
			setValuesAdjecent(space,temp,i);
		}
	}

	private void setValuesAdjecent(Space[][] input,ArrayList<Location> location, int index)
	{
		if(location.isEmpty())
		{
			return;
		}

		ArrayList<Location> adjecent = new ArrayList<Location>();

		Iterator<Location> iterator = location.iterator();
		while(iterator.hasNext())
		{
			Location temp = iterator.next();
			int x = temp.getX();
			int y = temp.getY();
			int value = input[x][y].getValueAt(index);
			for(int i = -1; i < 2; i++)
			{
				for(int j = -1; j < 2; j++)
				{
					if(i == 0 && j == 0)
					{
						continue;
					}
					if(validLocation(x+i,y+j))
					{
						if(setValue(input,new Location(x+i,y+j),value,index))
						{
							adjecent.add(new Location(x+i,y+j));
						}
					}
				}
			}
		}


		setValuesAdjecent(input,adjecent,index);
				
	}

	private boolean validLocation(int x, int y)
	{
		return ((x >= 0 && x < space.length) && (y >= 0 && y < space[x].length));
	}

	private boolean setValue(Space[][] input, Location location, int value, int index)
	{
		if(input[location.getX()][location.getY()].isValueSet(index) || !validLocation(location.getX(),location.getY()) || input[location.getX()][location.getY()].getType() == wall)
		{
			return false;
		}
		else if(input[location.getX()][location.getY()].getType() == obstacle)
		{
			input[location.getX()][location.getY()].setValueAt(value+2,index);	
			return true;
		}
		else
		{
			input[location.getX()][location.getY()].setValueAt(value+1,index);	
			return true;
		}
		// return false;
	}
	public void printValues()
	{	
		// System.out.println(doorLocation.get(0));
		// System.out.println(doorLocation.get(1));
		for(int k = 0; k < doorLocation.size(); k++)
		{
			for(int i = 0; i < space.length; i++)
			{
				for(int j = 0;  j < space.length; j++)
				{
					if(space[i][j].getValueAt(k) == -1)
					{
						System.out.print("x ");
					}
					else
					{
						System.out.print(space[i][j].getValueAt(k) +" ");
					}
				}
				System.out.println("\n");
			}
		}
	}

}