/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import java.io.Serializable;

/**
 * Every UI event in the Speaker application needs to implement this interface.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
public interface SpeakerEvent extends Serializable {

	/**
	 * The object / component which dispatched the vent. Needed to prevent
	 * event-loops.
	 * 
	 * @return
	 */
	public Object getEventSource();

	/**
	 * Sets the object which dispatched the event.
	 * 
	 * @param eventSource
	 */
	public void setEventSource(Object eventSource);

}
