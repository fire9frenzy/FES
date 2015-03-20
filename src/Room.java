public class Room
{
	private int wall = 0;
	private int obstacle = 1;
	private int floor = 2;
	private int door = 3;
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
			if(i == 9)
			{
				space[i][0] = new Space(door);
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
				space[i][j] = new Space(floor);
			}
		}
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
}