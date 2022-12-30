package me.humandavey.minigame.listener;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.manager.ConfigManager;
import me.humandavey.minigame.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ManageListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().teleport(ConfigManager.getLobbySpawn());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Arena arena = Minigame.getInstance().getArenaManager().getArena(event.getPlayer());
		if (arena != null) {
			arena.removePlayer(event.getPlayer());
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player attacker) {
			Arena victimArena = Minigame.getInstance().getArenaManager().getArena(victim);
			Arena attackerArena = Minigame.getInstance().getArenaManager().getArena(attacker);
			if (victimArena == attackerArena) {
				if (victimArena.sameTeam(victim, attacker)) {
					event.setCancelled(true);
					attacker.sendMessage(Util.colorize("&cYou cannot attack your teammates!"));
				}
			}
		}
	}
}
