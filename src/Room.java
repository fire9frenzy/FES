import java.util.*;

public class Room
{
	public String name = "Room";
	private int wall = 0;
	private int obstacle = 1;
	private int floor = 2;
	private int door = 3;
	private int agentsPlaced = 0;
	private ArrayList<Agent> queue = new ArrayList<Agent>();
	private ArrayList<Location> doorLocation = new ArrayList<Location>();
	private ArrayList<Door> doors = new ArrayList<Door>();
	private int totalFloors = 0;
	private boolean leadOutside = false;
	//    0 1 2 3
	//    _ _ _ _
	// 0 | | | | |  x = (1,2)
	// 1 | | | | |
	// 2 | |x| | |
	// 3 | | | | |
	private Space[][] space;
	public Room()
	{
		// test();
	}
	public Space[][] getSpace()
	{
		return space;
	}
	public void initiateRoom(int[] doorPair, int index)
	{
		// System.out.println("asdasda");
		setValues(doorPair, index);
		setAgentQueue();
		// System.out.println("-----------------------------------------------------------------");
		// printValues();
	}

	public boolean leadsOutside()
	{
		return leadOutside;
	}

	public void setDoorOutside()
	{
		leadOutside = true;
	}
	private void setAgentQueue()
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

		queue = new ArrayList<Agent>();
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
		// System.out.println("qweqwe");
	}

	public boolean isEmpty()
	{
		return (queue.size() == 0);
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
		Iterator<Agent> iterator = queue.iterator();
		// System.out.println("queue size" +queue.size());
		while(iterator.hasNext())
		{
			
			Agent change = iterator.next();
			System.out.println(change);
			Location currentPosition = change.getPosition();
			Location newPosition = change.getNextMove(space[0][0].getLayerAmount(),space);
			change.updatePosition(newPosition);
			space[currentPosition.getX()][currentPosition.getY()].setAgent(null);
			space[newPosition.getX()][newPosition.getY()].setAgent(change);
			if(change.getType() == (new PanickedAgent()).getType())
			{
				// System.out.println("true");
				if(distanceTravelled(currentPosition,newPosition) >= 2.0)
				{
					if((currentPosition.getX()-newPosition.getX()) != 0)
					{
						// System.out.println("X");
						// System.out.println(currentPosition.getX() +((newPosition.getX()-currentPosition.getX())/2));
						if(space[(currentPosition.getX())+((newPosition.getX()-currentPosition.getX())/2)][currentPosition.getY()].hasAgent())	
						{
							// System.out.println(space[(currentPosition.getX())+((newPosition.getX()-currentPosition.getX())/2)][currentPosition.getY()]);
							space[(currentPosition.getX())+((newPosition.getX()-currentPosition.getX())/2)][currentPosition.getY()].getAgent().stop();
							// System.out.println("@@@@@@@@@@@@@@@@@@@@@@");	
							// System.out.println(space[(currentPosition.getX())+((newPosition.getX()-currentPosition.getX())/2)][currentPosition.getY()].getAgent().ableToMove());
						}
					}
					else
					{
						// System.out.println("Y");
						if(space[currentPosition.getX()][(currentPosition.getY())+((newPosition.getY()-currentPosition.getY())/2)].hasAgent())	
						{
							space[currentPosition.getX()][(currentPosition.getY())+((newPosition.getY()-currentPosition.getY())/2)].getAgent().stop();	
						}
					}
				}
			}
			// System.out.println("check1");

		}
		setAgentQueue();
	}
	public void checkAllDoors()
	{
		checkDoors();
	}


	public void setAgent(String agentType)
	{
		agentsPlaced++;
		Random random = new Random();
		ArrayList<Location> available = new ArrayList<Location>();
		// System.out.println("The room looks like this\n" + this);
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

	public void setTotalLayers(int outsideDoors)
	{
		for(int i = 0;  i < space.length; i++)
		{
			for(int j = 0;  j < space[i].length; j++)
			{
				space[i][j].setPossibleDoors(outsideDoors);
			}
		}
	}

	private void setValues(int[] doorPair, int index)
	{
		for(int i = 0;  i < doorLocation.size(); i++)
		{
			if(doors.get(i).getID() == doorPair[0] || doors.get(i).getID() == doorPair[1])
			{
				int starting = 0;
				if(doorPair[0] == -1 || doorPair[1] == -1)
				{
					starting = 0;
				}
				else
				{
					starting = doors.get(i).getOtherValue(index);

				}
				// System.out.println(starting);
				Location tempLoc = doorLocation.get(i);
				space[tempLoc.getX()][tempLoc.getY()].setValueAt(starting,index);
				ArrayList<Location> temp = new ArrayList<Location>();
				temp.add(tempLoc);
				setValuesAdjecent(space,temp,index);
				i = doorLocation.size();
			}
		}

		// for(int i = 0; i < doorLocation.size(); i++)
		// {
		// 	Location tempLoc = doorLocation.get(i);
		// 	space[tempLoc.getX()][tempLoc.getY()].setValueAt(0,i);
		// 	ArrayList<Location> temp = new ArrayList<Location>();
		// 	temp.add(tempLoc);
		// 	setValuesAdjecent(space,temp,i);
		// }
	}

	private void setValuesAdjecent(Space[][] input,ArrayList<Location> location, int index)
	{
		if(location.isEmpty())
		{
			return;
		}

		ArrayList<Location> adjecent = new ArrayList<Location>();
		// System.out.println(location.size());
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
		// System.out.println(space[0][0].getLayerAmount());
		for(int k = 0; k < space[0][0].getLayerAmount(); k++)
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

	private double distanceTravelled(Location from, Location to)
	{
		return Math.sqrt(Math.pow((from.getX() - to.getX()),2) + Math.pow((from.getY() - to.getY()),2));
	}

	public boolean hasDoor(int doorID)
	{
		for (int i = 0; i < doors.size(); ++i)
		{
			if (doors.get(i).getID() == doorID)
			{
				return true;	
			}
		}
		return false;
	}

	public int getDoorValueAt(int doorID, int index)
	{
		for(int i = 0; i < doors.size(); ++i)
		{
			if(doors.get(i).getID() == doorID)
			{
				return space[doorLocation.get(i).getX()][doorLocation.get(i).getY()].getValueAt(index);
			}
		}
		return -1;
	}

	// need an add door method
	public void addDoor(int x, int y, Door d)
	{
		space[x][y] = new Space(door);
		doorLocation.add(new Location(x,y));
		doors.add(d);
		// System.out.println("The room looks like this\n" + this);
	}
	private int getQueueIndexFromLocation(Location l)
	{
		for (int i = 0; i < queue.size(); ++i)
		{
			if (queue.get(i).getPosition().equals(l))
			{
				return i;
			}
		}
		return -1;
	}
	private void checkDoors()
	{
		Iterator<Location> iterator = doorLocation.iterator();
		Iterator<Door> doorIt = doors.iterator();
		int index = 0;
		while(iterator.hasNext())
		{
			Location doorPos = iterator.next();
			Door currentDoor = doorIt.next();
			if(space[doorPos.getX()][doorPos.getY()].hasAgent() 
				&& !space[doorPos.getX()][doorPos.getY()].getAgent().hasUsedDoor())
			{
				if (!space[doorPos.getX()][doorPos.getY()].getAgent().hasUsedDoor())
				{
					if (currentDoor.leave(space[doorPos.getX()][doorPos.getY()].getAgent()))
					{
						// System.out.println("The queue \n" + queue);
						int rIndex = getQueueIndexFromLocation(doorPos);
						// System.out.println(rIndex);
						queue.remove(rIndex);
						// System.out.println(getQueueIndexFromLocation(door));
						space[doorPos.getX()][doorPos.getY()].setAgent(null);
						// System.out.println("The queue after\n" + queue);
						// need to remove you from the queue
						// System.out.println(queue);
					}
					// else do nothing
				}
				else
					space[doorPos.getX()][doorPos.getY()].getAgent().resetUsedDoor();
			}
			index++;
		}
		// System.out.println("asdasd");
	}
	public boolean placeByDoor(Agent a, int doorID)
	{
		for (int i = 0; i < doors.size(); ++i)
		{
			if (doors.get(i).getID() == doorID)
			{
				// check if there is an agent there
				Location local = doorLocation.get(i);
				if(space[local.getX()][local.getY()].hasAgent())
				{
					return false;
				}
				else
				{
					System.out.println("Move that agent!! into room " + name);
					addAgent(local, a.getType());
					space[local.getX()][local.getY()].getAgent().setUsedDoor();
					// System.out.println(local);
					// a.updatePosition(local);
					// space[local.getX()][local.getY()].setAgent(a);
					// queue.add(a);
					// setAgentQueue();
					// System.out.println(this);
					return true;
				}
			}
		}
		// find the doors location
		return false;
	}
	public void setSize(int w, int h)
	{
		space = new Space[w][h];
		for (int i = 0; i < space.length; ++i)
		{
			for (int j = 0; j < space[i].length; ++j)
			{
				if (j == 0 || j == h - 1 || i == 0 || i == w - 1)
				{
					space[i][j] = new Space(wall);
				}
				else
				{
					space[i][j] = new Space(floor);
					++totalFloors;
				}
			}
		}
		// System.out.println("The room looks like this\n" + this);
	}

	public int doorAmount()
	{
		return doorLocation.size();
	}

	public int getDoorID(int i)
	{
		return doors.get(i).getID();
	}
	public void addAgent(Location l, String type)
	{
		if (type.equals(new CalmAgent().getType()))
			space[l.getX()][l.getY()].setAgent(new CalmAgent());
		else
			space[l.getX()][l.getY()].setAgent(new PanickedAgent());

		space[l.getX()][l.getY()].getAgent().updatePosition(l);
		setAgentQueue();
	}
}