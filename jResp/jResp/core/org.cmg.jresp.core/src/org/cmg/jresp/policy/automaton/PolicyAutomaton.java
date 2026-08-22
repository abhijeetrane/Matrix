/**
 * Copyright (c) 2014 Concurrency and Mobility Group.
 * Universita' di Firenze
 *	
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Michele Loreti
 *      Andrea Margheri
 */
package org.cmg.jresp.policy.automaton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

//import org.cmg.jresp.behaviour.Action;
import org.cmg.jresp.behaviour.Agent;
import org.cmg.jresp.knowledge.Attribute;
import org.cmg.jresp.knowledge.Template;
import org.cmg.jresp.knowledge.Tuple;
import org.cmg.jresp.policy.ActionThisID;
import org.cmg.jresp.policy.AuthorizationDecision;
import org.cmg.jresp.policy.AuthorizationPolicy;
import org.cmg.jresp.policy.AuthorizationRequest;
import org.cmg.jresp.policy.AuthorizationResponse;
import org.cmg.jresp.policy.INodePolicy;
import org.cmg.jresp.policy.facpl.ObligationType;
import org.cmg.jresp.policy.facpl.elements.util.TargetTreeRepresentation;
import org.cmg.jresp.topology.GroupPredicate;
import org.cmg.jresp.topology.PointToPoint;
import org.cmg.jresp.topology.Target;

/**
 * @author Michele Loreti
 * @author Andrea Margheri
 * 
 */
public class PolicyAutomaton extends AuthorizationPolicy implements INodePolicy {

	private ArrayList<IPolicyAutomatonState> policyStates;

	private ArrayList<LinkedList<PATransition>> rules;

	private int currentState;
	
	public PolicyAutomaton() {
		this.policyStates = new ArrayList<IPolicyAutomatonState>();
		this.rules = new ArrayList<LinkedList<PATransition>>();
		this.currentState = 0;
	}

	public PolicyAutomaton(IPolicyAutomatonState... states) {
		this.policyStates = new ArrayList<IPolicyAutomatonState>();
		for (int i = 0; i < states.length; i++) {
			this.policyStates.add(states[i]);
		}
		this.rules = new ArrayList<LinkedList<PATransition>>(states.length);
		// initialise list of transition for each state
		for (int i = 0; i < states.length; i++) {
			this.rules.add(i, new LinkedList<PATransition>());
		}
		this.currentState = 0;
	}

	public void addState(int index, IPolicyAutomatonState state) {
		this.policyStates.add(index, state);
	}

	public void addState(IPolicyAutomatonState state) {
		this.policyStates.add(state);
	}

	public void setCurrentState(int idx) {
		this.currentState = idx;
	}

	/**
	 * Return the number of automaton's state
	 * 
	 * @return
	 */
	public int size() {
		return policyStates.size();
	}

	/**
	 * Add a transition in the automaton transition function
	 * 
	 * @param source
	 *            state
	 * @param TransitionCondition
	 * @param target
	 *            state
	 */
	public void addTransitionRule(int src, int trg, TargetTreeRepresentation condition) {
		_addTransitionRule(src, new PATransition(condition, trg));
	}

	/**
	 * Add all the transitions to the automaton
	 * 
	 * @param rules
	 */
	public void addTransitionRules(ArrayList<LinkedList<PATransition>> rules) {
		this.rules = rules;
	}

	/**
	 * Add Transition to the Automaton
	 * 
	 * @param src
	 * @param paTransition
	 */
	private void _addTransitionRule(int src, PATransition paTransition) {
		LinkedList<PATransition> stateRules = rules.get(src);
		if (stateRules == null) {
			stateRules = new LinkedList<PATransition>();
			rules.set(src, stateRules);
		}
		stateRules.add(paTransition);
	}

	/**
	 * Authorize the AuthorizationRequest created by the methods implementing
	 * the authorization predicates
	 * 
	 * @param req
	 *            AuthorizationRequest
	 * @return AuthorizationDecion and possibly a sequence of actions
	 */
	private AuthorizationResponse evaluateRequestOnState(AuthorizationRequest req) {
		return policyStates.get(currentState).evaluate(req, this.node.getName());
	}

