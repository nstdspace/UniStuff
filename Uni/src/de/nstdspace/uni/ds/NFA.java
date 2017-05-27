package de.nstdspace.uni.ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NFA<S, A> {
	
	private HashMap<S, HashMap<A, S>> transitions;
	
	public NFA(Set<S> states){
		this.transitions = new HashMap<>();
	}

	void addTransition(S q, A a, S p){
		if(!transitions.containsKey(q)){
			transitions.put(q, new HashMap<>());
		}
		transitions.get(q).put(a, p);
	}
	
	private void simulate(S q, Set<S> result, List<A> input){
		if(transitions.containsKey(q)){
			HashMap<A, S> transitionsFromQ = transitions.get(q);
			for(A a : transitionsFromQ.keySet()){
				if(input.contains(a)){
					S newState = transitionsFromQ.get(a);
					if(!result.contains(newState)){
						result.add(transitionsFromQ.get(a));
						simulate(newState, result, input);
					}
				}
			}
		}
	}
	
	public Set<S> simulate(S q, List<A> w){
		HashSet<S> result = new HashSet<>();
		simulate(q, new HashSet<S>(), w);
		return result;
	}
	
	enum Sigma {
		RIGHT, DOWN, LEFT, UP
	}

	public static void main(String[] args) {
		Set<Integer> states = new HashSet<>();
		states.add(1);
		states.add(2);
		states.add(3);
		states.add(4);
		NFA<Integer, Sigma> m = new NFA<>(states);
		m.addTransition(1, Sigma.RIGHT, 2);
		m.addTransition(1, Sigma.DOWN, 3);
		m.addTransition(2, Sigma.LEFT, 1);
		m.addTransition(2, Sigma.DOWN, 4);
		m.addTransition(3, Sigma.RIGHT, 4);
		m.addTransition(3, Sigma.UP, 1);
		m.addTransition(4, Sigma.LEFT, 3);
		m.addTransition(4, Sigma.UP, 2);
		List<Sigma> input = new LinkedList<>();
		input.add(Sigma.RIGHT);
//		input.add(Sigma.DOWN);
//		input.add(Sigma.LEFT);
		Set<Integer> reachable = m.simulate(1, input);
		System.out.println(reachable);
	}
}
