package me.humandavey.minigame.game.games;

import me.humandavey.minigame.game.Game;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.manager.ScoreboardManager;
import me.humandavey.minigame.util.Util;
import me.humandavey.minigame.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class FFAGame extends Game {

	private final HashMap<Player, Integer> kills = new HashMap<>();

	public FFAGame(Arena arena) {
		super(arena);
	}

	@Override
	public void onStart() {
		for (Player player : arena.getPlayers()) {
			player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD).build());
			kills.put(player, 0);

			ScoreboardManager.setScoreboard(player, Util.colorize("&e&lFFA"),
					Util.colorize("&7" + Util.getDate()),
					"",
					Util.colorize("&fNext Event: &eN/A"),
					"  ",
					Util.colorize("&fKills: &e0"),
					"   ",
					Util.colorize("&ewww.dartanetwork.net"));
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (arena.getAlivePlayers().contains(event.getEntity())) {
			Player victim = event.getEntity();
			event.getDrops().clear();
			event.setDroppedExp(0);

			if (victim.getKiller() != null) {
				Player killer = victim.getKiller();
				kills.put(killer, kills.get(killer) + 1);
				ScoreboardManager.updateLine(killer, 3, Util.colorize("&fKills: &e" + kills.get(killer)));

				arena.sendMessage(Util.colorize(arena.getTeam(killer).getColor() + killer.getName() + " &ehas killed " + arena.getTeam(victim).getColor() + victim.getName() + "&e!"));
			} else {
				arena.sendMessage(Util.colorize(arena.getTeam(victim).getColor() + victim.getName() + " &ehas died."));
			}

			if (arena.getAlivePlayers().size() == 1) {
				arena.addSpectator(victim);
				end(arena.getAlivePlayers().get(0));
			} else if (arena.getAlivePlayers().size() < 1) {
				end();
			}
		}
	}
}
