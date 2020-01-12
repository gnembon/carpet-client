package carpet_client.utils;

import carpet.CarpetServer;
import carpet.CarpetSettings;
import carpet.helpers.TickSpeed;
import carpet.settings.ParsedRule;
import carpet.utils.Messenger;
import carpet_client.network.ClientMessageHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;

public class CarpetSettingsServerNetworkHandler
{
    public static void sendGUIInfo(ServerPlayerEntity playerEntity)
    {
        CompoundTag data = new CompoundTag();
        
        data.putString("CarpetVersion", CarpetSettings.carpetVersion);
        data.putFloat("Tickrate", TickSpeed.tickrate);
        
        ListTag rulesList = new ListTag();
        for (ParsedRule<?> rule : CarpetServer.settingsManager.getRules())
        {
            CompoundTag ruleNBT = new CompoundTag();
            
            ruleNBT.putString("rule" ,rule.name);
            ruleNBT.putString("value", rule.getAsString());
            ruleNBT.putString("default", rule.defaultAsString);
            rulesList.add(ruleNBT);
        }
        data.put("rules", rulesList);
        PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packetBuf.writeVarInt(Reference.ALL_GUI_INFO);
        packetBuf.writeCompoundTag(data);
        playerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(Reference.CARPET_CHANNEL_NAME, packetBuf));
    }
    
    public static void sendRule(ServerPlayerEntity player, PacketByteBuf data)
    {
        String rule = data.readString();
        String newValue = data.readString();
        if (player.allowsPermissionLevel(2))
        {
            CarpetServer.settingsManager.getRule(rule).set(player.getCommandSource(), newValue);
            Messenger.m(player.getCommandSource(), "w " + rule + ": " + newValue + ", ", "c [change permanently?]",
                    "^w Click to keep the settings in carpet.conf to save across restarts",
                    "?/carpet setDefault " + rule + " " + newValue);
        }
        else
        {
            Messenger.m(player, "r You do not have permissions to change the rules.");
        }
    }
    
    public static void updateCarpetClientRules(String rule, String newValue, ServerPlayerEntity player)
    {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        
        data.writeVarInt(Reference.CHANGE_RULE);
        data.writeString(rule);
        data.writeString(newValue);
        player.networkHandler.sendPacket(new CustomPayloadS2CPacket(Reference.CARPET_CHANNEL_NAME, data));
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