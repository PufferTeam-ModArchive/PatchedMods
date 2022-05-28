package letiu.modbase.events;

import java.util.ArrayList;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import letiu.modbase.events.IArrowEventListener;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class ArrowEventHandler {
	
	private ArrayList<IArrowEventListener> listeners = new ArrayList<IArrowEventListener>();
	
	public void addListener(IArrowEventListener listener) {
		this.listeners.add(listener);
	}
	
	@SubscribeEvent
	public void handleEvent(ArrowNockEvent event) {
		for (IArrowEventListener listener : listeners) {
			listener.handleNockEvent(event);
		}
	}
	
	@SubscribeEvent
	public void handleEvent(ArrowLooseEvent event) {
		for (IArrowEventListener listener : listeners) {
			listener.handleLooseEvent(event);
		}
	}
}
