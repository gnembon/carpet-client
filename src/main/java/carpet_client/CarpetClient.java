package carpet_client;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet_client.network.MessageHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class CarpetClient implements CarpetExtension
{
    public static void noop() { }
    static
    {
        CarpetServer.manageExtension(new CarpetClient());
    }

    @Override
    public void onGameStarted()
    {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(CarpetClientSettings.class);
    }

    @Override
    public void onServerLoaded(MinecraftServer server)
    {
        // reloading of /carpet settings is handled by carpet
        // reloading of own settings is handled as an extension, since we claim own settings manager
    }

    @Override
    public void onTick(MinecraftServer server)
    {
        // no need to add this.
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        // client commands
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player)
    {
        MessageHandler.sendCarpetInfo(player);
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player)
    {
        //
    }
}
