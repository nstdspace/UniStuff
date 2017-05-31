package de.nstdspace.uni.ds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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

	private HashSet<S> simulate(S q, List<A> input){
		HashSet<S> result = new HashSet<>();
		result.add(q);
		for(int inputIndex = 0; inputIndex < input.size(); inputIndex++){
			A currentInput = input.get(inputIndex);
			HashSet<S> reachableStates = new HashSet<>();
			for(S s : result){
				HashMap<A, HashSet<S>> transitionsFromS = transitions.get(s);
				if(transitionsFromS.containsKey(currentInput)){
					reachableStates.addAll(transitionsFromS.get(currentInput));
				}
			}
			result = reachableStates;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<A> createInputList(A... inputs){
		ArrayList<A> inputList = new ArrayList<>();
		for(A a : inputs){
			inputList.add(a);
		}
		return inputList;
	}
	
	enum Sigma {
		RIGHT, DOWN, LEFT, UP
	}

	static void test1() {
		LinkedList<Sigma> input = new LinkedList<>();
		NFA<Integer, Sigma> m = new NFA<>();
		m.addTransition(1, Sigma.RIGHT, 2);
		m.addTransition(1, Sigma.DOWN, 3);
		m.addTransition(2, Sigma.LEFT, 1);
		m.addTransition(2, Sigma.DOWN, 4);
		m.addTransition(3, Sigma.RIGHT, 4);
		m.addTransition(3, Sigma.UP, 1);
		m.addTransition(4, Sigma.LEFT, 3);
		m.addTransition(4, Sigma.UP, 2);
		input.add(Sigma.RIGHT);
		input.add(Sigma.DOWN);
		input.add(Sigma.LEFT);
		System.out.println(m.simulate(1, input));
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
		System.out.println(nfa.simulate(7, nfa.createInputList('a', 'b', 'a', 'b', 'b', 'b', 'a', 'a')));
	}

	public static void main(String[] args) {
		test1();
		try{
			testFile();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
