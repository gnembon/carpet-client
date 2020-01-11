package carpet_client.gui.entries;

import carpet.settings.ParsedRule;
import carpet_client.gui.ConfigListWidget;
import carpet_client.gui.ServerRulesScreen;
import carpet_client.utils.CarpetSettingsClientNetworkHandler;
import carpet_client.utils.CarpetSettingsServerNetworkHandler;
import carpet_client.utils.ITooltipEntry;
import carpet_client.utils.RenderHelper;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BooleanListEntry extends ConfigListWidget.Entry implements ITooltipEntry
{
    private final ParsedRule<?> settings;
    private final String rule;
    private final ButtonWidget infoButton;
    private final ButtonWidget editButton;
    private final ButtonWidget resetButton;
    private final MinecraftClient client;
    private final ServerRulesScreen gui;
    
    public BooleanListEntry(final ParsedRule<?> settings, MinecraftClient client, ServerRulesScreen gui)
    {
        this.settings = settings;
        this.client = client;
        this.gui = gui;
        this.rule = settings.name;
        this.infoButton = new ButtonWidget(0, 0, 14, 20, "i", (button -> {
            button.active = false;
        }));
        this.editButton = new ButtonWidget(0, 0, 100, 20, settings.getAsString(), (buttonWidget) -> {
            String invertedBoolean = String.valueOf(!Boolean.parseBoolean(buttonWidget.getMessage()));
            CarpetSettingsServerNetworkHandler.ruleChange(settings.name, invertedBoolean, client);
            buttonWidget.setMessage(invertedBoolean);
        });
        this.resetButton = new ButtonWidget(0, 0, 50, 20, I18n.translate("controls.reset"), (buttonWidget) -> {
            CarpetSettingsServerNetworkHandler.ruleChange(settings.name, settings.defaultAsString, client);
            this.editButton.setMessage(settings.defaultAsString);
        });
    }
    
    @Override
    public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta)
    {
        TextRenderer font = client.textRenderer;
        float fontX = (float)(x + 90 - ConfigListWidget.length);
        float fontY = (float)(y + height / 2 - 9 / 2);
        font.draw(this.rule, fontX, fontY, 16777215);
        
        this.resetButton.x = x + 290;
        this.resetButton.y = y;
        this.resetButton.active = !this.settings.getAsString().equals(this.settings.defaultAsString);
        
        this.editButton.x = x + 180;
        this.editButton.y = y;
        
        this.infoButton.x = x + 156;
        this.infoButton.y = y;
        
        this.infoButton.render(mouseX, mouseY, delta);
        this.editButton.render(mouseX, mouseY, delta);
        this.resetButton.render(mouseX, mouseY, delta);
    }
    
    @Override
    public List<? extends Element> children()
    {
        return ImmutableList.of(this.infoButton ,this.editButton, this.resetButton);
    }
    
    @Override
    public void drawTooltip(int slotIndex, int x, int y, int mouseX, int mouseY, int listWidth, int listHeight, int slotWidth, int slotHeight, float partialTicks)
    {
        if (this.infoButton.isHovered() && !this.infoButton.active)
        {
            String description = this.settings.description;
            RenderHelper.drawGuiInfoBox(client.textRenderer, description, mouseY + 5, listWidth, slotWidth, listHeight, 48);
        }
    }
}
