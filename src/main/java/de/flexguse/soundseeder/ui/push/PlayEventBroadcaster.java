/**
 * 
 */
package de.flexguse.soundseeder.ui.push;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
public class PlayEventBroadcaster implements Serializable {

	private static final long serialVersionUID = 4302528592041463739L;

	static ExecutorService executorService = Executors.newSingleThreadExecutor();

	// Java 8
	private static LinkedList<Consumer<PlayEvent>> listeners = new LinkedList<Consumer<PlayEvent>>();

	public static synchronized void register(Consumer<PlayEvent> listener) {
		listeners.add(listener);
	}

	public static synchronized void unregister(Consumer<PlayEvent> listener) {
		listeners.remove(listener);
	}

	public static synchronized void broadcast(final PlayEvent playEvent) {
		for (final Consumer<PlayEvent> listener : listeners)
			executorService.execute(() -> listener.accept(playEvent));
	}
}
