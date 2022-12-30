package me.humandavey.minigame.game;

import me.humandavey.minigame.instance.Arena;

import java.lang.reflect.InvocationTargetException;

public enum GameType {

	;

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
