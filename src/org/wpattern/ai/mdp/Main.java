package org.wpattern.ai.mdp;

import org.wpattern.ai.simbad.mdp.window.MdpWindow;

public class Main {

	public static void main(String[] args) {
		String filename = System.getProperty("user.dir") + "/resources/mazes/maze_01.sbd";

		(new MdpWindow()).start(filename);
	}

}
