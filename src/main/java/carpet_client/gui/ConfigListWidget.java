package carpet_client.gui;

import carpet_client.gui.entries.BooleanListEntry;
import carpet_client.gui.entries.NumberListEntry;
import carpet_client.gui.entries.StringListEntry;
import carpet_client.utils.CarpetRules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.*;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public class ConfigListWidget extends ElementListWidget<ConfigListWidget.Entry>
{
    private ServerRulesScreen gui;
    public static int length;
    
    public ConfigListWidget(ServerRulesScreen gui, MinecraftClient client)
    {
        super(client, gui.width + 45, gui.height, 43, gui.height - 32, 20);
        this.gui = gui;
        ArrayList<CarpetRules.CarpetSettingEntry> rules = CarpetRules.getAllRules();
        for (CarpetRules.CarpetSettingEntry r : rules)
        {
            int i = client.textRenderer.getStringWidth(r.getRule());
            if (i > length)
            {
                length = i;
            }
            if (r.isBool())
                this.addEntry(new BooleanListEntry(r, client, gui));
            else if (r.isInteger() || r.isDouble())
                this.addEntry(new NumberListEntry(r, client, gui));
            else
                this.addEntry(new StringListEntry(r, client, gui));
        }
    }
    
    @Override
    protected int getScrollbarPosition()
    {
        return this.width / 2 + getRowWidth() / 2 + 4;
    }
    
    @Override
    public int getRowWidth()
    {
        return 180 * 2;
    }
    
    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ElementListWidget.Entry<ConfigListWidget.Entry>
    {
    
    }
}
