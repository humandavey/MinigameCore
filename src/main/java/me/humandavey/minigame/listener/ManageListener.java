package me.humandavey.minigame.listener;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.game.GameState;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.manager.ConfigManager;
import me.humandavey.minigame.manager.NametagManager;
import me.humandavey.minigame.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ManageListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Util.resetPlayer(event.getPlayer());
		event.getPlayer().teleport(ConfigManager.getLobbySpawn());

		NametagManager.setPrefix(event.getPlayer(), "");
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Arena arena = Minigame.getInstance().getArenaManager().getArena(event.getPlayer());
		if (arena != null) {
			arena.removePlayer(event.getPlayer());
			arena.removeSpectator(event.getPlayer());
		}
		NametagManager.setPrefix(event.getPlayer(), "");
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player attacker) {
			Arena victimArena = Minigame.getInstance().getArenaManager().getArena(victim);
			Arena attackerArena = Minigame.getInstance().getArenaManager().getArena(attacker);
			if (victimArena != null && victimArena == attackerArena) {
				if (victimArena.sameTeam(victim, attacker)) {
					event.setCancelled(true);
					attacker.sendMessage(Util.colorize("&cYou cannot attack your teammates!"));
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player player) {
			Arena arena = Minigame.getInstance().getArenaManager().getArena(player);
			if (arena != null) {
				if (arena.getState() != GameState.LIVE) {
					event.setCancelled(true);
					if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
						player.teleport(arena.getSpawn());
					}
				}
			}
		}
	}

	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player player) {
			Arena arena = Minigame.getInstance().getArenaManager().getArena(player);
			if (arena != null) {
				if (arena.getState() != GameState.LIVE) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Arena arena = Minigame.getInstance().getArenaManager().getArena(event.getPlayer());
		if (arena != null) {
			if (arena.getState() != GameState.LIVE) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Arena arena = Minigame.getInstance().getArenaManager().getArena(event.getPlayer());
		if (arena != null) {
			if (arena.getState() != GameState.LIVE) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Arena arena = Minigame.getInstance().getArenaManager().getArena(event.getPlayer());
		if (arena != null) {
			event.setCancelled(true);
			arena.sendMessage(Util.colorize("&7[" + arena.getTeam(event.getPlayer()).getDisplay() + "&7] " + arena.getTeam(event.getPlayer()).getColor() + event.getPlayer().getName() + "&f: " + event.getMessage()));
		}
	}
}
