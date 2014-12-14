package org.wpattern.ai.mdp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.wpattern.ai.mdp.beans.MdpBean;
import org.wpattern.ai.mdp.utils.ActionType;
import org.wpattern.ai.simbad.beans.MazeBean;
import org.wpattern.ai.simbad.beans.StateBean;

public class MdpLoader {

	private static final Charset ENCODING = StandardCharsets.UTF_8;

	private static final String ACTION_FIELD = "a";

	private static final String COST_FIELD = "c";

	private static final String GAMMA_FIELD = "g";

	private static final String PROBABILITY_FIELD = "p";

	private static final String REWARD_FIELD = "r";

	private static final String TERMINAL_STATE_FIELD = "t";

	public MdpBean load(MazeBean maze, String filename) {
		System.out.println(String.format("Loading map [%s].", filename));

		MdpBean mdp = new MdpBean();

		mdp.setNumStates(maze.getMazeHeight() * maze.getMazeWidth());

		try (Scanner scanner =  new Scanner(Paths.get(filename), ENCODING.name())){
			List<String> lines = new ArrayList<String>();
			String nextLine;

			while (scanner.hasNextLine()){
				nextLine = scanner.nextLine().trim();

				if ((nextLine.length() > 0) && (nextLine.charAt(0) != '#')) {
					lines.add(nextLine);
				}
			}

			Collections.sort(lines, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.charAt(0) - o2.charAt(0);
				}
			});

			for (String line : lines) {
				if (line.length() > 0) {
					this.processLine(maze, mdp, line);
				}
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		return mdp;
	}

	private void processLine(MazeBean maze, MdpBean mdp, String line) {
		System.out.println(String.format("MDP processing line [%s].", line));

		StringTokenizer stringTokenizer = new StringTokenizer(line, "(){},= ");

		String key = stringTokenizer.nextToken().trim();

		switch (key) {
		case ACTION_FIELD:
			this.processAction(mdp, stringTokenizer);
			break;

		case COST_FIELD:
			this.processCost(mdp, stringTokenizer);
			break;

		case GAMMA_FIELD:
			mdp.setGamma(Double.parseDouble(stringTokenizer.nextToken().trim()));
			break;

		case PROBABILITY_FIELD:
			this.processProbability(maze, mdp, stringTokenizer);
			break;

		case REWARD_FIELD:
			this.processReward(mdp, maze, stringTokenizer);
			break;

		case TERMINAL_STATE_FIELD:
			this.processTerminalState(mdp, stringTokenizer);
			break;
		}
	}

	private void processProbability(MazeBean maze, MdpBean mdp, StringTokenizer stringTokenizer) {
		this.initializeTransitions(maze, mdp);
		Double[][][] transition = mdp.getTransition();

		for (int i = 0; i < maze.getMazeHeight(); i++) {

		}



	}

	private void initializeTransitions(MazeBean maze, MdpBean mdp) {
		int numStates = maze.getMazeHeight() * maze.getMazeWidth();
		Double[][][] transition = new Double[numStates][mdp.getActions().length][numStates];

		for (int i = 0; i < transition.length; i++) {
			for (int j =0; j < transition[i].length; j++) {
				for (int k = 0; k < transition[i][j].length; k++) {
					transition[i][j][k] = 0.0d;
				}
			}
		}

		mdp.setTransition(transition);
	}

	private void processAction(MdpBean mdp, StringTokenizer stringTokenizer) {
		List<String> values = new ArrayList<String>();

		while (stringTokenizer.hasMoreTokens()) {
			values.add(stringTokenizer.nextToken().trim());
		}

		mdp.setActions(ActionType.parse(values));
	}

	private void processCost(MdpBean mdp, StringTokenizer stringTokenizer) {
		if (mdp.getCost() == null) {
			mdp.setCost(new double[mdp.getActions().length]);
		}

		ActionType action = ActionType.parse(stringTokenizer.nextToken());
		double cost = Double.parseDouble(stringTokenizer.nextToken());

		mdp.getCost()[this.index(mdp.getActions(), action)] = cost;
	}

	private void processReward(MdpBean mdp, MazeBean maze, StringTokenizer stringTokenizer) {
		if (mdp.getReward() == null) {
			mdp.setReward(new double[mdp.getNumStates()]);
		}

		int lineIndex = Integer.parseInt(stringTokenizer.nextToken());
		int columnIndex = Integer.parseInt(stringTokenizer.nextToken());
		double reward = Double.parseDouble(stringTokenizer.nextToken());

		mdp.getReward()[lineIndex * maze.getMazeWidth() + columnIndex] = reward;
	}

	private void processTerminalState(MdpBean mdp, StringTokenizer stringTokenizer) {
		if (mdp.getTerminationStates() == null) {
			mdp.setTerminationStates(new ArrayList<StateBean>());
		}

		int lineIndex = Integer.parseInt(stringTokenizer.nextToken());
		int columnIndex = Integer.parseInt(stringTokenizer.nextToken());

		mdp.getTerminationStates().add(new StateBean(lineIndex, columnIndex));
	}

	private int index(ActionType[] actions, ActionType action) {
		for (int i = 0; i < actions.length; i++) {
			if (actions[i] == action) {
				return i;
			}
		}

		return -1;
	}

}