	/**
	 * Evaluate the transition function in order to calculate the new
	 * automaton's state
	 * 
	 * @param req
	 */
	private void updatePolicState(AuthorizationRequest req) {
		if (rules.size() > 0) {
			LinkedList<PATransition> stateTransitions = rules.get(currentState);
			for (PATransition paTransition : stateTransitions) {
				// ADD policy Name
				if (paTransition.isEnabled(req, node.getName())) {
					System.out.println("Automaton state updated to: " + paTransition.nextState);
					currentState = paTransition.nextState;
					return;
				}
			}
		}
	}

	/*
	 * ##################################################### -> Implementation
	 * of AUTHORIZATION PREDICATEs ->-> These are the entry points invoked by
	 * Node in order to authorize the action ->-> (and then execute it)
	 * #####################################################
	 */

	/*
	 * ---> Point-To-Point remote authorization predicate
	 */

	@Override
	public boolean acceptPut(PointToPoint from, int session, Tuple tuple) throws InterruptedException, IOException {
		this.lock.lock();

		Attribute[] subAttributes = node.sendAttributeRequest(from);
		AuthorizationRequest req = new AuthorizationRequest(
				// Subject
				from.getName(),
				// Object
				this.node.getName(), ActionThisID.ACCEPT_PUT, tuple, from,
				// Subject
				subAttributes,
				// Object
				getAttributes(node.getInterface()));

		AuthorizationResponse res = evaluateRequestOnState(req);
		updatePolicState(req);
		this.lock.unlock();

		if (res.getDecision() == AuthorizationDecision.PERMIT) {
			// Action Authorised
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			//System.out.println("Permitting put action by " + from.toString() + "  with argument "+ tuple.toString());
			node.put(from, session, tuple);
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		} else {
			// action not authorised, obligation actions executed
			System.out.println("Denying put action by " + from.toString() + "  with argument "+ tuple.toString());
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
			node.sendFail(from, session, "Denying put action by " + from.toString() + "  with argument "+ tuple.toString());
		}
		return true;
	}

	@Override
	public Tuple acceptGet(PointToPoint from, int session, Template template) throws InterruptedException, IOException {
		Tuple t = null;
		this.lock.lock();

		Attribute[] subAttributes = node.sendAttributeRequest(from);
		AuthorizationRequest req = new AuthorizationRequest(
				// Subject
				from.getName(),
				// Object
				this.node.getName(), ActionThisID.ACCEPT_GET, template, from,
				// Subject
				subAttributes,
				// Object
				getAttributes(node.getInterface()));

		AuthorizationResponse res = evaluateRequestOnState(req);
		updatePolicState(req);
		this.lock.unlock();

		if (res.getDecision() == AuthorizationDecision.PERMIT) {
			// Action Authorised
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			t = node.get(template);
			//System.out.println("Permitting get action by " + from + " with template "+ template.toString());
			if (t != null) {
				node.sendTuple(from, session, t);
			} else {
				node.sendFail(from, session, "Tuple not found!");
			}

			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		} else {
			// action not authorised, obligation actions executed
			System.out.println("Denying get action by " + from + " with template "+ template.toString());
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		}
		return t;
	}

	@Override
	public Tuple acceptQuery(PointToPoint from, int session, Template template)
			throws InterruptedException, IOException {
		Tuple t = null;
		this.lock.lock();

		Attribute[] subAttributes = node.sendAttributeRequest(from);
		AuthorizationRequest req = new AuthorizationRequest(
				// Subject
				from.getName(),
				// Object
				this.node.getName(), ActionThisID.ACCEPT_QRY, template, from,
				// Subject
				subAttributes,
				// Object
				getAttributes(node.getInterface()));

		AuthorizationResponse res = evaluateRequestOnState(req);
		updatePolicState(req);
		this.lock.unlock();

		if (res.getDecision() == AuthorizationDecision.PERMIT) {
			// Action Authorised
			// Execution of before actions
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			// execution of action
			t = node.query(template);
			//System.out.println("Permitting query action by " + from.toString() + "  with argument "+ t.toString());

			if (t != null) {
				node.sendTuple(from, session, t);
			} else {
				node.sendFail(from, session, "Tuple not found!");
			}

			// execution of after actions
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		} else {
			System.out.println("Denying query action by " + from + " with template "+ template.toString());
			// action not authorised, obligation actions executed
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		}
		return t;
	}

	/*
	 * ---> authorization for local, pointToPoint or group actions
	 */

