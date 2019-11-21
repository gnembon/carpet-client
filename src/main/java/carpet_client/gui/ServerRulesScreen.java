package carpet_client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;

public class ServerRulesScreen extends Screen
{
    private final Screen parent;
    
    public ServerRulesScreen(Screen parent)
    {
        super(new LiteralText("Carpet server options"));
        this.parent = parent;
    }
    
    @Override
    protected void init()
    {
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, I18n.translate("gui.done"), (button) -> {
            this.minecraft.options.write();
            this.minecraft.window.method_4475();
            this.minecraft.openScreen(this.parent);
        }));
    }
    
    @Override
    public void render(int mouseX, int mouseY, float delta)
    {
        this.renderBackground();
        this.drawCenteredString(this.font, "Carpet Server Options", this.width / 2, 8, 0xFFFFFF);
        super.render(mouseX, mouseY, delta);
    }
}