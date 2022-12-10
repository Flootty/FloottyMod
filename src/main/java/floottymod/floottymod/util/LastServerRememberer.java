package floottymod.floottymod.util;

import floottymod.floottymod.FloottyMod;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public enum LastServerRememberer {
    ;
    private static ServerInfo lastServer;

    public static ServerInfo getLastServer()  {
        return lastServer;
    }

    public static void setLastServer(ServerInfo server)  {
        lastServer = server;
    }

    public static void reconnect(Screen prevScreen) {
        if(lastServer == null) return;

        ConnectScreen.connect(prevScreen, FloottyMod.MC, ServerAddress.parse(lastServer.address), lastServer);
    }
}
