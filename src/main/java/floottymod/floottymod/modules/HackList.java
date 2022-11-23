package floottymod.floottymod.modules;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.modules.combat.ClickAura;
import floottymod.floottymod.modules.combat.Critical;
import floottymod.floottymod.modules.combat.KillAura;
import floottymod.floottymod.modules.macro.AutoFish;
import floottymod.floottymod.modules.movement.Flight;
import floottymod.floottymod.modules.movement.Teleport;
import floottymod.floottymod.modules.movement.ToggleSprint;
import floottymod.floottymod.modules.player.*;
import floottymod.floottymod.modules.qol.ReplaceCrop;
import floottymod.floottymod.modules.render.Coordinates;
import floottymod.floottymod.modules.render.Hud;
import floottymod.floottymod.modules.render.XRay;

import java.util.ArrayList;
import java.util.List;

public class HackList implements UpdateListener {
    public final KillAura killAura = new KillAura();
    public final ClickAura clickAura = new ClickAura();
    public final Critical critical = new Critical();
    public final Flight flight = new Flight();
    public final ToggleSprint toggleSprint = new ToggleSprint();
    public final NoFall noFall = new NoFall();
    public final NoKnockback noKnockback = new NoKnockback();
    public final FastBreak fastBreak = new FastBreak();
    public final AutoFish autoFish = new AutoFish();
    public final ReplaceCrop replaceCrop = new ReplaceCrop();
    public final Teleport teleport = new Teleport();
    public final NightVision nightVision = new NightVision();
    public final AntiHunger antiHunger = new AntiHunger();
    public final XRay xRay = new XRay();
    public final Coordinates coordinates = new Coordinates();
    public final Hud hud = new Hud();

    private final List<Hack> hacks = new ArrayList<>();

    private final EventManager eventManager = FloottyMod.INSTANCE.getEventManager();

    public HackList() {
        addModule(killAura);
        addModule(clickAura);
        addModule(critical);
        addModule(flight);
        addModule(toggleSprint);
        addModule(noFall);
        addModule(noKnockback);
        addModule(fastBreak);
        addModule(autoFish);
        addModule(replaceCrop);
        addModule(teleport);
        addModule(nightVision);
        addModule(antiHunger);
        addModule(xRay);
        addModule(coordinates);
        addModule(hud);
        eventManager.add(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        eventManager.remove(UpdateListener.class, this);
    }

    public List<Hack> getAllMods() {
        return hacks;
    }

    public List<Hack> getAllModsByCategory(Category category) {
        return getAllMods().stream().filter(m -> m.getCategory() == category).toList();
    }

    public List<Hack> getModsEnabled() {
        return getAllMods().stream().filter(m -> m.isEnabled()).toList();
    }

    public int countMods() {
        return hacks.size();
    }

    private void addModule(Hack hack) {
        hacks.add(hack);
    }
}
