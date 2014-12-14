package org.wpattern.ai.mdp.beans;

import java.util.List;

import org.wpattern.ai.mdp.utils.ActionType;
import org.wpattern.ai.simbad.beans.StateBean;

public class MdpBean {

	private double gamma;

	private int numStates;

	private ActionType[] actions;

	private ActionType[] policy;

	private double[] reward;

	private double[] cost;

	private Double[][][] transition;

	private double value[];

	private List<StateBean> terminationStates;

	public MdpBean() {
	}

	public double getGamma() {
		return this.gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public ActionType[] getPolicy() {
		return this.policy;
	}

	public void setPolicy(ActionType[] policy) {
		this.policy = policy;
	}

	public double[] getReward() {
		return this.reward;
	}

	public void setReward(double[] reward) {
		this.reward = reward;
	}

	public double[] getCost() {
		return this.cost;
	}

	public void setCost(double[] cost) {
		this.cost = cost;
	}

	public Double[][][] getTransition() {
		return this.transition;
	}

	public void setTransition(Double[][][] transition) {
		this.transition = transition;
	}

	public double[] getValue() {
		return this.value;
	}

	public void setValue(double[] value) {
		this.value = value;
	}

	public ActionType[] getActions() {
		return this.actions;
	}

	public void setActions(ActionType[] actions) {
		this.actions = actions;
	}

	public List<StateBean> getTerminationStates() {
		return this.terminationStates;
	}

	public void setTerminationStates(List<StateBean> terminationStates) {
		this.terminationStates = terminationStates;
	}

	public int getNumStates() {
		return this.numStates;
	}

	public void setNumStates(int numStates) {
		this.numStates = numStates;
	}

}
