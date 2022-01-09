package ru.DmN.fb.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.DmN.fb.Main;

import static net.minecraft.block.CropBlock.AGE;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void injectTick(CallbackInfo ci) {
        if (Main.State)
        for (int i = -4; i < 4; i++)
            for (int j = -4; j < 4; j++)
                for (int k = -4; k < 4; k++) {
                    var pos = this.getBlockPos().add(i, j, k);
                    var state = this.world.getBlockState(pos);
                    if (state.getProperties().contains(AGE) && state.get(AGE) == 7)
                        ((ClientPlayerEntity) (Object) this).networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, Direction.UP));
                    else if (state.getBlock() == Blocks.FARMLAND && this.world.getBlockState(pos.up()).getBlock() == Blocks.AIR)
                        MinecraftClient.getInstance().interactionManager.interactBlock((ClientPlayerEntity) (Object) this, (ClientWorld) this.world, Hand.MAIN_HAND, BlockHitResult.createMissed(new Vec3d(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5), Direction.UP, pos));
                }
    }
}