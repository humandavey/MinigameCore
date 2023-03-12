package me.humandavey.minigame.game.games;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.game.Game;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.manager.ScoreboardManager;
import me.humandavey.minigame.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TNTRunGame extends Game {

	public TNTRunGame(Arena arena) {
		super(arena);
	}

	@Override
	public void onStart() {
		for (Player player : arena.getAllPlayers()) {
			ScoreboardManager.setScoreboard(player, Util.colorize("&e&lTNT RUN"),
					Util.colorize("&7" + Util.getDate()),
					" ",
					Util.colorize("&ewww.dartanetwork.net"));
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (arena.getAlivePlayers().contains(event.getPlayer())) {
			if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND) {
				Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
				Block tnt = block.getWorld().getBlockAt(block.getLocation().add(0, -1, 0));

				tnt.setType(Material.AIR);
				block.getWorld().spawnEntity(block.getLocation().add(0, -1, 0), EntityType.PRIMED_TNT);
			}
		}
	}

	@EventHandler
	public void onEntityChange(EntityExplodeEvent event) {
		if (event.getEntityType() == EntityType.PRIMED_TNT) {
			event.setCancelled(true);
			event.getEntity().remove();
		}
	}

	@EventHandler
	public void onDamage(PlayerDeathEvent event) {
		if (!arena.getAlivePlayers().contains(event.getEntity())) return;
		if (event.getEntity().getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.VOID) return;

		new BukkitRunnable() {
			@Override
			public void run() {
				event.getEntity().spigot().respawn();
			}
		}.runTaskLater(Minigame.getInstance(), 1L);

		arena.addSpectator(event.getEntity());
		event.getEntity().teleport(arena.getSpawn());

		if (arena.getAliveTeams().size() == 1) {
			arena.getGame().end(arena.getAliveTeams().get(0));
		} else if (arena.getAliveTeams().size() < 1) {
			arena.getGame().end();
		}
	}
}
