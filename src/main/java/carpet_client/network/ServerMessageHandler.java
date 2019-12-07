package carpet_client.network;

import carpet.CarpetServer;
import carpet.helpers.TickSpeed;
import carpet.settings.CarpetSettings;
import carpet.settings.ParsedRule;
import carpet.utils.Messenger;
import carpet_client.utils.Reference;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
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
            sendGUIInfo(sender);
        else if (id == Reference.RULE_REQUEST)
            sendRule(sender, data);
    }
    
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
            ruleNBT.putString("description", rule.description);
            ruleNBT.putString("default", rule.defaultAsString);
            ruleNBT.putString("type", rule.type.toString());
            ListTag extraList = new ListTag();
            if (rule.extraInfo != null)
            {
                for (String extra : rule.extraInfo)
                    extraList.add(StringTag.of(extra));
            }
            ruleNBT.put("extraInfo", extraList);
            rulesList.add(ruleNBT);
        }
        data.put("rules", rulesList);
        PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packetBuf.writeVarInt(Reference.ALL_GUI_INFO);
        packetBuf.writeCompoundTag(data);
        playerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(Reference.CARPET_CHANNEL_NAME, packetBuf));
    }
    
    private static void sendRule(ServerPlayerEntity player, PacketByteBuf data)
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
}
