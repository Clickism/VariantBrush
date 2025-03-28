/*
 * Copyright 2020-2025 Clickism
 * Released under the GNU General Public License 3.0.
 * See LICENSE.md for details.
 */

package me.clickism.variantbrush;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class VariantBrush extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
    }
}