	@Override
	public boolean put(Agent a, Tuple t, Target l) throws InterruptedException, IOException {

		AuthorizationResponse res = null;
		AuthorizationRequest req = null;
		boolean isAuthorised = false;

		while (!isAuthorised) {

			this.lock.lock();
			// Create authorisation request
			req = createRequest(ActionThisID.PUT, l, t);
			// Evaluation of request
			res = evaluateRequestOnState(req);
			// Update Automaton State
			updatePolicState(req);
			this.lock.unlock();

			if (res.getDecision() == AuthorizationDecision.PERMIT) {
				isAuthorised = true;
			} else {
				System.out.println(a.getName() + " Decision DENY - PUT " + t.toString());
				// action not authorised, executing obligation actions
				if (res.getObligations(ObligationType.BEFORE).size() > 0) {
					executeActions(node, res.getObligations(ObligationType.BEFORE));
				}
				if (res.getObligations(ObligationType.AFTER).size() > 0) {
					executeActions(node, res.getObligations(ObligationType.AFTER));
				}
				Thread.sleep(100);
			}
		}

		// Action Authorised
		if (res.getObligations(ObligationType.BEFORE).size() > 0) {
			executeActions(node, res.getObligations(ObligationType.BEFORE));
		}
		// execution of action put
		node.put(t, l);

		if (res.getObligations(ObligationType.AFTER).size() > 0) {
			executeActions(node, res.getObligations(ObligationType.AFTER));
		}

		return true;
	}

	@Override
	public Tuple get(Agent a, Template t, Target l) throws InterruptedException, IOException {
		AuthorizationResponse res = null;
		AuthorizationRequest req = null;
		boolean isAuthorised = false;

		while (!isAuthorised) {

			this.lock.lock();
			// Create authorisation request
			req = createRequest(ActionThisID.GET, l, t);
			// Evaluation of request
			res = evaluateRequestOnState(req);
			// Update Automaton State
			updatePolicState(req);
			this.lock.unlock();

			if (res.getDecision() == AuthorizationDecision.PERMIT) {
				isAuthorised = true;
			} else {
				System.out.println(a.getName() + " Decision DENY - GET " + t.toString());
				// action not authorised, executing obligation actions
				if (res.getObligations(ObligationType.BEFORE).size() > 0) {
					executeActions(node, res.getObligations(ObligationType.BEFORE));
				}
				if (res.getObligations(ObligationType.AFTER).size() > 0) {
					executeActions(node, res.getObligations(ObligationType.AFTER));
				}
				Thread.sleep(100);
			}
		}

		Tuple result = null;

		if (res.getObligations(ObligationType.BEFORE).size() > 0) {
			executeActions(node, res.getObligations(ObligationType.BEFORE));
		}
		// execution of action get
		result = node.get(t, l);
		if (res.getObligations(ObligationType.AFTER).size() > 0) {
			executeActions(node, res.getObligations(ObligationType.AFTER));
		}

		return result;
	}

	@Override
	public Tuple query(Agent a, Template t, Target l) throws InterruptedException, IOException {

		AuthorizationResponse res = null;
		AuthorizationRequest req = null;
		boolean isAuthorised = false;

		while (!isAuthorised) {

			this.lock.lock();
			// Create authorisation request
			req = createRequest(ActionThisID.QRY, l, t);
			// Evaluation of request
			res = evaluateRequestOnState(req);
			// Update Automaton State
			updatePolicState(req);
			this.lock.unlock();

			if (res.getDecision() == AuthorizationDecision.PERMIT) {
				isAuthorised = true;
			} else {
				System.out.println(a.getName() + " Decision DENY - QUERY " + t.toString());
				// action not authorised, executing obligation actions
				if (res.getObligations(ObligationType.BEFORE).size() > 0) {
					executeActions(node, res.getObligations(ObligationType.BEFORE));
				}
				if (res.getObligations(ObligationType.AFTER).size() > 0) {
					executeActions(node, res.getObligations(ObligationType.AFTER));
				}
				Thread.sleep(100);
			}
		}

		Tuple result = null;
		if (res.getObligations(ObligationType.BEFORE).size() > 0) {
			executeActions(node, res.getObligations(ObligationType.BEFORE));
		}
		// execution of action get
		result = node.query(t, l);
		if (res.getObligations(ObligationType.AFTER).size() > 0) {
			executeActions(node, res.getObligations(ObligationType.AFTER));
		}

		return result;
	}

