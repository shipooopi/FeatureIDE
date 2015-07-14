/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2015  FeatureIDE team, University of Magdeburg, Germany
 *
 * This file is part of FeatureIDE.
 * 
 * FeatureIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * FeatureIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FeatureIDE.  If not, see <http://www.gnu.org/licenses/>.
 *
 * See http://featureide.cs.ovgu.de/ for further information.
 */
package de.ovgu.featureide.fm.core;

import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A generic class to provide the observer pattern for listening object events. Using this class, listeners can be registered or removed, and an action of type <b>T</b>
 * can be fired (which is broadcasted to all all registered listeners).
 * <br/><br/><b>Example</b>
<pre>
<code>
 public class PropertyChangedEventListener extends EventListener<PropertyChangeListener> {

	public void fireEventForListeners(final PropertyChangeEvent event) {	
		super.forEach(listener -> {listener.propertyChange(event)});
	}
}

public class MyClass {
	private PropertyChangedEventListener eventListeners = new PropertyChangedEventListener();
	public PropertyChangedEventListener getEventListeners() { return eventListeners; }
	
	//... somewhere here, a property is changed and eventListeners.forEach(...) is invoked
}

public static void main(String... args) {
	MyClass myclass = new MyClass();
	myclass.getEventListeners().addListener(event -> { Systen.out.println(event.getPropertyName()) });
}
</code>
</pre> 
 * 
 * @author Marcus Pinnecke, 2015/July/14
 */
public class EventListener<T> implements Iterable<T> {
	
	private LinkedList<T> eventListeners = new LinkedList<>();

	/**
	 * @param listener
	 */
	public void addListener(T listener) {
		if (!eventListeners.contains(listener))
				eventListeners.add(listener);
	}

	/**
	 * @param listener
	 */
	public void removeListener(PropertyChangeListener listener) {
		eventListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return eventListeners.iterator();
	}
	
	public interface Action<T> {
		void invoke(final T object);
	}
	
	public void forEach(final Action<T> actionForSingleObject) {
		for (T t : eventListeners)
			actionForSingleObject.invoke(t);
	}

}
