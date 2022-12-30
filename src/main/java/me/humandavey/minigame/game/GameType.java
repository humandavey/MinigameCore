package me.humandavey.minigame.game;

import me.humandavey.minigame.game.games.WaterClutcherGame;
import me.humandavey.minigame.instance.Arena;

import java.lang.reflect.InvocationTargetException;

public enum GameType {

	WATERCLUTCHER("WaterClutcher", 2, 8, 8, new String[]{"Try not to die from fall damage", "while being the last player alive!"}, WaterClutcherGame.class);

	private final String display;
	private final int minTeams;
	private final int maxPlayers;
	private final int numTeams;
	private final String[] description;
	private final Class<? extends Game> clazz;

	GameType(String display, int minTeams, int maxPlayers, int numTeams, String[] description, Class<? extends Game> clazz) {
		this.display = display;
		this.minTeams = minTeams;
		this.maxPlayers = maxPlayers;
		this.numTeams = numTeams;
		this.description = description;
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

	public Game getNewInstance(Arena arena) {
		try {
			return clazz.getConstructor(Arena.class).newInstance(arena);
		} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