	@Override
	public void exec(Agent a, Agent b) throws InterruptedException {
		AuthorizationResponse res = null;
		AuthorizationRequest req = null;
		boolean isAuthorised = false;

		try {

			while (!isAuthorised) {

				this.lock.lock();
				/*
				 * Subject and Object identifiers and interfaces are equal in
				 * the case of EXEC actions
				 */
				req = new AuthorizationRequest(this.node.getName(), this.node.getName(), ActionThisID.EXEC, b, null,
						getAttributes(node.getInterface()), getAttributes(node.getInterface()));
				res = evaluateRequestOnState(req);
				updatePolicState(req);
				this.lock.unlock();

				if (res.getDecision() == AuthorizationDecision.PERMIT) {
					isAuthorised = true;
				} else {
					System.out.println("Decision DENY - EXEC");
					// action not authorised, executing obligation actions
					if (res.getObligations(ObligationType.BEFORE).size() > 0) {
						executeActions(node, res.getObligations(ObligationType.BEFORE));
					}
					if (res.getObligations(ObligationType.AFTER).size() > 0) {
						executeActions(node, res.getObligations(ObligationType.AFTER));
					}
					Thread.sleep(100);
				}

			}

			// Action Authorised
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			// execution of agent
			a.exec(b);
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new InterruptedException();
		}

	}

	@Override
	public String fresh(Agent a) throws InterruptedException {
		AuthorizationResponse res = null;
		AuthorizationRequest req = null;
		boolean isAuthorised = false;

		try {

			while (!isAuthorised) {

				this.lock.lock();
				/*
				 * Subject and Object identifiers and interfaces are equal in
				 * the case of FRESH actions
				 */
				req = new AuthorizationRequest(this.node.getName(), this.node.getName(), ActionThisID.FRESH, null, null,
						getAttributes(node.getInterface()), getAttributes(node.getInterface()));
				res = evaluateRequestOnState(req);
				updatePolicState(req);
				this.lock.unlock();

				if (res.getDecision() == AuthorizationDecision.PERMIT) {
					isAuthorised = true;
				} else {
					System.out.println(a.getName() + " Decision DENY - FRESH");
					// action not authorised, executing obligation actions
					if (res.getObligations(ObligationType.BEFORE).size() > 0) {
						executeActions(node, res.getObligations(ObligationType.BEFORE));
					}
					if (res.getObligations(ObligationType.AFTER).size() > 0) {
						executeActions(node, res.getObligations(ObligationType.AFTER));
					}
					Thread.sleep(100);
				}
			}

			String result = "";
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			// execution of action fresh
			result = node.fresh();
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
			return result;

		} catch (IOException e) {
			e.printStackTrace();
			throw new InterruptedException();
		}
	}

	@Override
	public Object readAttribute(String name) throws InterruptedException {
		AuthorizationResponse res = null;
		boolean isAuthorised = false;
		try {
			while (!isAuthorised) {

				this.lock.lock();
				/*
				 * Subject and Object identifiers and interfaces are the same
				 */
				AuthorizationRequest req = new AuthorizationRequest(this.node.getName(), this.node.getName(),
						ActionThisID.READ, null, null, getAttributes(node.getInterface()),
						getAttributes(node.getInterface()));
				res = evaluateRequestOnState(req);
				updatePolicState(req);
				this.lock.unlock();

				if (res.getDecision() == AuthorizationDecision.PERMIT) {
					isAuthorised = true;
				} else {
					System.out.println("Decision DENY - READAttr");
					// action not authorised, executing obligation actions
					if (res.getObligations(ObligationType.BEFORE).size() > 0) {
						executeActions(node, res.getObligations(ObligationType.BEFORE));
					}
					if (res.getObligations(ObligationType.AFTER).size() > 0) {
						executeActions(node, res.getObligations(ObligationType.AFTER));
					}
					Thread.sleep(100);
				}
			}

			Object result = null;
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}

			// execution of action read
			result = node.readAttribute(name);

			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}

