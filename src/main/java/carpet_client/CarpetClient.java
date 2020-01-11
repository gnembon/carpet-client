package carpet_client;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet_client.network.ServerMessageHandler;
import carpet_client.utils.CarpetSettingsClientNetworkHandler;
import carpet_client.utils.CarpetSettingsServerNetworkHandler;
import carpet_client.utils.Reference;
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
    public String version()
    {
        return "carpet-client";
    }

    @Override
    public void onGameStarted()
    {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(CarpetClientSettings.class);
        CarpetServer.settingsManager.addRuleObserver((source, parsedRule, s) -> {
            try
            {
                CarpetSettingsServerNetworkHandler.updateCarpetClientRules(parsedRule.name, parsedRule.getAsString(), source.getPlayer());
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
        Reference.isCarpetClientPresent = true;
        CarpetSettingsClientNetworkHandler.attachServer(server);
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
        CarpetSettingsServerNetworkHandler.sendGUIInfo(player);
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player)
    {
        //
    }
}
