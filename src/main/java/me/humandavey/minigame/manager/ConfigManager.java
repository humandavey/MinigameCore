package me.humandavey.minigame.manager;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.util.Util;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

	private static FileConfiguration config = Minigame.getInstance().getConfig();

	public static int getCountdownSeconds() {
		return config.getInt("countdown-seconds");
	}

	public static Location getLobbySpawn() {
		return Util.configToLocation(config, "lobby-spawn");
	}
}
