package carpet_client.mixins;

import carpet_client.gui.ConfigScreen;
import carpet_client.utils.Reference;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen
{
    protected GameMenuScreenMixin(Text title)
    {
        super(title);
    }

    @ModifyConstant(method = "initWidgets", constant = @Constant(intValue = 120))
    private int pushLimit(int original)
    {
        return 144;
    }

    @Inject(method = "initWidgets", at = @At("TAIL"))
    private void onInit(CallbackInfo ci)
    {
        ButtonWidget buttonWidget = (ButtonWidget) this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 121-1 + -16, 204, 20, "Carpet Mod Menu", (b) -> {
            this.minecraft.openScreen(new ConfigScreen((Screen)(Object)this));
        }));
        buttonWidget.active = Reference.isCarpetServer;
    }
}
