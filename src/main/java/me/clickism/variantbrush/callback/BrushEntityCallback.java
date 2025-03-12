/*
 * Copyright 2020-2025 Clickism
 * Released under the GNU General Public License 3.0.
 * See LICENSE.md for details.
 */

package me.clickism.variantbrush.callback;

import me.clickism.variantbrush.VariantHandler;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class BrushEntityCallback implements UseEntityCallback {

    private static final Set<EntityType<?>> FARM_ANIMALS = Set.of(
            EntityType.PIG,
            EntityType.COW,
            EntityType.CHICKEN
    );

    private final VariantHandler variantHandler = new VariantHandler();

    private static String formatKey(RegistryKey<?> key) {
        return key.getValue().getPath().replace('_', ' ');
    }

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity,
                                 @Nullable EntityHitResult hitResult) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isOf(Items.BRUSH)) return ActionResult.PASS;
        if (world.isClient()) {
            if (FARM_ANIMALS.contains(entity.getType())) {
                player.swingHand(hand);
            }
            return ActionResult.SUCCESS;
        }
        if (!hand.equals(Hand.MAIN_HAND)) return ActionResult.PASS;
        if (player.isSpectator()) return ActionResult.PASS;

        variantHandler.brushEntity(entity).ifPresent(key -> {
            sendVariantChangeMessage(player, key);
            sendEffect(player, entity);
        });
        return ActionResult.PASS;
    }

    private void sendVariantChangeMessage(PlayerEntity player, RegistryKey<?> key) {
        player.sendMessage(Text.literal("< ").formatted(Formatting.DARK_GRAY)
                .append(Text.literal("↔ ").formatted(Formatting.DARK_GREEN))
                .append(Text.literal("Variant changed to ").formatted(Formatting.GREEN))
                .append(Text.literal(formatKey(key)).formatted(Formatting.GREEN))
                .append(Text.literal(" >").formatted(Formatting.DARK_GRAY)), true);
    }

    private void sendEffect(PlayerEntity player, Entity entity) {
        player.playSoundToPlayer(SoundEvents.BLOCK_COMPOSTER_FILL_SUCCESS, SoundCategory.MASTER, 1, .5f);
        player.playSoundToPlayer(SoundEvents.BLOCK_AZALEA_LEAVES_PLACE, SoundCategory.MASTER, 1, 1);
        player.playSoundToPlayer(SoundEvents.BLOCK_AZALEA_PLACE, SoundCategory.MASTER, 1, 2);
        ServerWorld serverWorld = (ServerWorld) entity.getEntityWorld();
        serverWorld.spawnParticles(
                ParticleTypes.WAX_ON,
                entity.getX(), entity.getY() + .4, entity.getZ(),
                10, .2, 0, .2, 2
        );
        BlockPos posBelow = entity.getBlockPos().down();
        serverWorld.spawnParticles(
                new BlockStateParticleEffect(ParticleTypes.BLOCK, serverWorld.getBlockState(posBelow)),
                entity.getX(), entity.getY(), entity.getZ(),
                30, 0, 0, 0, 1
        );
    }
}
