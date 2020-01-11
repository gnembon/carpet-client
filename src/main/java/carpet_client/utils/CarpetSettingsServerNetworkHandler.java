package carpet_client.utils;

import carpet_client.network.ClientMessageHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.PacketByteBuf;

public class CarpetSettingsServerNetworkHandler
{
    public static void ruleChange(String rule, String newValue, MinecraftClient client)
    {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeVarInt(Reference.RULE_REQUEST);
        data.writeString(rule);
        data.writeString(newValue);
        ClientMessageHandler.sendPacket(data, client);
    }
    
    public static void requestUpdate(MinecraftClient client)
    {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeVarInt(Reference.ALL_GUI_INFO);
        ClientMessageHandler.sendPacket(data, client);
    }
}