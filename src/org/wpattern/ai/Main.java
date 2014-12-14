package org.wpattern.ai;

import org.wpattern.ai.mdp.MdpLoader;
import org.wpattern.ai.simbad.MazeBuilder;
import org.wpattern.ai.simbad.MazeLoader;
import org.wpattern.ai.simbad.beans.MazeBean;
import org.wpattern.ai.simbad.window.MdpWindow;

import simbad.gui.Simbad;

public class Main {

	public static void main(String[] args) {
		// request antialising
		System.setProperty("j3d.implicitAntialiasing", " true");

		String filename = System.getProperty("user.dir") + "/resources/mazes/maze_01.sbd";

		// Maze
		MazeLoader mazeLoader = new MazeLoader();

		MazeBean maze = mazeLoader.load(filename);

		// MDP
		MdpLoader mdpLoader = new MdpLoader();

		mdpLoader.load(maze, filename);

		Simbad simbad = new Simbad(new MazeBuilder(maze), false);

		MdpWindow mdpWindow = new MdpWindow(maze, 40, 50);

		simbad.getDesktopPane().add(mdpWindow);
	}

}