import java.util.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.*;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Simulation
{
	private static File inputFile;
	private static double calmPercentage = 50.0;
	private static int startingP = 0;
	private static int incrementP = 0;	
	public static void main(String[] args)
	{
		Building lab = new Building();
		// lab.addRoom(new Room());
		// lab.addRoom(new Room());
		// lab.addRoom(new Room());
		lab.placeAgents(1);
		lab.initiate();		// new Simulation();
		getValuesFromUsers();
		System.out.println(inputFile.getAbsolutePath());
		System.out.println(calmPercentage);
		System.out.println(startingP);
		System.out.println(incrementP);
		// System.out.println()

		// System.exit(0);
		// Building lab = new Building();
		// // lab.addRoom(new Room());
		// // lab.addRoom(new Room());
		// // lab.addRoom(new Room());
		// lab.placeAgents(5);
		// lab.initiate();
		
		// // System.out.println(lab);
		// do
		// {
		// 	lab.update();
		// 	// System.out.println(lab);
		// 	// System.out.println("-------------------------------------------------------");
		// 	// System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		// }
		// while(!lab.isEmpty());
		// System.out.println(lab);
		do
		{
			lab.update();
			// System.out.println(lab);
			// System.out.println("-------------------------------------------------------");
			// System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
		while(!lab.isEmpty());
		// System.out.println(lab);
	}

	public static void getValuesFromUsers()
	{
		int[] test = new int[1];
		// JFrame frame = new JFrame("Fire Emergency Simulatior Parameters");
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setSize(300, 400);
		// frame.setLayout(new FlowLayout());
		JTextField startingAmount = new JTextField(10);
		JTextField increment = new JTextField(5);
		JPanel myPanel = new JPanel();
		JFileChooser file = new JFileChooser();
		JSlider slider = new JSlider(0,100,50);
		// slider.setMinorTickSpacing(1);
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
		inputFile = file.getSelectedFile();
		startingP = Integer.parseInt(startingAmount.getText());
		incrementP = Integer.parseInt(increment.getText());
		calmPercentage = slider.getValue();
		// return test;
	}
}