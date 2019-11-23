package carpet_client.utils;

import carpet_client.gui.ConfigScreen;
import carpet_client.network.ClientMessageHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CarpetRules
{
    public static PacketByteBuf data;
    private static final Map<String, CarpetSettingEntry> rules = new HashMap<>();
    
    public static CarpetSettingEntry getRule(String rule)
    {
        return rules.get(rule);
    }
    
    public static boolean hasRule(String rule)
    {
        return rules.containsKey(rule);
    }
    
    public static void setAllData(PacketByteBuf buf)
    {
        CarpetRules.data = buf;
        split();
        editClientRules();
    }
    
    private static void editClientRules()
    {
    
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
            String defaultOption = ruleNBT.getString("default");
            if (hasRule(rule))
                getRule(rule).update(value, null, defaultOption);
            else
                rules.put(rule, new CarpetSettingEntry(rule, value, null, defaultOption));
        }
        ConfigScreen.setCarpetServerVersion(carpetServerVersion);
        Reference.isCarpetServer = true;
    }
    
    public static void ruleData(PacketByteBuf data)
    {
        String rule = data.readString();
        String newValue = data.readString();
        getRule(rule).changeRule(newValue);
    }
    
    public static void ruleChangeBoolean(String rule, String newValue, MinecraftClient client)
    {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeVarInt(Reference.RULE_REQUEST);
        data.writeString(rule);
        data.writeString(newValue);
        ClientMessageHandler.sendPacket(data, client);
    }
    
    public static void resetRule(String rule)
    {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeVarInt(Reference.RULE_REQUEST);
        //data.writeVarInt(Reference.RESET_RULE);
        data.writeString(rule);
        //ClientMessageHandler.sendPacket(data);
    }
    
    public static void requestUpdate()
    {
        PacketByteBuf sender = new PacketByteBuf(Unpooled.buffer());
        sender.writeVarInt(Reference.ALL_GUI_INFO);
        //ClientMessageHandler.sendPacket(sender);
    }
    
    public static ArrayList<CarpetSettingEntry> getAllRules()
    {
        ArrayList<CarpetSettingEntry> res = new ArrayList<>();
        for (String rule : rules.keySet().stream().sorted().collect(Collectors.toList()))
        {
            res.add(rules.get(rule));
        }
        return res;
    }
    
    public static class CarpetSettingEntry
    {
        private String rule;
        private String currentOption;
        private String defaultOption;
        private boolean isNumber;
        private boolean isSomethingElse;
        private String[] options;
        private int integer;
        private float flt;
        private boolean bool;
    
        public CarpetSettingEntry(String rule, String currentOption, String[] options, String defaultOption)
        {
            this.rule = rule;
            this.update(currentOption, options, defaultOption);
        }
    
        public void update(String currentOption, String[] options, String defaultOption)
        {
            this.currentOption = currentOption;
            this.options = options;
            this.defaultOption = defaultOption;
            this.checkValues();
            editClientRules();
        }
    
        private void checkValues()
        {
            this.bool = Boolean.parseBoolean(this.currentOption);
        
            try
            {
                this.integer = Integer.parseInt(this.currentOption);
            }
            catch (NumberFormatException e)
            {
                this.integer = 0;
            }
        
            try
            {
                this.flt = Float.parseFloat(this.currentOption);
                this.isNumber = true;
            }
            catch (NumberFormatException e)
            {
                this.isNumber = false;
                this.flt = 0.0F;
            }
        }
        
        public String getRule()
        {
            return rule;
        }
        
        public String getCurrentOption()
        {
            return currentOption;
        }
        
        public boolean isNumber()
        {
            return isNumber;
        }
    
        public void changeRule(String change)
        {
            this.currentOption = change;
            this.checkValues();
            editClientRules();
        }
        
        public boolean getBoolean() {
            return bool;
        }
        
        public String getDefaultOption()
        {
            return this.defaultOption;
        }
    }
}
