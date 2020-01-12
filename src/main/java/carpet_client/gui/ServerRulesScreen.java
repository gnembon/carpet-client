package carpet_client.gui;

import carpet_client.utils.CarpetSettingsServerNetworkHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;

public class ServerRulesScreen extends Screen
{
    private final Screen parent;
    private ConfigListWidget list;
    private boolean invalid;
    private boolean isEmpty;
    private ArrayList<TextFieldWidget> stringFieldList = new ArrayList<>();
    private ArrayList<TextFieldWidget> numberFieldList = new ArrayList<>();
    
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
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height - 29, 150, 20, "Refresh", (buttonWidget) -> {
            CarpetSettingsServerNetworkHandler.requestUpdate(this.minecraft);
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.translate("gui.done"), (buttonWidget) -> {
            this.minecraft.openScreen(this.parent);
        }));
    }
    
    @Override
    public void tick()
    {
        this.stringFieldList.forEach(TextFieldWidget::tick);
        this.numberFieldList.forEach(TextFieldWidget::tick);
    }
    
    @Override
    public void render(int mouseX, int mouseY, float delta)
    {
        this.renderBackground();
        this.list.render(mouseX, mouseY, delta);
        this.drawTooltip(mouseX, mouseY, delta);
        this.drawCenteredString(this.font, "Carpet Settings", this.width / 2, 8, 0xFFFFFF);
        if (this.invalid)
        {
            String text = this.isEmpty ? "You can't leave the field empty!" : "Invalid value! Type an integer!";
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            fillGradient(8, 9, 20 + this.font.getStringWidth(text), 14 + this.font.fontHeight, 0x68000000, 0x68000000);
            blit(10, 10, 0, 54, 3, 11);
            drawString(this.font, text, 18, 12, 16733525);
        }
        super.render(mouseX, mouseY, delta);
    }
    
    public void drawTooltip(int mouseX, int mouseY, float delta)
    {
        this.list.drawTooltip(mouseX, mouseY, delta);
    }
    
    public void setInvalid(boolean invalid)
    {
        this.invalid = invalid;
    }
    
    public void setEmpty(boolean isEmpty)
    {
        this.isEmpty = isEmpty;
    }
    
    public ArrayList<TextFieldWidget> getStringFieldList()
    {
        return stringFieldList;
    }
    
    public ArrayList<TextFieldWidget> getNumberFieldList()
    {
        return numberFieldList;
    }
}