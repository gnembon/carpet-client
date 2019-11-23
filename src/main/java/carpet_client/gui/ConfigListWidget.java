package carpet_client.gui;

import carpet_client.utils.CarpetRules;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.resource.language.I18n;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ConfigListWidget extends ElementListWidget<ConfigListWidget.Entry>
{
    private ServerRulesScreen gui;
    private int length;
    
    public ConfigListWidget(ServerRulesScreen gui, MinecraftClient client)
    {
        super(client, gui.width + 45, gui.height, 43, gui.height - 32, 20);
        this.gui = gui;
        ArrayList<CarpetRules.CarpetSettingEntry> rules = CarpetRules.getAllRules();
        for (CarpetRules.CarpetSettingEntry r : rules)
        {
            int i = client.textRenderer.getStringWidth(r.getRule());
            if (i > this.length)
            {
                this.length = i;
            }
            this.addEntry(new ConfigListWidget.BooleanRuleEntry(r, client));
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
    public class BooleanRuleEntry extends ConfigListWidget.Entry
    {
        private final CarpetRules.CarpetSettingEntry settings;
        private final String rule;
        private final ButtonWidget infoButton;
        private final AbstractButtonWidget editButton;
        private final ButtonWidget resetButton;
        
        private BooleanRuleEntry(final CarpetRules.CarpetSettingEntry settings, MinecraftClient client)
        {
            this.settings = settings;
            this.rule = settings.getRule();
            this.infoButton = new ButtonWidget(0, 0, 14, 20, "i", (button -> {
                System.out.println("Description!!");
            }));
            if (!settings.isNumber())
            {
                this.editButton = new ButtonWidget(0, 0, 100, 20, settings.getCurrentOption(), (buttonWidget) -> {
                    String invertedBoolean = String.valueOf(!Boolean.parseBoolean(buttonWidget.getMessage()));
                    CarpetRules.ruleChangeBoolean(settings.getRule(), invertedBoolean, client);
                    buttonWidget.setMessage(invertedBoolean);
                });
            }
            else
            {
                this.editButton = new TextFieldWidget(client.textRenderer, 0, 0, 75, 20, settings.getCurrentOption());
            }
            this.resetButton = new ButtonWidget(0, 0, 50, 20, I18n.translate("controls.reset"), (buttonWidget) -> {
                CarpetRules.resetRule(settings.getRule());
                System.out.println("Reset!!!");
            });
        }
    
        @Override
        public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta)
        {
            TextRenderer font = ConfigListWidget.this.minecraft.textRenderer;
            float fontX = (float)(x + 90 - ConfigListWidget.this.length);
            float fontY = (float)(y + height / 2 - 9 / 2);
            font.draw(this.rule, fontX, fontY, 16777215);
            this.resetButton.x = x + 290;
            this.resetButton.y = y;
            this.resetButton.active = !this.settings.getCurrentOption().equals(this.settings.getDefaultOption());
            this.resetButton.render(mouseX, mouseY, delta);
            this.editButton.x = x + 180;
            this.editButton.y = y;
            this.editButton.render(mouseX, mouseY, delta);
            this.infoButton.x = x + 156;
            this.infoButton.y = y;
            this.infoButton.render(mouseX, mouseY, delta);
        }
    
        @Override
        public List<? extends Element> children()
        {
            return ImmutableList.of(this.infoButton ,this.editButton, this.resetButton);
        }
    
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button)
        {
            if (this.editButton.mouseClicked(mouseX, mouseY, button))
            {
                return true;
            }
            else if (this.infoButton.mouseClicked(mouseX, mouseY, button))
            {
                return true;
            }
            else
            {
                return this.resetButton.mouseClicked(mouseX, mouseY, button);
            }
        }
    
        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button)
        {
            return this.infoButton.mouseReleased(mouseX, mouseY, button) || this.editButton.mouseReleased(mouseX, mouseY, button) || this.resetButton.mouseReleased(mouseX, mouseY, button);
        }
    }
    
    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ElementListWidget.Entry<ConfigListWidget.Entry>
    {
    
    }
}
