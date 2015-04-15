import java.util.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

// add weighting to the rooms
// spread fire over the floor/kill people
// 

public class Simulation
{
	private static File inputFile;
	private static double calmPercentage = 50.0;
	private static int startingP = 10;
	private static int incrementP = 10;	
	private static int max = 10;
	public static void main(String[] args) throws InterruptedException
	{

		boolean showEvac = false;
		if(args.length > 0)
		{
			for(int i = 0; i < args.length; ++i)
			{
				if(args[i].compareToIgnoreCase("-g")==0)
				{
					showEvac = true;
				}
				else if(args[i].compareToIgnoreCase("-h")==0)
				{
					printHelp();
					System.exit(0);
					// showEvac = true;
				}
				else if(args[i].compareToIgnoreCase("-s")==0)
				{
					i++;
					startingP = Integer.parseInt(args[i]);
					if(startingP < 0)
					{
						printHelp();
						System.exit(0);
					}
				}
				else if(args[i].compareToIgnoreCase("-i")==0)
				{
					i++;
					incrementP = Integer.parseInt(args[i]);
					if(incrementP <= 0)
					{
						printHelp();
						System.exit(0);
					}
				}
				else if(args[i].compareToIgnoreCase("-p")==0)
				{
					i++;
					calmPercentage = Double.parseDouble(args[i]);
					if(calmPercentage < 0.0 || calmPercentage > 100)
					{
						printHelp();
						System.exit(0);
					}
				}
				else
				{
					inputFile = new File(args[i]);
				}

			}
		}


		if(!showEvac && args.length == 0)
		{
			printHelp();
			System.exit(0);			
		}
		Building lab;
		if (showEvac)
		{
			getValuesFromUsers();
			lab = new Building(inputFile);
			lab.placeAgents(startingP,calmPercentage);
			lab.initiate();		// new Simulation();
			lab.createGUI();	
		}
		else
		{
			lab = new Building(inputFile);
			lab.placeAgents(startingP,calmPercentage);
			lab.initiate();		// new Simulation();
		}
		// used for manual stepping
		// Scanner scan = new Scanner(System.in);
		max = lab.getMaxPeople();
		System.out.println(max);
		int updateCount = 0;
		System.out.println("People\tTurns");
		for(int count = startingP; count <= max; count = count+incrementP)
		{
			for(int i = 0; i < 10; i++)
			{
				updateCount = 0;
				do
				{
					++updateCount;
					lab.update();
					// scan.next();
					if (showEvac)
						Thread.sleep(500);
				}
				while(!lab.isEmpty());
				lab = new Building(inputFile);
				lab.placeAgents(count,calmPercentage);
				lab.initiate();
				System.out.println(count+"\t"+updateCount);
			}
			
		}
	}

	public static void getValuesFromUsers()
	{
		JTextField startingAmount = new JTextField(10);
		JTextField increment = new JTextField(5);
		JPanel myPanel = new JPanel();
		JFileChooser file = new JFileChooser();
		JSlider slider = new JSlider(0,100,50);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(5); // step / interavl
   		slider.setSnapToTicks(true);
   		slider.setPaintTicks(true);

   		slider.setPaintLabels(true);
		myPanel.add(new JLabel("Starting Amount of People:"));
		myPanel.add(startingAmount);

		myPanel.add(new JLabel("People Increment:"));
		myPanel.add(increment);
		myPanel.add(new JLabel("Presense of Calm Agents"));
		myPanel.add(slider);
		myPanel.add(new JLabel("Select Building Template"));
		myPanel.add(file);
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		int result = JOptionPane.showConfirmDialog(null, myPanel, "FES Program", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.CANCEL_OPTION)
		{
			System.exit(0);
			// System.out.println("OK");
		}
		if(result == JOptionPane.CLOSED_OPTION)
		{
			System.exit(0);
			// System.out.println("OK");
		}
		if(result == JOptionPane.OK_OPTION)
		{
			inputFile = file.getSelectedFile();
			startingP = Integer.parseInt(startingAmount.getText());
			incrementP = Integer.parseInt(increment.getText());
			calmPercentage = slider.getValue();
			// System.out.println(startingP);
			if(startingP < 0)
			{
				printHelp();
				System.exit(0);
			}
			if(incrementP <= 0)
			{
				printHelp();
				System.exit(0);
			}
			if(calmPercentage < 0.0 || calmPercentage > 100)
			{
				printHelp();
				System.exit(0);
			}	
		}
	}

	public static void printHelp()
	{
		System.out.println("Fire Emergency Simulator (FES)");
		System.out.println("To run: java Simulator <BuildingTemplate.Building>");
		System.out.println("Options:");
		System.out.println("-s -- <number> indicates the number (1...MAX for building) of starting people within the building");
		System.out.println("-i -- <number> indicates the increment (1...MAX for building) for each iteration");
		System.out.println("-p -- <number> indicate the percentage (0...100) of calm people present");		
		System.out.println("-G -- User will use GUI instead of command line (overwrites command line input)");
		System.out.println("-h -- print this message:");
	}

}