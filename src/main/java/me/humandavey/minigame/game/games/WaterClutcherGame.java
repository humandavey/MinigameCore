package me.humandavey.minigame.game.games;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.game.Game;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.util.Util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class WaterClutcherGame extends Game {

	private final HashMap<Player, Double> knockback = new HashMap<>();
	private final ArrayList<Block> waters = new ArrayList<>();

	public WaterClutcherGame(Arena arena) {
		super(arena);
	}

	@Override
	public void onStart() {
		for (Player player : arena.getAlivePlayers()) {
			player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
			knockback.put(player, 1.0);
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : arena.getAlivePlayers()) {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Util.colorize("&eYou are at " + Math.round(knockback.get(player) * 100) + "% knockback!")));
				}
			}
		}.runTaskTimer(Minigame.getInstance(), 0, 20);
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player attacker) {
			if (arena.getPlayers().contains(victim) && arena.getPlayers().contains(attacker)) {
				victim.setVelocity(victim.getVelocity().multiply(knockback.get(victim)));
				knockback.replace(victim, knockback.get(victim) + 0.1);
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (arena.getPlayers().contains(event.getEntity())) {
			arena.addSpectator(event.getEntity());
			event.setDeathMessage(null);
			for (Player player : arena.getAllPlayers()) {
				if (event.getEntity().getKiller() == null) {
					player.sendMessage(Util.colorize("&7" + event.getEntity().getName() + " has died!"));
				} else {
					player.sendMessage(Util.colorize("&7" + event.getEntity().getName() + " &ewas killed by &7" + event.getEntity().getKiller() + "&e!"));
				}
			}
			if (arena.getAliveTeams().size() <= 1) {
				end(arena.getTeam(arena.getAlivePlayers().get(0)));
			}
		}
	}

	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		if (arena.getPlayers().contains(event.getPlayer())) {
			waters.add(event.getBlockClicked().getRelative(event.getBlockFace()));
		}
	}

	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent event) {
		if (arena.getPlayers().contains(event.getPlayer())) {
			waters.remove(event.getBlock());
		}
	}

	@EventHandler
	public void onWaterFlow(BlockFromToEvent event) {
		if (event.getBlock().isLiquid() && waters.contains(event.getBlock())) {
			event.setCancelled(true );
		}
	}
}
