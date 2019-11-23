package carpet_client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;

public class ConfigScreen extends Screen
{
    private final Screen parent;
    private static String carpetServerVersion;
    
    public static void setCarpetServerVersion(String string)
    {
        carpetServerVersion = string;
    }
    
    public ConfigScreen(Screen parent)
    {
        super(new LiteralText("Carpet Client"));
        this.parent = parent;
    }
    
    @Override
    protected void init()
    {
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 - 12, 200, 20, "Carpet Server Options", (button) -> {
            this.minecraft.openScreen(new ServerRulesScreen(this));
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 12, 200, 20, "Carpet Client Options", (button) -> {
            // this.minecraft.openScreen(new ClientRulesScreen(this));
        }));
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
        this.drawCenteredString(this.font, "Carpet Client", this.width / 2, 8, 0xFFFFFF);
        this.drawCenteredString(this.font, String.format("Carpet server version: %s", carpetServerVersion), this.width / 2, 8 + this.font.fontHeight + 2, 0xFFFFFF);
        super.render(mouseX, mouseY, delta);
    }
}
