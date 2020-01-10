package carpet_client.utils;

import carpet.CarpetServer;
import carpet_client.gui.ConfigScreen;
import carpet_client.network.ClientMessageHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.PacketByteBuf;

public class CarpetRules
{
    public static PacketByteBuf data;
    private static MinecraftServer server;
    
    public static void setAllData(PacketByteBuf buf)
    {
        CarpetRules.data = buf;
        split();
    }
    
    public static void attachServer(MinecraftServer server)
    {
        CarpetRules.server = server;
    }
    
    private static void split()
    {
        CompoundTag compound = data.readCompoundTag();
        if (compound == null) return;
        String carpetServerVersion = compound.getString("CarpetVersion");
    
        ListTag rulesList = compound.getList("rules", 10);
        for (Tag tag : rulesList)
        {
            CompoundTag ruleNBT = (CompoundTag) tag;
            String rule = ruleNBT.getString("rule");
            String value = ruleNBT.getString("value");
            CarpetServer.settingsManager.getRule(rule).set(server.getCommandSource(), value);
        }
        ConfigScreen.setCarpetServerVersion(carpetServerVersion);
        Reference.isCarpetServer = true;
    }
    
    public static void ruleData(PacketByteBuf data)
    {
        String rule = data.readString();
        String newValue = data.readString();
        CarpetServer.settingsManager.getRule(rule).set(server.getCommandSource(), newValue);
    }
    
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
