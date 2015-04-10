import java.util.*;
import java.awt.*;
import javax.swing.*;
public class Room extends Canvas
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
	private int tileSize = 16;
	private Color[] colors = new Color[4];
	private int updates = 0;
	private boolean onFire = false;
	// private boolean[] valuesSet;
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
	public boolean initiateRoom(int[] doorPair)
	{
		// System.out.println("asdasda");
		// valuesSet[index]= true;
		setSize(space[0].length * tileSize, space.length * tileSize);
		//0 -> wall
		//1 -> obstacle
		//2 -> floor
		//3 -> door
		colors[0] = new Color(0,0,0);
		colors[1] = new Color(255,136,0);
		colors[2] = new Color(255,255,255);
		colors[3] = new Color(0,255,0);

		setAgentQueue();
		return setValues(doorPair);
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
		if(updates == 15)
		{
			if(onFire)
			{
				fireSpread();
				updates = 0;
			}
			updates = 0;
		}
		Iterator<Agent> iterator = queue.iterator();
		// System.out.println("queue size" +queue.size());
		while(iterator.hasNext())
		{
			
			Agent change = iterator.next();
			// System.out.println(change);
			Location currentPosition = change.getPosition();
			Location newPosition = change.getNextMove(space);
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
		++updates;
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
			for(int j = 0;  j < space[i].length; j++)
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

	// public void setTotalLayers(int outsideDoors)
	// {
	// 	for(int i = 0;  i < space.length; i++)
	// 	{
	// 		for(int j = 0;  j < space[i].length; j++)
	// 		{
	// 			space[i][j].setPossibleDoors(outsideDoors);
	// 		}
	// 	}
	// }

	private boolean setValues(int[] doorPair)
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
					starting = doors.get(i).getOtherValue();

				}
				// System.out.println(starting);
				Location tempLoc = doorLocation.get(i);
				if(space[tempLoc.getX()][tempLoc.getY()].isValueSet())
				{
					if(space[tempLoc.getX()][tempLoc.getY()].getValueAt() <= starting)
					{
						return false;
					}
				}
				space[tempLoc.getX()][tempLoc.getY()].setValueAt(starting);
				ArrayList<Location> temp = new ArrayList<Location>();
				temp.add(tempLoc);
				setValuesAdjecent(space,temp);
				i = doorLocation.size();
			}
		}
		return true;
		// for(int i = 0; i < doorLocation.size(); i++)
		// {
		// 	Location tempLoc = doorLocation.get(i);
		// 	space[tempLoc.getX()][tempLoc.getY()].setValueAt(0,i);
		// 	ArrayList<Location> temp = new ArrayList<Location>();
		// 	temp.add(tempLoc);
		// 	setValuesAdjecent(space,temp,i);
		// }
	}

	private void setValuesAdjecent(Space[][] input,ArrayList<Location> location)
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
			int value = input[x][y].getValueAt();
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
						if(setValue(input,new Location(x+i,y+j),value))
						{
							adjecent.add(new Location(x+i,y+j));
						}
					}
				}
			}
		}

		// System.out.println(adjecent.size());
		setValuesAdjecent(input,adjecent);
				
	}

	private boolean validLocation(int x, int y)
	{
		return ((x >= 0 && x < space.length) && (y >= 0 && y < space[x].length));
	}

	private boolean setValue(Space[][] input, Location location, int value)
	{
		if(!validLocation(location.getX(),location.getY()) || input[location.getX()][location.getY()].getType() == wall)
		{
			return false;
		}
		else if(input[location.getX()][location.getY()].isValueSet())
		{
			// System.out.println("in");
			if(input[location.getX()][location.getY()].getType() == obstacle)
			{
				if(input[location.getX()][location.getY()].getValueAt() > (value+2) )
				{
					input[location.getX()][location.getY()].setValueAt(value+2);	
					return true;
				}
				return false;	
			}
			else
			{
				if(input[location.getX()][location.getY()].getValueAt() > (value+1) )
				{
					input[location.getX()][location.getY()].setValueAt(value+1);
					return true;	
				}
				
				return false;
			}	
		}
		else if(input[location.getX()][location.getY()].getType() == obstacle)
		{
			// System.out.println("obstacle");
			input[location.getX()][location.getY()].setValueAt(value+2);	
			return true;
		}
		else
		{
			// System.out.println("floor");
			input[location.getX()][location.getY()].setValueAt(value+1);	
			return true;
		}
		// return false;
	}
	public void printValues()
	{	
		// System.out.println(doorLocation.get(0));
		// System.out.println(doorLocation.get(1));
		// System.out.println(space[0][0].getLayerAmount());

		for(int i = 0; i < space.length; i++)
		{
			for(int j = 0;  j < space[i].length; j++)
			{
				if(space[i][j].getValueAt() == -1)
				{
					System.out.print("x ");
				}
				else
				{
					System.out.print(space[i][j].getValueAt() +" ");
				}
			}
			System.out.println("\n");
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

	public int getDoorValueAt(int doorID)
	{
		for(int i = 0; i < doors.size(); ++i)
		{
			if(doors.get(i).getID() == doorID)
			{
				return space[doorLocation.get(i).getX()][doorLocation.get(i).getY()].getValueAt();
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
			if(space[doorPos.getX()][doorPos.getY()].hasAgent())
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
				// System.out.println(space[doorPos.getX()][doorPos.getY()].getAgent().hasUsedDoor());
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
					// System.out.println("Move that agent!! into room " + name);
					addAgent(local, a.getType());
					space[local.getX()][local.getY()].getAgent().setUsedDoor();
					return true;
				}
			}
		}
		// find the doors location
		return false;
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


	public void produceFire()
	{
		onFire = true;
		ArrayList<Location> wallLocation = new ArrayList<Location>();
		for(int i = 0; i < space.length; ++i)
		{
			for(int j = 0; j < space[i].length; ++j)
			{
				if(space[i][j].getType() == wall)
				{
					wallLocation.add(new Location(i,j));
				}
			}
		}

		Random random = new Random();
		Location temp = wallLocation.get(random.nextInt(wallLocation.size()));
		space[temp.getX()][temp.getY()].setOnFire();

	}
	public void setSpace(Space[][] newSpace)
	{
		space = newSpace;
	}
	public void printDoors()
	{
		for (int i = 0; i < doors.size(); ++i)
		{
			System.out.println(doors.get(i).getID());
		}
	}

	// public boolean isValuesSet(int index)
	// {
	// 	return valuesSet;

	// }
	public void paint(Graphics ctx)
	{
		//0 -> wall
		//1 -> obstacle
		//2 -> floor
		//3 -> door
		for (int i = 0; i < space.length; ++i)
		{
			for (int j = 0; j < space[i].length; ++j)
			{
				// draw walls doors and obsticals
				ctx.setColor(colors[space[i][j].getType()]);
				ctx.fillRect(tileSize * j, tileSize * i, tileSize, tileSize);
				// show weights
				if (space[i][j].getType() == 2)
				{
					int w = space[i][j].getValueAt();
					w *= 2;
					if (w > 180)
						w = 180;
					// w *= 8;
					// if (w > 180)
					// 	w = 180;

					ctx.setColor(new Color(255 - w, 255 - w, 255 - w));
					ctx.fillRect(tileSize * j, tileSize * i, tileSize, tileSize);
				}
				// draw fire
				if (space[i][j].isOnFire())
				{
					ctx.setColor(new Color(255,255,0));
					ctx.fillRect(tileSize * j, tileSize * i, tileSize, tileSize);		
				}
				// draw door numbers
				if (space[i][j].getType() == 3)
				{
					// find door index
					int index = 0;
					for (int x = 0; x < doorLocation.size(); ++x)
					{
						if(doorLocation.get(x).equals(new Location(i,j)))
							index = x;
					}
					// draw that index
					ctx.setColor(new Color(0, 0, 0));
					ctx.drawString(doors.get(index).getDoorPairIndex(), tileSize * j, tileSize * i + tileSize);
				}
				// draw agents
				if (space[i][j].hasAgent())
				{
					// draw circle
					if (space[i][j].getAgent().getType().equals("C"))
						if (space[i][j].getAgent().ableToMove())
							ctx.setColor(new Color(0,0,180));
						else
							ctx.setColor(new Color(100,100,100));
					else
						if (space[i][j].getAgent().ableToMove())
							ctx.setColor(new Color(180,0,0));
						else
							ctx.setColor(new Color(100,100,100));
						// ctx.setColor(new Color(180,0,0));
					ctx.fillOval(j * tileSize, i * tileSize, tileSize, tileSize);
				}
			}
		}

	}

	private void fireSpread()
	{
		ArrayList<Location> newFire = new ArrayList<Location>();
		for(int i = 0; i < space.length; i++)
		{
			for(int j = 0; j < space[i].length; j++)
			{
				if(space[i][j].isOnFire())
				{
					Iterator<Location> iterator;
					iterator = adjecentFire(new Location(i,j)).iterator();

					while(iterator.hasNext())
					{
						Location temp = iterator.next();
						newFire.add(temp);
					}
				}
			}
		}

		for(int i = 0; i < newFire.size(); ++i)
		{
			space[newFire.get(i).getX()][newFire.get(i).getY()].setOnFire();
		}

	}

	private ArrayList<Location> adjecentFire(Location fire)
	{
		ArrayList<Location> temp = new ArrayList<Location>();
		for(int i = -1; i < 2; ++i)
		{
			for(int j = -1; j < 2; ++j)
			{
				if(validLocation(fire.getX()+i,fire.getY()+j))
				{
					if(space[fire.getX()+i][fire.getY()+j].getType() == wall)
					{
						if(!space[fire.getX()+i][fire.getY()+j].isOnFire())
						{
							temp.add(new Location(fire.getX()+i,fire.getY()+j));

						}
					}
				}
			}
		}

		return temp;
	}
}