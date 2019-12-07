package carpet_client.mixins;

import carpet_client.network.ServerMessageHandler;
import carpet_client.utils.Reference;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin
{
    @Shadow public ServerPlayerEntity player;
    
    @Inject(method = "onCustomPayload", at = @At("HEAD"))
    private void onOnCustomPayload(CustomPayloadC2SPacket packet, CallbackInfo ci)
    {
        CustomPayloadC2SPacketAccessor packetAccessor = (CustomPayloadC2SPacketAccessor) packet;
        Identifier channel = packetAccessor.getChannel();
        PacketByteBuf data = packetAccessor.getData();
        ServerMessageHandler.receivedPacket(channel, data, this.player);
    }
}
