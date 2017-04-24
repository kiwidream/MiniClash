package com.redream.ld38;

import java.util.LinkedList;

public class PriorityList extends LinkedList<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void add(Comparable<Object> object) {
		for (int i=0; i<size(); i++) {
			if (object.compareTo(get(i)) <= 0) {
			  add(i, object);
			  return;
			}
		}
		addLast(object);
	}
}
