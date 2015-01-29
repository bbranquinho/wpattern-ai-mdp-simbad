package org.wpattern.ai.mdp.robots;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import org.wpattern.ai.simbad.RobotMotion;
import org.wpattern.ai.simbad.beans.MazeBean;
import org.wpattern.ai.simbad.mdp.beans.MdpBean;
import org.wpattern.ai.simbad.mdp.interfaces.IMdpListener;
import org.wpattern.ai.simbad.mdp.interfaces.IRobotMdp;
import org.wpattern.ai.simbad.utils.ActionType;

public class RobotValueIteration extends RobotMotion implements IRobotMdp {

	private final static double epsilon = 1e-3;

	private List<IMdpListener> listeners;

	private MdpBean mdp;

	private MazeBean maze;

	private double gamma;

	private int numStateLines;

	private int numStateColumns;

	private double[][] w;

	private boolean executeMoviment;

	public RobotValueIteration(Vector3d startPosition, String name) {
		super(startPosition, name);
		this.listeners = new ArrayList<IMdpListener>();
	}

	@Override
	public void init() {
		this.numStateLines = this.mdp.getReward().length;
		this.numStateColumns = this.mdp.getReward()[0].length;
		this.w = new double[this.numStateLines][this.numStateColumns];
		this.gamma = this.mdp.getGamma();
		this.executeMoviment = false;

		this.mdp.setValue(new double[this.numStateLines][this.numStateColumns]);
		this.mdp.setPolicy(new ActionType[this.numStateLines][this.numStateColumns]);

		for (int i = 0; i < this.mdp.getValue().length; i++) {
			for (int j = 0; j < this.mdp.getValue()[i].length; j++) {
				this.mdp.getValue()[i][j] = this.mdp.getReward()[i][j];
			}
		}

		this.notiffyAll();
	}

	@Override
	public void run() {
		if (this.executeMoviment) {

		} else {
			this.mdpStep();
		}
	}

	private void mdpStep() {
		double maxError = -1, sum;
		ActionType action;

		for (int s1 = 0; s1 < this.numStateLines; s1++) {
			for (int s2 = 0; s2 < this.numStateColumns; s2++) {
				double maxValue = Double.NEGATIVE_INFINITY;
				int maxAction = -1;

				for (int a1 = 0; a1 < this.mdp.getActions().length; a1++) {
					sum = 0;

					if ((this.maze.getMap()[s1][s2] == null) || (!this.maze.getMap()[s1][s2].contains(this.mdp.getActions()[a1]))) {
						continue;
					}

					for (int a2 = 0; a2 < this.mdp.getTransition()[a1].length; a2++) {
						sum += this.mdp.getTransition()[a1][a2] * this.getSuccValue(this.mdp.getValue(), s1, s2, this.mdp.getActions()[a2]);
					}

					double Q = this.mdp.getReward()[s1][s2] - this.mdp.getCost()[a1] + this.gamma * sum;

					if (maxValue < Q) {
						maxValue = Q;
						maxAction = a1;
					}
				}

				if (maxAction == -1) {
					continue;
				}

				double currentError = Math.abs(maxValue - this.mdp.getValue()[s1][s2]);

				if (currentError > maxError)
					maxError = currentError;

				this.w[s1][s2] = maxValue;
				this.notifyStateValue(s1, s2, this.w[s1][s2]);

				action = this.mdp.getActions()[maxAction];

				if (this.mdp.getPolicy()[s1][s2] != action) {
					this.mdp.getPolicy()[s1][s2] = action;
					this.notifyStatePolicy(s1, s2, action);
				}
			}
		}

		for (int i = 0; i < this.numStateLines; i++) {
			for (int j = 0; j < this.numStateColumns; j++) {
				this.mdp.getValue()[i][j] = this.w[i][j];
			}
		}

		if (maxError <= epsilon) {
			this.executeMoviment = true;
		}
	}

	@Override
	public void setMdp(MdpBean mdp) {
		this.mdp = mdp;
	}

	@Override
	public void setMaze(MazeBean maze) {
		this.maze = maze;
	}

	@Override
	public void registerListener(IMdpListener listener) {
		this.listeners.add(listener);
	}

	@Override
	protected void reset() {
		super.reset();

		this.mdp.setValue(new double[this.numStateLines][this.numStateColumns]);
		this.mdp.setPolicy(new ActionType[this.numStateLines][this.numStateColumns]);

		this.notiffyAll();
	}

	private void notiffyAll() {
		for (int line = 0; line < this.numStateLines; line++) {
			for (int column = 0; column < this.numStateColumns; column++) {
				ActionType newAction = this.mdp.getPolicy()[line][column];
				double newValue = this.mdp.getValue()[line][column];

				for (IMdpListener listener : this.listeners) {
					listener.onPolicyChange(line, column, newAction);
					listener.onValueChange(line, column, newValue);
				}
			}
		}
	}

	private void notifyStatePolicy(int line, int column, ActionType newAction) {
		for (IMdpListener listener : this.listeners) {
			listener.onPolicyChange(line, column, newAction);
		}
	}

	private void notifyStateValue(int line, int column, double value) {
		for (IMdpListener listener : this.listeners) {
			listener.onValueChange(line, column, value);
		}
	}

	private double getSuccValue(double[][] value, int stateLine, int stateColumn, ActionType action) {
		int succLineIndex = this.getLine(stateLine, action);
		int succColumnIndex = this.getColumn(stateColumn, action);

		if ((succLineIndex < 0) || (succLineIndex >= value.length)) {
			return 0.0d;
		}

		if ((succColumnIndex < 0) || (succColumnIndex >= value[0].length)) {
			return 0.0d;
		}

		return value[succLineIndex][succColumnIndex];
	}

	private int getLine(int value, ActionType action) {
		if (action == ActionType.NORTH) {
			return value - 1;
		}

		if (action == ActionType.SOUTH) {
			return value + 1;
		}

		return value;
	}

	private int getColumn(int value, ActionType action) {
		if (action == ActionType.WEST) {
			return value - 1;
		}

		if (action == ActionType.EAST) {
			return value + 1;
		}

		return value;
	}

}
