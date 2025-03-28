/*
 * Copyright 2020-2025 Clickism
 * Released under the GNU General Public License 3.0.
 * See LICENSE.md for details.
 */

package me.clickism.variantbrush;

import me.clickism.variantbrush.callback.BrushEntityCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariantBrush implements ModInitializer {

    public static final String MOD_ID = "variant-brush";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        UseEntityCallback.EVENT.register(new BrushEntityCallback());
    }
}