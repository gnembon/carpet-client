package carpet_client.network;

import carpet_client.utils.CarpetSettingsServerNetworkHandler;
import carpet_client.utils.Reference;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class ServerMessageHandler
{
    public static void receivedPacket(Identifier channel, PacketByteBuf buf, ServerPlayerEntity player)
    {
        if (Reference.CARPET_CHANNEL_NAME.equals(channel) && buf != null)
            handleData(player, buf);
    }
    
    private static void handleData(ServerPlayerEntity sender, PacketByteBuf data)
    {
        int id = data.readVarInt();
        
        if (id == Reference.ALL_GUI_INFO)
            CarpetSettingsServerNetworkHandler.sendGUIInfo(sender);
        else if (id == Reference.RULE_REQUEST)
            CarpetSettingsServerNetworkHandler.sendRule(sender, data);
    }
}
