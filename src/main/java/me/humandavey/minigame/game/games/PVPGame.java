package me.humandavey.minigame.game.games;

import me.humandavey.minigame.game.Game;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;


public class PVPGame extends Game {

	public PVPGame(Arena arena) {
		super(arena);
	}

	@Override
	public void onStart() {
		for (Player player : arena.getPlayers()) {
			player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
		}
	}

	@EventHandler
	public void onPlayerDeath(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player victim && victim.getKiller() == null) {
			arena.setSpectator(victim);
			arena.sendMessage(Util.colorize("&7" + victim.getName() + " &ehas died!"));

			if (arena.getAlivePlayers().size() <= 1) {
				end();
			}
			return;
		}

		if (event.getEntity() instanceof Player victim && victim.getKiller() != null) {
			arena.setSpectator(victim);
			arena.sendMessage(Util.colorize("&7" + victim.getName() + " &ewas killed by &7" + victim.getKiller().getName() + "!"));

			if (arena.getAlivePlayers().size() <= 1) {
				end(victim.getKiller());
			}
		}
	}
}
