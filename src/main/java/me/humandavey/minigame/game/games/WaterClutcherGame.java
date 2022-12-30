package me.humandavey.minigame.game.games;

import me.humandavey.minigame.game.Game;
import me.humandavey.minigame.instance.Arena;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WaterClutcherGame extends Game {

	public WaterClutcherGame(Arena arena) {
		super(arena);
	}

	@Override
	public void onStart() {
		for (Player player : arena.getAlivePlayers()) {
			player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
		}
	}
}
