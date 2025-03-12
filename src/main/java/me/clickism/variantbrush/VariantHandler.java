/*
 * Copyright 2020-2025 Clickism
 * Released under the GNU General Public License 3.0.
 * See LICENSE.md for details.
 */

package me.clickism.variantbrush;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class VariantHandler {

    private static final List<RegistryKey<CowVariant>> COW_VARIANTS = List.of(
            CowVariants.TEMPERATE,
            CowVariants.COLD,
            CowVariants.WARM
    );
    private static final List<RegistryKey<ChickenVariant>> CHICKEN_VARIANTS = List.of(
            ChickenVariants.TEMPERATE,
            ChickenVariants.COLD,
            ChickenVariants.WARM
    );
    private static final List<RegistryKey<PigVariant>> PIG_VARIANTS = List.of(
            PigVariants.TEMPERATE,
            PigVariants.COLD,
            PigVariants.WARM
    );

    public Optional<RegistryKey<?>> brushEntity(Entity entity) {
        try {
            return Optional.ofNullable(brushEntityInternal(entity));
        } catch (Exception e) {
            VariantBrush.LOGGER.error("Failed to brush entity", e);
            return Optional.empty();
        }
    }

    private RegistryKey<?> brushEntityInternal(Entity entity) {
        World world = entity.getEntityWorld();
        return switch (entity) {
            case PigEntity pigEntity -> setNextVariant(
                    world, pigEntity.getVariant(),
                    entry -> pigEntity.setComponent(DataComponentTypes.PIG_VARIANT, entry),
                    PIG_VARIANTS
            );
            case CowEntity cowEntity ->
                    setNextVariant(world, cowEntity.getVariant(), cowEntity::setVariant, COW_VARIANTS);
            case ChickenEntity chickenEntity ->
                    setNextVariant(world, chickenEntity.getVariant(), chickenEntity::setVariant, CHICKEN_VARIANTS);
            default -> null;
        };
    }

    private static <T> RegistryKey<T> setNextVariant(
            World world,
            RegistryEntry<T> currentEntry,
            Consumer<RegistryEntry<T>> setter,
            List<RegistryKey<T>> values
    ) {
        var currentKey = currentEntry.getKey().orElseThrow();
        var nextKey = getNext(values, currentKey);
        var nextEntry = getEntry(nextKey, world);
        setter.accept(nextEntry);
        return nextKey;
    }

    private static <T> RegistryEntry<T> getEntry(RegistryKey<T> key, World world) {
        return world.getRegistryManager()
                .getOrThrow(key.getRegistryRef())
                .getEntry(key.getValue())
                .orElseThrow();
    }

    private static <T> T getNext(List<T> list, T current) {
        int idx = list.indexOf(current);
        if (idx == -1) {
            throw new IllegalArgumentException("Current value not found in list");
        }
        int nextIdx = (idx + 1) % list.size();
        return list.get(nextIdx);
    }
}
