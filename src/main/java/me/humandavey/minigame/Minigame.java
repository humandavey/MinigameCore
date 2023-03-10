package me.humandavey.minigame;

import me.humandavey.minigame.command.commands.ArenaCommand;
import me.humandavey.minigame.command.commands.StartCommand;
import me.humandavey.minigame.command.commands.StopCommand;
import me.humandavey.minigame.listener.ManageListener;
import me.humandavey.minigame.manager.ArenaManager;
import me.humandavey.minigame.manager.NametagManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minigame extends JavaPlugin {

	private static Minigame instance;
	private ArenaManager arenaManager;

	@Override
	public void onEnable() {
		instance = this;

		setupConfigs();
		setupManagers();
		registerListeners();
		registerCommands();
	}

	@Override
	public void onDisable() {

	}

	private void setupConfigs() {
		getConfig().options().copyDefaults();
		saveDefaultConfig();
	}

	private void setupManagers() {
		arenaManager = new ArenaManager();

		NametagManager.update();
	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new ManageListener(), this);
		getServer().getPluginManager().registerEvents(new NametagManager(), this);
	}

	private void registerCommands() {
		new ArenaCommand();
		new StartCommand();
		new StopCommand();
	}

	public ArenaManager getArenaManager() {
		return arenaManager;
	}

	public static Minigame getInstance() {
		return instance;
	}
}
