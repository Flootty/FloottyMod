package floottymod.floottymod.event;

import floottymod.floottymod.FloottyMod;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class EventManager {
    private final FloottyMod client;
    private final HashMap<Class<? extends Listener>, ArrayList<? extends Listener>> listenerMap = new HashMap<>();

    public EventManager(FloottyMod client) {
        this.client = client;
    }

    public static <L extends Listener, E extends Event<L>> void fire(E event) {
        EventManager eventManager = FloottyMod.INSTANCE.getEventManager();
        if(eventManager == null) return;

        eventManager.fireImpl(event);
    }

    private <L extends Listener, E extends Event<L>> void fireImpl(E event) {
        if(!client.isEnabled()) return;

        try {
            Class<L> type = event.getListenerType();
            @SuppressWarnings("unchecked")
            ArrayList<L> listeners = (ArrayList<L>)listenerMap.get(type);

            if(listeners == null || listeners.isEmpty()) return;
            ArrayList<L> listeners2 = new ArrayList<>(listeners);
            listeners2.removeIf(Objects::isNull);

            event.fire(listeners2);
        } catch(Throwable e) {
            e.printStackTrace();

            CrashReport report = CrashReport.create(e, "Firing event");
            CrashReportSection section = report.addElement("Affected event");
            section.add("Event class", () -> event.getClass().getName());

            throw new CrashException(report);
        }
    }

    public <L extends Listener> void add(Class<L> type, L listener) {
        try {
            @SuppressWarnings("unchecked")
            ArrayList<L> listeners = (ArrayList<L>)listenerMap.get(type);

            if(listeners == null) {
                listeners = new ArrayList<>(Arrays.asList(listener));
                listenerMap.put(type, listeners);
                return;
            }
            listeners.add(listener);
        } catch(Throwable e) {
            e.printStackTrace();

            CrashReport report = CrashReport.create(e, "Adding event listener");
            CrashReportSection section = report.addElement("Affected listener");
            section.add("Listener type", () -> type.getName());
            section.add("Listener class", () -> listener.getClass().getName());

            throw new CrashException(report);
        }
    }

    public <L extends Listener> void remove(Class<L> type, L listener) {
        try {
            @SuppressWarnings("unchecked")
            ArrayList<L> listeners = (ArrayList<L>)listenerMap.get(type);
            if(listeners != null) listeners.remove(listener);
        } catch(Throwable e) {
            e.printStackTrace();

            CrashReport report = CrashReport.create(e, "Removing event listener");
            CrashReportSection section = report.addElement("Affected listener");
            section.add("Listener type", () -> type.getName());
            section.add("Listener class", () -> listener.getClass().getName());

            throw new CrashException(report);
        }
    }
}
