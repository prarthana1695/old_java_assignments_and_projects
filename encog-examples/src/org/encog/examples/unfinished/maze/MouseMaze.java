/*
 * Encog(tm) Examples v2.4
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010 by Heaton Research Inc.
 * 
 * Released under the LGPL.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * 
 * Encog and Heaton Research are Trademarks of Heaton Research, Inc.
 * For information on Heaton Research trademarks, visit:
 * 
 * http://www.heatonresearch.com/copyright.html
 */

package org.encog.examples.unfinished.maze;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class MouseMaze extends JFrame implements Runnable, ActionListener {
	
	public enum State {
		Running,
		Stopped,
		Training
	}
	
	private Maze maze;
	private MazePanel mazePanel;
	private JButton buttonRun = new JButton("Run");
	private JButton buttonTrain = new JButton("Train");
	private JButton buttonStop = new JButton("Stop");
	private State state;
	private boolean requestStop = false;

	public MouseMaze() {
		this.state = State.Stopped;
		this.maze = new Maze(30,30);		
		this.maze.generateMaze();
		this.mazePanel = new MazePanel(this.maze);
		this.buttonStop.setEnabled(false);

		Container content = getContentPane();

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		content.setLayout(gridbag);

		c.fill = GridBagConstraints.NONE;
		c.weightx = 1.0;
		// maze
		c.gridwidth = 1;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.NORTHWEST;

		mazePanel.setSize(301, 301);
		content.add(mazePanel, c);

		// Current state
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		content.add(this.buttonRun, c);
		content.add(this.buttonTrain, c);
		content.add(this.buttonStop, c);

		// north
		c.gridwidth = 1;

		// adjust size and position
		pack();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension d = toolkit.getScreenSize();
		setLocation((int) (d.width - this.getSize().getWidth()) / 2,
				(int) (d.height - this.getSize().getHeight()) / 2);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		this.buttonRun.addActionListener(this);
		this.buttonTrain.addActionListener(this);
		this.buttonStop.addActionListener(this);
	}

	public static void main(String args[]) {
		(new MouseMaze()).setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if( e.getSource()==this.buttonRun )
		{
			performRun();
		}	
		else if( e.getSource()==this.buttonTrain )
		{
			performTrain();
		}
		else if( e.getSource()==this.buttonStop )
		{
			performStop();
		}
	}
	
	private void performRun()
	{			
		this.buttonRun.setEnabled(false);
		this.buttonTrain.setEnabled(false);
		this.buttonStop.setEnabled(true);
		this.state = State.Running;
		Thread t = new Thread(this);
		t.start();
	}
	
	private void performTrain()
	{
		this.buttonRun.setEnabled(false);
		this.buttonTrain.setEnabled(false);
		this.buttonStop.setEnabled(true);
		this.state = State.Training;
		Thread t = new Thread(this);
		t.start();		
	}
	
	private void performStop()
	{
		this.buttonRun.setEnabled(false);
		this.buttonTrain.setEnabled(false);
		this.buttonStop.setEnabled(false);
		this.requestStop = true;
	}
	
	public void run() {
		
		switch(this.state)
		{
		case Training:
			threadTrain();
			break;
		case Running:
			threadRun();
			break;
		}
		
		// done
		this.requestStop = false;
		this.state = State.Stopped;
		this.buttonRun.setEnabled(true);
		this.buttonTrain.setEnabled(true);
		this.buttonStop.setEnabled(false);
	}
	
	public void threadTrain()
	{
		while(!this.requestStop)
		{
			try {
				Thread.sleep(1000);
				System.out.println("Tick");
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void threadRun()
	{
		NeuralMouse mouse = MouseFactory.generateSmartMouse();
		
		while(!this.requestStop)
		{
			this.mazePanel.setMouseX(mouse.getX());
			this.mazePanel.setMouseY(mouse.getY());
			this.mazePanel.repaint();
			mouse.move();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

}
