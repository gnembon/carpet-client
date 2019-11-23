package carpet_client.mixins;

import carpet.settings.ParsedRule;
import carpet_client.network.ServerMessageHandler;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParsedRule.class)
public abstract class ParsedRuleMixin
{
    @Shadow @Final public String name;
    
    @Inject(method = "set(Lnet/minecraft/server/command/ServerCommandSource;Ljava/lang/String;)Lcarpet/settings/ParsedRule;", at = @At("HEAD"))
    private void onSet(ServerCommandSource source, String value, CallbackInfoReturnable<Object> cir) throws CommandSyntaxException
    {
        ServerMessageHandler.updateCarpetClientRules(this.name, value, source.getPlayer());
    }
}
