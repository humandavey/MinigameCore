package me.humandavey.minigame.game;

import me.humandavey.minigame.game.games.*;
import me.humandavey.minigame.instance.Arena;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.lang.reflect.InvocationTargetException;

public enum GameType {

	WATERCLUTCHER("WaterClutcher", 2, 8, 8, new String[]{"Try not to die from fall damage", "while being the last player alive!"}, Material.WATER_BUCKET, WaterClutcherGame.class),
	FFA("FFA", 2, 16, 16, new String[]{"Last player standing wins!"}, Material.DIAMOND_AXE, FFAGame.class);

	private final String display;
	private final int minTeams;
	private final int maxPlayers;
	private final int numTeams;
	private final String[] description;
	private final Material icon;
	private final Class<? extends Game> clazz;

	GameType(String display, int minTeams, int maxPlayers, int numTeams, String[] description, Material icon, Class<? extends Game> clazz) {
		this.display = display;
		this.minTeams = minTeams;
		this.maxPlayers = maxPlayers;
		this.numTeams = numTeams;
		this.description = description;
		this.icon = icon;
		this.clazz = clazz;
	}

	public String getDisplay() {
		return display;
	}

	public int getMinTeams() {
		return minTeams;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getNumTeams() {
		return numTeams;
	}

	public int getPlayersPerTeam() {
		return getMaxPlayers() / getNumTeams();
	}

	public String[] getDescription() {
		return description;
	}

	public String[] getFormattedDescription(ChatColor color) {
		String[] arr = new String[description.length];
		for (int i = 0; i < description.length; i++) {
			arr[i] = color + description[i];
		}
		return arr;
	}

	public Material getIcon() {
		return icon;
	}

	public Game getNewInstance(Arena arena) {
		try {
			return clazz.getConstructor(Arena.class).newInstance(arena);
		} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
