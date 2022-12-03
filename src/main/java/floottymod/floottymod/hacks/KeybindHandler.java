package floottymod.floottymod.hacks;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.ui.screens.clickgui.ClickGui;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    private static Key UNKNOWN_KEY = InputUtil.Type.KEYSYM.createFromCode(-1);

    public static final String KEY_CATEGORY_FLOOTTYMOD = "key.category.floottymod";
    public static final String KEY_CATEGORY_FLOOTTYMOD_COMBAT = "key.category.floottymod.combat";
    public static final String KEY_CATEGORY_FLOOTTYMOD_MACRO = "key.category.floottymod.macro";
    public static final String KEY_CATEGORY_FLOOTTYMOD_MOVEMENT = "key.category.floottymod.movement";
    public static final String KEY_CATEGORY_FLOOTTYMOD_PLAYER = "key.category.floottymod.player";
    public static final String KEY_CATEGORY_FLOOTTYMOD_QOL = "key.category.floottymod.qol";
    public static final String KEY_CATEGORY_FLOOTTYMOD_RENDER = "key.category.floottymod.render";


    public static final String KEY_MENU = "key.floottymod.menu";

    public static final String KEY_CLICKAURA = "key.floottymod.clickaura";
    public static final String KEY_CRITICAL = "key.floottymod.critical";
    public static final String KEY_KILLAURA = "key.floottymod.killaura";
    public static final String KEY_CRYSTALAURA = "key.floottymod.crystalaura";
    public static final String KEY_TPAURA = "key.floottymod.tpaura";
    public static final String KEY_REACH = "key.floottymod.reach";

    public static final String KEY_AUTOFISH = "key.floottymod.autofish";
    public static final String KEY_ANTIAFK = "key.floottymod.antiafk";
    public static final String KEY_FORAGING = "key.floottymod.foraging";

    public static final String KEY_FLIGHT = "key.floottymod.flight";
    public static final String KEY_TELEPORT = "key.floottymod.teleport";
    public static final String KEY_TOGGLESPRINT = "key.floottymod.togglesprint";
    public static final String KEY_BOATFLY = "key.floottymod.boatfly";


    public static final String KEY_ANTIHUNGER = "key.floottymod.antihunger";
    public static final String KEY_FASTBREAK = "key.floottymod.fastbreak";
    public static final String KEY_NIGHTVISION = "key.floottymod.nightvision";
    public static final String KEY_NOFALL = "key.floottymod.nofall";
    public static final String KEY_NOKNOCKBACK = "key.floottymod.noknockback";

    public static final String KEY_REPLACECROP = "key.floottymod.replacecrop";

    public static final String KEY_COORDINATES = "key.floottymod.coordinates";
    public static final String KEY_HUD = "key.floottymod.hud";
    public static final String KEY_XRAY = "key.floottymod.xray";
    public static final String KEY_FREECAM = "key.floottymod.freecam";
    public static final String KEY_CHESTESP = "key.floottymod.chestesp";
    public static final String KEY_OREESP = "key.floottymod.oreesp";


    public static KeyBinding menuKey = new KeyBinding(KEY_MENU, GLFW.GLFW_KEY_RIGHT_SHIFT, KEY_CATEGORY_FLOOTTYMOD);

    public static KeyBinding clickAuraKey = new KeyBinding(KEY_CLICKAURA, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_COMBAT);
    public static KeyBinding criticalKey = new KeyBinding(KEY_CRITICAL, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_COMBAT);
    public static KeyBinding killAuraKey = new KeyBinding(KEY_KILLAURA, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_COMBAT);
    public static KeyBinding crystalAuraKey = new KeyBinding(KEY_CRYSTALAURA, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_COMBAT);
    public static KeyBinding tpAuraKey = new KeyBinding(KEY_TPAURA, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_COMBAT);
    public static KeyBinding reachKey = new KeyBinding(KEY_REACH, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_COMBAT);

    public static KeyBinding autoFishKey = new KeyBinding(KEY_AUTOFISH, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_MACRO);
    public static KeyBinding antiAfkKey = new KeyBinding(KEY_ANTIAFK, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_MACRO);
    public static KeyBinding foragingKey = new KeyBinding(KEY_FORAGING, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_MACRO);

    public static KeyBinding flightKey = new KeyBinding(KEY_FLIGHT, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_MOVEMENT);
    public static KeyBinding teleportKey = new KeyBinding(KEY_TELEPORT, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_MOVEMENT);
    public static KeyBinding toggleSprintKey = new KeyBinding(KEY_TOGGLESPRINT, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_MOVEMENT);
    public static KeyBinding boatFlyKey = new KeyBinding(KEY_BOATFLY, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_MOVEMENT);

    public static KeyBinding antiHungerKey = new KeyBinding(KEY_ANTIHUNGER, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_PLAYER);
    public static KeyBinding fastBreakKey = new KeyBinding(KEY_FASTBREAK, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_PLAYER);
    public static KeyBinding nightVisionKey = new KeyBinding(KEY_NIGHTVISION, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_PLAYER);
    public static KeyBinding noFallKey = new KeyBinding(KEY_NOFALL, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_PLAYER);
    public static KeyBinding noKnockbackKey = new KeyBinding(KEY_NOKNOCKBACK, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_PLAYER);

    public static KeyBinding replaceCropKey = new KeyBinding(KEY_REPLACECROP, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_QOL);

    public static KeyBinding coordinatesKey = new KeyBinding(KEY_COORDINATES, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_RENDER);
    public static KeyBinding hudKey = new KeyBinding(KEY_HUD, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_RENDER);
    public static KeyBinding xrayKey = new KeyBinding(KEY_XRAY, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_RENDER);
    public static KeyBinding freeCamKey = new KeyBinding(KEY_FREECAM, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_RENDER);
    public static KeyBinding chestEspKey = new KeyBinding(KEY_CHESTESP, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_RENDER);
    public static KeyBinding oreEspKey = new KeyBinding(KEY_OREESP, UNKNOWN_KEY.getCode(), KEY_CATEGORY_FLOOTTYMOD_RENDER);

    public static KeyBinding[] keyBindings = {
            menuKey,

            clickAuraKey,
            criticalKey,
            killAuraKey,
            crystalAuraKey,
            tpAuraKey,
            reachKey,

            autoFishKey,
            antiAfkKey,
            foragingKey,

            flightKey,
            teleportKey,
            toggleSprintKey,
            boatFlyKey,

            antiHungerKey,
            fastBreakKey,
            nightVisionKey,
            noFallKey,
            noKnockbackKey,

            replaceCropKey,

            coordinatesKey,
            hudKey,
            xrayKey,
            freeCamKey,
            chestEspKey,
            oreEspKey
    };

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(menuKey.wasPressed()) {
                ClickGui.INSTANCE.drawFrames = true;
                FloottyMod.INSTANCE.MC.setScreen(ClickGui.INSTANCE);
            }

            if(clickAuraKey.wasPressed()) FloottyMod.INSTANCE.getHackList().clickAura.toggle(false);
            if(criticalKey.wasPressed()) FloottyMod.INSTANCE.getHackList().critical.toggle(false);
            if(killAuraKey.wasPressed()) FloottyMod.INSTANCE.getHackList().killAura.toggle(false);
            if(crystalAuraKey.wasPressed()) FloottyMod.INSTANCE.getHackList().crystalAura.toggle(false);
            if(tpAuraKey.wasPressed()) FloottyMod.INSTANCE.getHackList().tpAura.toggle(false);
            if(reachKey.wasPressed()) FloottyMod.INSTANCE.getHackList().reach.toggle(false);

            if(autoFishKey.wasPressed()) FloottyMod.INSTANCE.getHackList().autoFish.toggle(false);
            if(antiAfkKey.wasPressed()) FloottyMod.INSTANCE.getHackList().antiAfk.toggle(false);
            if(foragingKey.wasPressed()) FloottyMod.INSTANCE.getHackList().foraging.toggle(false);

            if(flightKey.wasPressed()) FloottyMod.INSTANCE.getHackList().flight.toggle(false);
            if(teleportKey.wasPressed()) FloottyMod.INSTANCE.getHackList().teleport.toggle(true);
            if(toggleSprintKey.wasPressed()) FloottyMod.INSTANCE.getHackList().toggleSprint.toggle(false);
            if(boatFlyKey.wasPressed()) FloottyMod.INSTANCE.getHackList().boatFly.toggle(false);

            if(antiHungerKey.wasPressed()) FloottyMod.INSTANCE.getHackList().antiHunger.toggle(false);
            if(fastBreakKey.wasPressed()) FloottyMod.INSTANCE.getHackList().fastBreak.toggle(false);
            if(nightVisionKey.wasPressed()) FloottyMod.INSTANCE.getHackList().nightVision.toggle(false);
            if(noFallKey.wasPressed()) FloottyMod.INSTANCE.getHackList().noFall.toggle(false);
            if(noKnockbackKey.wasPressed()) FloottyMod.INSTANCE.getHackList().noKnockback.toggle(false);

            if(replaceCropKey.wasPressed()) FloottyMod.INSTANCE.getHackList().replaceCrop.toggle(false);

            if(coordinatesKey.wasPressed()) FloottyMod.INSTANCE.getHackList().coordinates.toggle(false);
            if(hudKey.wasPressed()) FloottyMod.INSTANCE.getHackList().hud.toggle(false);
            if(xrayKey.wasPressed()) FloottyMod.INSTANCE.getHackList().xray.toggle(false);
            if(freeCamKey.wasPressed()) FloottyMod.INSTANCE.getHackList().freeCam.toggle(false);
            if(chestEspKey.wasPressed()) FloottyMod.INSTANCE.getHackList().chestEsp.toggle(false);
            if(oreEspKey.wasPressed()) FloottyMod.INSTANCE.getHackList().oreEsp.toggle(false);
        });
    }

    public static void register() {
        for(KeyBinding binding : keyBindings) KeyBindingHelper.registerKeyBinding(binding);
        registerKeyInputs();
    }

}
