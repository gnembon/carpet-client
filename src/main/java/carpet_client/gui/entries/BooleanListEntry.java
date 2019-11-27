package carpet_client.gui.entries;

import carpet_client.gui.ConfigListWidget;
import carpet_client.gui.ServerRulesScreen;
import carpet_client.utils.CarpetRules;
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
public class BooleanListEntry extends ConfigListWidget.Entry
{
    private final CarpetRules.CarpetSettingEntry settings;
    private final String rule;
    private final ButtonWidget infoButton;
    private final ButtonWidget editButton;
    private final ButtonWidget resetButton;
    private final MinecraftClient client;
    private final ServerRulesScreen gui;
    
    public BooleanListEntry(final CarpetRules.CarpetSettingEntry settings, MinecraftClient client, ServerRulesScreen gui)
    {
        this.settings = settings;
        this.client = client;
        this.gui = gui;
        this.rule = settings.getRule();
        this.infoButton = new ButtonWidget(0, 0, 14, 20, "i", (button -> {
            System.out.println("Description!!");
        }));
        this.editButton = new ButtonWidget(0, 0, 100, 20, settings.getCurrentOption(), (buttonWidget) -> {
            String invertedBoolean = String.valueOf(!Boolean.parseBoolean(buttonWidget.getMessage()));
            CarpetRules.ruleChange(settings.getRule(), invertedBoolean, client);
            buttonWidget.setMessage(invertedBoolean);
        });
        this.resetButton = new ButtonWidget(0, 0, 50, 20, I18n.translate("controls.reset"), (buttonWidget) -> {
            CarpetRules.ruleChange(settings.getRule(), settings.getDefaultOption(), client);
            this.editButton.setMessage(settings.getDefaultOption());
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
        this.resetButton.active = !this.settings.getCurrentOption().equals(this.settings.getDefaultOption());
        
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
}
