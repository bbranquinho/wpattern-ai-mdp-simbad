package org.wpattern.ai.mdp;

import org.wpattern.ai.mdp.beans.MdpBean;

public class ValueIteration {

	private double epsilon = 1e-3;

	public void valueIteration(MdpBean mdp, double gamma) {
		int states = mdp.getNumStates();

		double[] value = new double[states];

		double maxError = -1;
		double sum;
		double w[] = new double[states];

		for (int i = 0; i < states; i++) {
			value[i] = mdp.getReward()[i];
		}

		do {
			maxError = -1;
			for (int s1 = 0; s1 < states; s1++) {
				double maxValue = Double.NEGATIVE_INFINITY;
				int maxAction = -1;
				for (int a = 0; a < mdp.getActions().length; a++) {
					sum = 0;

					for (int s2 = 0; s2 < states; s2++)
						sum += mdp.getTransition()[s1][a][s2] * value[s2];

					double Q = mdp.getReward()[s1] - mdp.getCost()[a] + gamma * sum;

					if (maxValue < Q) {
						maxValue = Q;
						maxAction = a;
					}
				}

				double currentError = Math.abs(maxValue - value[s1]);

				if (currentError > maxError)
					maxError = currentError;

				w[s1] = maxValue;
				mdp.getPolicy()[s1] = mdp.getActions()[maxAction];
			}

			for (int i = 0; i < states; i++) {
				value[i] = w[i];
			}

		} while (maxError > this.epsilon);
	}

}