			return result;

		} catch (IOException e) {
			e.printStackTrace();
			throw new InterruptedException();
		}
	}

	@Override
	public boolean updateAttribute(String name, Object value) throws InterruptedException {
		AuthorizationResponse res = null;
		boolean isAuthorised = false;
		try {
			while (!isAuthorised) {

				this.lock.lock();
				/*
				 * Subject and Object identifiers and interfaces are the same
				 */
				AuthorizationRequest req = new AuthorizationRequest(this.node.getName(), this.node.getName(),
						ActionThisID.UPD, null, null, getAttributes(node.getInterface()),
						getAttributes(node.getInterface()));
				res = evaluateRequestOnState(req);
				updatePolicState(req);
				this.lock.unlock();

				if (res.getDecision() == AuthorizationDecision.PERMIT) {
					isAuthorised = true;
				} else {
					System.out.println("Decision DENY - UPDATEAttr");
					// action not authorised, executing obligation actions
					if (res.getObligations(ObligationType.BEFORE).size() > 0) {
						executeActions(node, res.getObligations(ObligationType.BEFORE));
					}
					if (res.getObligations(ObligationType.AFTER).size() > 0) {
						executeActions(node, res.getObligations(ObligationType.AFTER));
					}
					Thread.sleep(100);
				}
			}

			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}

			// execution of action upd
			node.updateAttribute(name, value);

			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			throw new InterruptedException();
		}
	}

	/* ################################################################# */

	/*
	 * ---> Group-oriented remote authorization predicate
	 */

	@Override
	public void acceptGroupPut(PointToPoint from, int session, GroupPredicate groupPredicate, Tuple tuple)
			throws IOException, InterruptedException {
		this.lock.lock();
		Attribute[] subAttributes = node.sendAttributeRequest(from);
		AuthorizationRequest req = new AuthorizationRequest(
				// Subject
				from.getName(),
				// Object
				this.node.getName(), ActionThisID.ACCEPT_PUT, tuple, from,
				// Subject
				subAttributes,
				// Object
				getAttributes(node.getInterface()));
		AuthorizationResponse res = evaluateRequestOnState(req);
		updatePolicState(req);
		this.lock.unlock();
		if (res.getDecision() == AuthorizationDecision.PERMIT) {
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			// execution of action fresh
			node.gPut(from, session, groupPredicate, tuple);
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		} else {
			// action not authorised, obligation actions executed
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		}
	}

	@Override
	public Tuple acceptGroupGet(PointToPoint from, int session, GroupPredicate groupPredicate, Template template)
			throws IOException, InterruptedException {
		this.lock.lock();
		Attribute[] subAttributes = node.sendAttributeRequest(from);
		AuthorizationRequest req = new AuthorizationRequest(
				// Subject
				from.getName(),
				// Object
				this.node.getName(), ActionThisID.ACCEPT_GET, template, from,
				// Subject
				subAttributes,
				// Object
				getAttributes(node.getInterface()));
		AuthorizationResponse res = evaluateRequestOnState(req);
		updatePolicState(req);
		this.lock.unlock();
		Tuple t = new Tuple();
		if (res.getDecision() == AuthorizationDecision.PERMIT) {
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			// execution of action fresh
			t = node.gGet(from, session, groupPredicate, template);
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		} else {
			// action not authorised, obligation actions executed
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		}
		return t;
	}

	@Override
	public Tuple acceptGroupQuery(PointToPoint from, int session, GroupPredicate groupPredicate, Template template)
			throws IOException, InterruptedException {
		this.lock.lock();
		Attribute[] subAttributes = node.sendAttributeRequest(from);
		AuthorizationRequest req = new AuthorizationRequest(
				// Subject
				from.getName(),
				// Object
				this.node.getName(), ActionThisID.ACCEPT_QRY, template, from,
				// Subject
				subAttributes,
				// Object
				getAttributes(node.getInterface()));
		AuthorizationResponse res = evaluateRequestOnState(req);
		updatePolicState(req);
		this.lock.unlock();
		Tuple t = new Tuple();
		if (res.getDecision() == AuthorizationDecision.PERMIT) {
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			// execution of action fresh
			t = node.gQuery(from, session, groupPredicate, template);
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		} else {
			// action not authorised, obligation actions executed
			if (res.getObligations(ObligationType.BEFORE).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.BEFORE));
			}
			if (res.getObligations(ObligationType.AFTER).size() > 0) {
				executeActions(node, res.getObligations(ObligationType.AFTER));
			}
		}
		return t;
	}

}
