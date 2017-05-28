import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NFA<S, A> {
	
	private HashMap<S, HashMap<A, HashSet<S>>> transitions;
	
	public NFA(Set<S> states){
		this.transitions = new HashMap<>();
	}

	void addTransition(S q, A a, S p){
		if(!transitions.containsKey(q)){
			transitions.put(q, new HashMap<>());
		}
        	if(!transitions.get(q).containsKey(a)){
        		transitions.get(q).put(a, new HashSet<S>());
        	}  
		transitions.get(q).get(a).add(p);
	}
	
	private void simulate(S q, Set<S> result, ArrayList<A> input, int inputIndex){
		if(transitions.containsKey(q)){
			HashMap<A, HashSet<S>> transitionsFromQ = transitions.get(q);
			for(A a : transitionsFromQ.keySet()){
				if(input.get(inputIndex).equals(a)){
                    			for(S newState : transitionsFromQ.get(a)){
                        			if(!result.contains(newState)){
					 	    if(inputIndex < input.size() - 1)	
                      		    			simulate(newState, result, input, inputIndex + 1);
                            			    else 
                        	    			result.add(newState);
                        			}
                    			}
				}
			}
		}
    	}
	
	public Set<S> simulate(S q, List<A> input){
        	HashSet<S> result = new HashSet<>();
		simulate(q, result, new ArrayList<>(input), 0);
        	return result;
	}
	
	enum Sigma {
		RIGHT, DOWN, LEFT, UP
	}
	
  	static NFA<Integer, Sigma> createTestNFA(){
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
        	return m;
    	}
  
 	static void test1(){
    		List<Sigma> input = new LinkedList<>();
		input.add(Sigma.RIGHT);
		input.add(Sigma.DOWN);
		input.add(Sigma.LEFT);
		Set<Integer> reachable = createTestNFA().simulate(1, input);
		System.out.println(reachable);
    	}
  
  	static void test2(){
        	HashSet<Integer> states = new HashSet<>();
      		states.add(1);
        	states.add(2);
        	states.add(3);
	        states.add(4);
    		NFA<Integer, Character> nfa = new NFA<>(states);
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
  
	public static void main(String[] args) {
		//test1();
    		test2();
    }
}
