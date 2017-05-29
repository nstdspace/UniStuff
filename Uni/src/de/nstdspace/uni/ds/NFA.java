package de.nstdspace.uni.ds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NFA<S, A> {

	private HashMap<S, HashMap<A, HashSet<S>>> transitions;

	public NFA() {
		this.transitions = new HashMap<>();
	}

	void addTransition(S q, A a, S p) {
		if (!transitions.containsKey(q)) {
			transitions.put(q, new HashMap<>());
		}
		if (!transitions.get(q).containsKey(a)) {
			transitions.get(q).put(a, new HashSet<S>());
		}
		transitions.get(q).get(a).add(p);
	}

	private void simulate(S q, Set<S> result, ArrayList<A> input, int inputIndex) {
		if (transitions.containsKey(q)) {
			HashMap<A, HashSet<S>> transitionsFromQ = transitions.get(q);
			for (A a : transitionsFromQ.keySet()) {
				if (input.get(inputIndex).equals(a)) {
					for (S newState : transitionsFromQ.get(a)) {
						if (!result.contains(newState)) {
							if (inputIndex < input.size() - 1)
								simulate(newState, result, input, inputIndex + 1);
							else
								result.add(newState);
						}
					}
				}
			}
		}
	}

	public Set<S> simulate(S q, List<A> input) {
		HashSet<S> result = new HashSet<>();
		simulate(q, result, new ArrayList<>(input), 0);
		return result;
	}

	enum Sigma {
		RIGHT, DOWN, LEFT, UP
	}

	static NFA<Integer, Sigma> createTestNFA() {
		NFA<Integer, Sigma> m = new NFA<>();
		m.addTransition(1, Sigma.RIGHT, 2);
		m.addTransition(1, Sigma.DOWN, 3);
		m.addTransition(2, Sigma.LEFT, 1);
		m.addTransition(2, Sigma.DOWN, 4);
		m.addTransition(3, Sigma.RIGHT, 4);
		m.addTransition(3, Sigma.UP, 1);
		m.addTransition(4, Sigma.LEFT, 3);
		m.addTransition(4, Sigma.UP, 2);
		return m;
	}

	static void test1() {
		List<Sigma> input = new LinkedList<>();
		input.add(Sigma.RIGHT);
		input.add(Sigma.DOWN);
		input.add(Sigma.LEFT);
		Set<Integer> reachable = createTestNFA().simulate(1, input);
		System.out.println(reachable);
	}

	static void test2() {
		NFA<Integer, Character> nfa = new NFA<>();
		nfa.addTransition(1, 'a', 2);
		nfa.addTransition(2, 'a', 3);
		nfa.addTransition(3, 'a', 4);
		nfa.addTransition(3, 'a', 1);
		ArrayList<Character> input = new ArrayList<>();
		input.add('a');
		input.add('a');
		input.add('a');
		System.out.println(nfa.simulate(1, input));
	}
	
	static void testFile() throws IOException {
		File file = new File("C:\\Users\\Skysoldier\\Desktop\\H8.trans");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line = null;
		NFA<Integer, Character> nfa = new NFA<>();
		while((line = reader.readLine()) != null){
			String lineSplit[] = line.split(" ");
			nfa.addTransition(Integer.parseInt(lineSplit[0]), lineSplit[1].charAt(0), Integer.parseInt(lineSplit[2]));
		}
		ArrayList<Character> input = new ArrayList<>();
		String inputString = "ababbbaa";
		for(char c : inputString.toCharArray()){
			input.add(c);
		}
		System.out.println(nfa.simulate(7, input));
	}
	
	static void debug(){
		NFA<Character, Integer> nfa = new NFA<>();
		nfa.addTransition('a', 1, 'd');
		nfa.addTransition('a', 1, 'c');
		nfa.addTransition('a', 2, 'b');
		nfa.addTransition('b', 1, 'b');
		ArrayList<Integer> input = new ArrayList<>();
		input.add(1);
		System.out.println(nfa.simulate('a', input));
	}

	public static void main(String[] args) {
		// test1();
		// test2();
//		try{
//			testFile();
//		}
//		catch(IOException e){
//			e.printStackTrace();
//		}
		debug();
	}
}
