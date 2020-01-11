package carpet_client.network;

import carpet.CarpetServer;
import carpet.helpers.TickSpeed;
import carpet.CarpetSettings;
import carpet.settings.ParsedRule;
import carpet.utils.Messenger;
import carpet_client.utils.CarpetSettingsServerNetworkHandler;
import carpet_client.utils.Reference;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
