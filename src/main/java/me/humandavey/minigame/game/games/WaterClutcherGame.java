package me.humandavey.minigame.game.games;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.game.Game;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.manager.ScoreboardManager;
import me.humandavey.minigame.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
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
	private final HashMap<Player, Integer> kills = new HashMap<>();

	public WaterClutcherGame(Arena arena) {
		super(arena);
	}

	@Override
	public void onStart() {
		for (Player player : arena.getAlivePlayers()) {
			player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
			knockback.put(player, 1.0);
			kills.put(player, 0);

			ScoreboardManager.setScoreboard(player, Util.colorize("&e&lWATERCLUTCHER"),
					Util.colorize("&7" + Util.getDate()),
					" ",
					Util.colorize("&fKnockback: &e100%"),
					"  ",
					Util.colorize("&fKills: &e0"),
					"   ",
					Util.colorize("&ewww.dartanetwork.net"));
		}
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player attacker) {
			if (arena.getPlayers().contains(victim) && arena.getPlayers().contains(attacker)) {
				victim.setVelocity(attacker.getLocation().getDirection().setY(knockback.get(victim) - 1).normalize().multiply(knockback.get(victim)));
				knockback.replace(victim, knockback.get(victim) + 0.1);
				ScoreboardManager.updateLine(victim, 2, Util.colorize("&fKnockback: &e" + Math.round(knockback.get(victim) * 100) + "%"));
				event.setDamage(0);
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (arena.getPlayers().contains(event.getEntity())) {
			event.getDrops().clear();
			event.setDroppedExp(0);
			new BukkitRunnable() {
				@Override
				public void run() {
					event.getEntity().spigot().respawn();
				}
			}.runTaskLater(Minigame.getInstance(), 1L);
			arena.addSpectator(event.getEntity());
			event.setDeathMessage(null);
			for (Player player : arena.getAllPlayers()) {
				if (event.getEntity().getKiller() == null) {
					player.sendMessage(Util.colorize("&7" + arena.getTeam(event.getEntity()).getColor() + event.getEntity().getName() + " &ehas died!"));
				} else if (event.getEntity().getKiller() != null) {
					player.sendMessage(Util.colorize("&7" + arena.getTeam(event.getEntity()).getColor() + event.getEntity().getName() + " &ewas killed by &7" + arena.getTeam(event.getEntity().getKiller()).getColor() + event.getEntity().getKiller().getName() + "&e!"));
				} else {
					player.sendMessage(Util.colorize("&7" + arena.getTeam(event.getEntity()).getColor() + event.getEntity().getName() + " &ehas died!"));
				}
			}
			if (event.getEntity().getKiller() != null) {
				kills.put(event.getEntity().getKiller(), kills.getOrDefault(event.getEntity().getKiller(), 0) + 1);
				ScoreboardManager.updateLine(event.getEntity().getKiller(), 4, Util.colorize("&fKills: &e" + kills.get(event.getEntity().getKiller())));
			}
			if (arena.getAliveTeams().size() == 1) {
				end(arena.getAliveTeams().get(0));
			} else if (arena.getAliveTeams().size() < 1) {
				end();
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
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent event) {
		if (arena.getPlayers().contains((Player) event.getEntity())) {
			event.setCancelled(true);
		}
	}
}
