package floottymod.floottymod.hack;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.hacks.combat.*;
import floottymod.floottymod.hacks.macro.AntiAfk;
import floottymod.floottymod.hacks.macro.AutoFish;
import floottymod.floottymod.hacks.macro.Foraging;
import floottymod.floottymod.hacks.macro.Mining;
import floottymod.floottymod.hacks.movement.*;
import floottymod.floottymod.hacks.player.*;
import floottymod.floottymod.hacks.qol.AutoReconnect;
import floottymod.floottymod.hacks.qol.AutoTool;
import floottymod.floottymod.hacks.qol.ReplaceCrop;
import floottymod.floottymod.hacks.render.*;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

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
    public final Xray xray = new Xray();
    public final Coordinates coordinates = new Coordinates();
    public final Hud hud = new Hud();
    public final CrystalAura crystalAura = new CrystalAura();
    public final AntiAfk antiAfk = new AntiAfk();
    public final FreeCam freeCam = new FreeCam();
    public final BoatFly boatFly = new BoatFly();
    public final TpAura tpAura = new TpAura();
    public final Reach reach = new Reach();
    public final ChestEsp chestEsp = new ChestEsp();
    public final OreEsp oreEsp = new OreEsp();
    public final Foraging foraging = new Foraging();
    public final Mining mining = new Mining();
    public final CreativeFlight creativeFlight = new CreativeFlight();
    public final AutoTool autoTool = new AutoTool();
    public final AutoReconnect autoReconnect = new AutoReconnect();

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
        addModule(xray);
        addModule(coordinates);
        addModule(hud);
        addModule(crystalAura);
        addModule(antiAfk);
        addModule(freeCam);
        addModule(boatFly);
        addModule(tpAura);
        addModule(reach);
        addModule(chestEsp);
        addModule(oreEsp);
        addModule(foraging);
        addModule(mining);
        addModule(creativeFlight);
        addModule(autoTool);
        addModule(autoReconnect);

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
