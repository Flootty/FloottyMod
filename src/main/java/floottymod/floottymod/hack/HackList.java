package floottymod.floottymod.hack;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.hacks.combat.ClickAura;
import floottymod.floottymod.hacks.combat.Critical;
import floottymod.floottymod.hacks.combat.CrystalAura;
import floottymod.floottymod.hacks.combat.KillAura;
import floottymod.floottymod.hacks.macro.AntiAfk;
import floottymod.floottymod.hacks.macro.AutoFish;
import floottymod.floottymod.hacks.movement.Flight;
import floottymod.floottymod.hacks.movement.Teleport;
import floottymod.floottymod.hacks.movement.ToggleSprint;
import floottymod.floottymod.hacks.player.*;
import floottymod.floottymod.hacks.qol.ReplaceCrop;
import floottymod.floottymod.hacks.render.Coordinates;
import floottymod.floottymod.hacks.render.FreeCam;
import floottymod.floottymod.hacks.render.Hud;
import floottymod.floottymod.hacks.render.XRay;

import java.nio.file.Path;
import java.util.*;

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
    public final CrystalAura crystalAura = new CrystalAura();
    public final AntiAfk antiAfk = new AntiAfk();
    public final FreeCam freeCam = new FreeCam();

    private final TreeMap<String, Hack> hacks = new TreeMap<>(String::compareToIgnoreCase);
    private final EnabledHacksFile enabledHacksFile;
    private final EventManager eventManager = FloottyMod.INSTANCE.getEventManager();

    public HackList(Path enabledHacksFile) {
        this.enabledHacksFile = new EnabledHacksFile(enabledHacksFile);

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
        addModule(crystalAura);
        addModule(antiAfk);
        addModule(freeCam);

        eventManager.add(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        enabledHacksFile.load(this);
        eventManager.remove(UpdateListener.class, this);
    }

    public void saveEnabledHacks() {
        enabledHacksFile.save(this);
    }

    public Hack getHackByName(String name) {
        return hacks.get(name);
    }

    public Collection<Hack> getAllHacks() {
        return Collections.unmodifiableCollection(hacks.values());
    }

    public List<Hack> getAllModsByCategory(Category category) {
        return getAllHacks().stream().filter(m -> m.getCategory() == category).toList();
    }

    public List<Hack> getHacksEnabled() {
        return getAllHacks().stream().filter(m -> m.isEnabled()).toList();
    }

    public int countMods() {
        return hacks.size();
    }

    private void addModule(Hack hack) {
        hacks.put(hack.getName(), hack);
    }
}
