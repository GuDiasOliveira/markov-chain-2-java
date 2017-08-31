package gudiasoliveira.markovchain2;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleAndTest {
	
	@Test
	public void calcPiTest() {
		MarkovChain<Integer> matrix = new MarkovChain<>(
				new double[][] {
					{1.0 / 3,  2.0 / 3,  0,        0      },
					{1.0 / 3,  1.0 / 3,  1.0 / 3,  0      },
					{1.0 / 3,  1.0 / 3,  1.0 / 6,  1.0 / 6},
					{0,        0,        0,        1}
				}, Arrays.asList(100, 200, 300, 400));
		
		// p_2
		double[] expectedResult = {1.0 / 3,  4.0 / 9,  11.0 / 54,  1.0 / 54};
		double[] result = matrix.getPi(
				new double[] {2.0 / 3,  1.0 / 3,  0,  0}, // p_0
				2);
		assertEquals(expectedResult.length, result.length);
		for (int i = 0; i < result.length; i++)
			assertEquals(expectedResult[i], result[i], 0.001);
		
		// p_3
		expectedResult = new double[] {0.32716, 0.43827, 0.18210, 0.05247};
		result = matrix.getPi(
				new double[] {2.0 / 3,  1.0 / 3,  0,  0}, // p_0
				3);
		assertEquals(expectedResult.length, result.length);
		for (int i = 0; i < result.length; i++)
			assertEquals(expectedResult[i], result[i], 0.0001);
		
		// p_1
		expectedResult = new double[] {1.0 / 3,  5.0 / 9,  1.0 / 9, 0};
		result = matrix.getPi(
				new double[] {2.0 / 3,  1.0 / 3,  0,  0},  // p_0
				1);
		assertEquals(expectedResult.length, result.length);
		for (int i = 0; i < result.length; i++)
			assertEquals(expectedResult[i], result[i], 0.0001);
	}
	
	@Test
	public void getProbabilityTest() {
		MarkovChain<String> matrix = new MarkovChain<>(
				new double[][] {
					{1.0 / 3,  2.0 / 3,  0,        0      },
					{1.0 / 3,  1.0 / 3,  1.0 / 3,  0      },
					{1.0 / 3,  1.0 / 3,  1.0 / 6,  1.0 / 6},
					{0,        0,        0,        1}
				}, Arrays.asList("A", "B", "C", "D"));

		double res = matrix.getProbability(new double[] {2.0 / 3,  1.0 / 3,  0,  0}, "C", 2);
		assertEquals(11.0 / 54, res, 0.001);
		res = matrix.getProbability(new double[] {2.0 / 3,  1.0 / 3,  0,  0}, "A", 3);
		assertEquals(0.32716, res, 0.00001);
		res = matrix.getProbability(new double[] {2.0 / 3,  1.0 / 3,  0,  0}, "D", 1);
		assertEquals(0, res, 0.001);
		
		res = matrix.getProbability("B", "A", 2);
		assertEquals(1.0 / 3, res, 0.001);
		res = matrix.getProbability("B", "D", 3);
		assertEquals(9.0 / 108, res, 0.0001);
		res = matrix.getProbability("C", "A", 1);
		assertEquals(1.0 / 3, res, 0.0001);
		res = matrix.getProbability("C", "C", 0);
		assertEquals(1.0, res, 0.0001);
		res = matrix.getProbability("C", "D", 0);
		assertEquals(0.0, res, 0.0001);
		res = matrix.getProbability("C", "B", 0);
		assertEquals(0.0, res, 0.0001);
		res = matrix.getProbability("C", "A", 0);
		assertEquals(0.0, res, 0.0001);
	}
	
	@Test
	public void getChainProbabilityTest() {
		MarkovChain<Character> markov = new MarkovChain<>(
				new double[][] {
					{1.0 / 3,  2.0 / 3,  0,        0      },
					{1.0 / 3,  1.0 / 3,  1.0 / 3,  0      },
					{1.0 / 3,  1.0 / 3,  1.0 / 6,  1.0 / 6},
					{0,        0,        0,        1}
				}, Arrays.asList('A', 'B', 'C', 'D'));
		
		double res;
		res = markov.getProbability('B', 'A', 2);
		assertEquals(1.0 / 3, res, 0.0001);
		res = markov.getProbability('B', 'D', 3);
		assertEquals(9.0 / 108, res, 0.0001);
		
		res = markov.getProbability('C', 'A', 1);
		assertEquals(1.0 / 3, res, 0.0001);
		res = markov.getProbability('C', 'D', 1);
		assertEquals(1.0 / 6, res, 0.0001);
		res = markov.getProbability('C', 'C', 0);
		assertEquals(1.0, res, 0.0001);
		res = markov.getProbability('C', 'A', 0);
		assertEquals(0.0, res, 0.0001);
		res = markov.getProbability('C', 'B', 0);
		assertEquals(0.0, res, 0.0001);
		res = markov.getProbability('C', 'D', 0);
		assertEquals(0.0, res, 0.0001);
		
		res = markov.getProbability('C', 2, new MarkovChain.ProbabilityCondition<Character>() {
			@Override
			public boolean test(Character valueToTest) {
				return valueToTest > 'B';
			}
		});
		assertEquals(12.0 / 36, res, 0.0001);
	}
}