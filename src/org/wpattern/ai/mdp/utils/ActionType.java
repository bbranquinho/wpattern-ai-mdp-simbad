package org.wpattern.ai.mdp.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ActionType {

	NORTH("N", "NORTH"),

	WEST("W", "WEST"),

	SOUTH("S", "SOUTH"),

	EAST("E", "EAST"),

	STAY("S", "STAY");

	private static final Map<String, ActionType> map = new HashMap<String, ActionType>();

	static {
		for (ActionType value : values()) {
			map.put(value.symbol, value);
			map.put(value.fullname, value);
		}
	}

	private final String symbol;

	private final String fullname;

	private ActionType(String symbol, String fullname) {
		this.symbol = symbol;
		this.fullname = fullname;
	}

	public static ActionType parse(String action) {
		return map.get(action);
	}

	public static ActionType[] parse(List<String> values) {
		ActionType[] actions = new ActionType[values.size()];

		for (int i = 0; i < values.size(); i++) {
			actions[i] = parse(values.get(i));
		}

		return actions;
	}

	public static ActionType[] parse(String[] values) {
		ActionType[] actions = new ActionType[values.length];

		for (int i = 0; i < values.length; i++) {
			actions[i] = parse(values[i]);
		}

		return actions;
	}

}
