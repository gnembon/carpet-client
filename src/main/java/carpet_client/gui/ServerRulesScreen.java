package carpet_client.gui;

import carpet_client.utils.CarpetRules;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;

public class ServerRulesScreen extends Screen
{
    private final Screen parent;
    private ConfigListWidget list;
    
    public ServerRulesScreen(Screen parent)
    {
        super(new LiteralText("Carpet server options"));
        this.parent = parent;
    }
    
    @Override
    protected void init()
    {
        this.list = new ConfigListWidget(this, this.minecraft);
        this.children.add(this.list);
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height - 29, 150, 20, "Force Update", (buttonWidget) -> {
            CarpetRules.requestUpdate();
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.translate("gui.done"), (buttonWidget) -> {
            this.minecraft.openScreen(this.parent);
        }));
    }
    
    @Override
    public void render(int mouseX, int mouseY, float delta)
    {
        this.renderBackground();
        this.list.render(mouseX, mouseY, delta);
        this.drawCenteredString(this.font, "Carpet Server Options", this.width / 2, 8, 0xFFFFFF);
        super.render(mouseX, mouseY, delta);
    }
}