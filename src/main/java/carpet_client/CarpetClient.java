package carpet_client;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet_client.network.ServerMessageHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
        CarpetServer.settingsManager.addRuleObserver((source, parsedRule, s) -> {
            try
            {
                ServerMessageHandler.updateCarpetClientRules(parsedRule.name, parsedRule.getAsString(), source.getPlayer());
            }
            catch (CommandSyntaxException e)
            {
                e.printStackTrace();
            }
        });
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
        ServerMessageHandler.sendGUIInfo(player);
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player)
    {
        //
    }
}
