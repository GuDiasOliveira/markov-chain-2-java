package gudiasoliveira.markovchain2;

import java.util.ArrayList;
import java.util.List;

public class MarkovChain<T> {
	private double[][] mTransitionMatrix;
	private List<T> mStates = new ArrayList<>();
	
	public MarkovChain(double[][] transitionMatrix, List<T> states) {
		setTransitionMatrix(transitionMatrix, states);
	}
	
	public MarkovChain(double[][] transitionMatrix, T[] states) {
		setTransitionMatrix(transitionMatrix, states);
	}
	
	public int size() {
		return mTransitionMatrix.length;
	}
	
	public double get(int i, int j) {
		return mTransitionMatrix[i][j];
	}
	
	public double get(T fromState, T toState) {
		int i = mStates.indexOf(fromState);
		int j = mStates.indexOf(toState);
		return mTransitionMatrix[i][j];
	}
	
	public void set(int i, int j, double p) {
		mTransitionMatrix[i][j] = p;
	}
	
	public void set(T fromState, T toState, double p) {
		int i = mStates.indexOf(fromState);
		int j = mStates.indexOf(toState);
		mTransitionMatrix[i][j] = p;
	}
	
	public T getState(int i) {
		return mStates.get(i);
	}
	
	public void setState(int i, T state) {
		mStates.set(i, state);
	}
	
	public List<T> getStates() {
		List<T> states = new ArrayList<>();
		states.addAll(mStates);
		return states;
	}
	
	public void setStates(List<T> states) {
		int size = size();
		mStates.clear();
		for (int i = 0; i < size; i++)
			mStates.add(states.get(i));
	}
	
	public void setTransitionMatrix(double[][] matrix) {
		int size = size();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++)
				mTransitionMatrix[i][j] = matrix[i][j];
		}
	}
	
	public void setTransitionMatrix(double[][] matrix, List<T> states) {
		int size = matrix.length;
		mTransitionMatrix = new double[size][size];
		mStates.clear();
		for (int i = 0; i < size; i++) {
			mStates.add(states.get(i));
			for (int j = 0; j < size; j++)
				mTransitionMatrix[i][j] = matrix[i][j];
		}
	}
	
	public void setTransitionMatrix(double[][] mat, T[] states) {
		List<T> list = new ArrayList<>();
		for (T state : states)
			list.add(state);
		setTransitionMatrix(mat, list);
	}
	
	public void normalizeTransitionMatrix() {
		int size = size();
		for (int i = 0; i < size; i++) {
			double rowSum = 0;
			for (int j = 0; j < size; j++)
				rowSum += mTransitionMatrix[i][j];
			for (int j = 0; j < size; j++)
				mTransitionMatrix[i][j] /= rowSum;
		}
	}
	
	public double[] getPi(double[] p0, int timeInterval) {
		if (timeInterval < 0)
			return null;
		normalizeTransitionMatrix();
		int size = size();
		double[] s = new double[size];
		for (int index = 0; index < size; index++)
			s[index] = p0[index];
		if (timeInterval == 0) {
			return s;
		}
		for (int time = 0; time < timeInterval; time++) {
			double[] sNext = new double[size];
			for (int index = 0; index < size; index++) {
				sNext[index] = 0;
				for (int k = 0; k < size; k++) {
					sNext[index] += s[k] * mTransitionMatrix[k][index];
				}
			}
			for (int index = 0; index < size; index++)
				s[index] = sNext[index];
		}
		return s;
	}
	
	public double getProbability(T initialState, int timeInterval, ProbabilityCondition<T> condition) {
		double p = 0;
		for (T state : mStates) {
			if (condition.test(state))
				p += this.getProbability(
						initialState,
						state, timeInterval);
		}
		return p;
	}

	public double getProbability(double[] p0, T state, int timeInterval) {
		int stateIndex = mStates.indexOf(state);
		return getPi(p0, timeInterval)[stateIndex];
	}
	
	public double getProbability(T initialState, T state, int timeInterval) {
		int size = size();
		int initialStateIndex = mStates.indexOf(initialState);
		double[] initialStatesProbs = new double[size];
		initialStatesProbs[initialStateIndex] = 1;
		return getProbability(initialStatesProbs, state, timeInterval);
	}
	
	
	public interface ProbabilityCondition<T> {
		boolean test(T valueToTest);
	}
}