package model;

import java.util.Set;
import java.util.HashSet;

public abstract class Subject {

	protected Set<Observer> observers = new HashSet<>();

	protected void notifyObservers() {
		for (Observer observer : observers) {
			observer.update(this);
		}
	}

	protected void notifyObservers(Object data) {
		for (Observer observer : observers) {
			observer.update(this, data);
		}
	}

	public void attach(Observer observer) {
		observers.add(observer);
	}

	public void detach(Observer observer) {
		observers.remove(observer);
	}
}
