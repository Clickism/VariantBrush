/*
 * Copyright 2020-2025 Clickism
 * Released under the GNU General Public License 3.0.
 * See LICENSE.md for details.
 */

package me.clickism.variantbrush;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class VariantHandler {

    private static final List<Cow.Variant> COW_VARIANTS = List.of(
            Cow.Variant.TEMPERATE,
            Cow.Variant.COLD,
            Cow.Variant.WARM
    );
    private static final List<Chicken.Variant> CHICKEN_VARIANTS = List.of(
            Chicken.Variant.TEMPERATE,
            Chicken.Variant.COLD,
            Chicken.Variant.WARM
    );
    private static final List<Pig.Variant> PIG_VARIANTS = List.of(
            Pig.Variant.TEMPERATE,
            Pig.Variant.COLD,
            Pig.Variant.WARM
    );

    public Optional<String> brushEntity(Entity entity) {
        Keyed variant = brushEntityInternal(entity);
        return Optional.ofNullable(variant)
                .map(Keyed::getKey)
                .map(NamespacedKey::getKey)
                .map(String::toLowerCase);
    }

    @Nullable
    private Keyed brushEntityInternal(Entity entity) {
        if (entity instanceof Cow cow) {
            Cow.Variant nextVariant = getNextVariant(cow.getVariant(), COW_VARIANTS);
            cow.setVariant(nextVariant);
            return nextVariant;
        }
        if (entity instanceof Chicken chicken) {
            Chicken.Variant nextVariant = getNextVariant(chicken.getVariant(), CHICKEN_VARIANTS);
            chicken.setVariant(nextVariant);
            return nextVariant;
        }
        if (entity instanceof Pig pig) {
            Pig.Variant nextVariant = getNextVariant(pig.getVariant(), PIG_VARIANTS);
            pig.setVariant(nextVariant);
            return nextVariant;
        }
        return null;
    }

    private <T> T getNextVariant(T currentVariant, List<T> variants) {
        int index = variants.indexOf(currentVariant);
        return variants.get((index + 1) % variants.size());
    }
}
