package carpet_client.utils;

import carpet.CarpetServer;
import carpet_client.gui.ConfigScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.PacketByteBuf;

public class CarpetSettingsClientNetworkHandler
{
    public static PacketByteBuf data;
    private static MinecraftServer server;
    
    public static void setAllData(PacketByteBuf buf)
    {
        CarpetSettingsClientNetworkHandler.data = buf;
        split();
    }
    
    public static void attachServer(MinecraftServer server)
    {
        CarpetSettingsClientNetworkHandler.server = server;
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
    
    public static void updateRule(PacketByteBuf data)
    {
        String rule = data.readString();
        String newValue = data.readString();
        CarpetServer.settingsManager.getRule(rule).set(server.getCommandSource(), newValue);
    }
}
