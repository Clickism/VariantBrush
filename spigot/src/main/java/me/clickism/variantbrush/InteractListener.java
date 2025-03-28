/*
 * Copyright 2020-2025 Clickism
 * Released under the GNU General Public License 3.0.
 * See LICENSE.md for details.
 */

package me.clickism.variantbrush;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

public class InteractListener implements Listener {

    private static final VariantHandler VARIANT_HANDLER = new VariantHandler();

    @EventHandler
    private void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.BRUSH) return;
        Entity entity = event.getRightClicked();
        EntityType type = entity.getType();
        if (type != EntityType.COW && type != EntityType.PIG && type != EntityType.CHICKEN) return;
        VARIANT_HANDLER.brushEntity(entity).ifPresent(variant -> {
            event.setCancelled(true);
            sendMessage(player, variant);
            sendEffect(player, entity);
        });
    }

    private static void sendMessage(Player player, String variant) {
        String message = DARK_GRAY + "< " + DARK_GREEN + "↔ " + GREEN
                + "Variant changed to " + variant + " " + DARK_GRAY + ">";
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(message));
    }

    private static void sendEffect(Player player, Entity entity) {
        World world = player.getWorld();
        Location location = entity.getLocation();
        player.playSound(entity, Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 1, .5f);
        player.playSound(entity, Sound.BLOCK_AZALEA_LEAVES_PLACE, 1, 1);
        player.playSound(entity, Sound.BLOCK_AZALEA_PLACE, 1, 2);
        world.spawnParticle(Particle.WAX_ON, location, 10, .2, 0, .2, 2);
        Block blockBelow = location.getBlock().getRelative(BlockFace.DOWN);
        world.spawnParticle(Particle.BLOCK, location, 30, blockBelow.getBlockData());
    }
}
