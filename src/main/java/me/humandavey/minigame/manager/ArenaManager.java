package me.humandavey.minigame.manager;

import me.humandavey.minigame.game.GameState;
import me.humandavey.minigame.game.GameType;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.util.Util;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ArenaManager {

	private ArrayList<Arena> arenas = new ArrayList<>();

	public ArenaManager() {
		FileConfiguration config = Minigame.getInstance().getConfig();

		for (String s : config.getConfigurationSection("arenas").getKeys(false)) {
			arenas.add(new Arena(Integer.parseInt(s), Util.configToLocation(config, "arenas." + s), GameType.valueOf(config.getString("arenas." + s + ".game"))));
		}
	}

	public ArrayList<Arena> getArenas() {
		return arenas;
	}

	public Arena findArena(GameType type) {
		ArrayList<Arena> availableArenas = new ArrayList<>();
		for (Arena arena : arenas) {
			if (arena.getGameType() == type && (arena.getState() == GameState.COUNTDOWN || arena.getState() == GameState.WAITING) && arena.getPlayers().size() < arena.getGameType().getMaxPlayers()) {
				availableArenas.add(arena);
			}
		}
		if (availableArenas.size() == 0) return null;
		Arena finalArena = availableArenas.get(0);
		for (Arena arena : availableArenas) {
			if (arena.getPlayers().size() > finalArena.getPlayers().size() && arena.getPlayers().size() < arena.getGameType().getMaxPlayers()) {
				finalArena = arena;
			}
		}
		return finalArena;
	}

	public Arena getArena(Player player) {
		for (Arena arena : arenas) {
			if (arena.getPlayers().contains(player)) {
				return arena;
			}
		}
		return null;
	}

	public Arena getArena(int id) {
		for (Arena arena : arenas) {
			if (arena.getID() == id) {
				return arena;
			}
		}
		return null;
	}
}
