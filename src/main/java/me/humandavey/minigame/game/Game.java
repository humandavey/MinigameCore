package me.humandavey.minigame.game;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.team.Team;
import me.humandavey.minigame.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Game implements Listener {

	protected Arena arena;

	public Game(Arena arena) {
		this.arena = arena;
	}

	public void start() {
		arena.setState(GameState.LIVE);
		arena.sendMessage(Util.colorize("&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));
		arena.sendMessage(Util.colorize(Util.getCenteredMessage("&f&l" + arena.getGameType().getDisplay())));
		arena.sendMessage("");
		for (String s : arena.getGameType().getDescription()) {
			arena.sendMessage(Util.colorize(Util.getCenteredMessage("&e&l" + s)));
		}
		arena.sendMessage("");
		arena.sendMessage(Util.colorize("&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));

		for (Player player : arena.getPlayers()) {
			Util.resetPlayer(player);
		}
		Bukkit.getPluginManager().registerEvents(this, Minigame.getInstance());

		onStart();
	}

	public abstract void onStart();

	public void end() {
		arena.setState(GameState.ENDING);
		arena.sendMessage(Util.colorize("&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));
		arena.sendMessage(Util.colorize(Util.getCenteredMessage("&f&l" + arena.getGameType().getDisplay())));
		arena.sendMessage("");
		arena.sendMessage(Util.colorize(Util.getCenteredMessage("&eWinner &7- &cN/A")));
		arena.sendMessage("");
		arena.sendMessage(Util.colorize("&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));

		for (Player player : arena.getAlivePlayers()) {
			arena.addSpectator(player);
		}

		Bukkit.getScheduler().runTaskLater(Minigame.getInstance(), (e) -> {
			arena.reset(true);
		}, 5 * 20L);
	}

	public void end(Player winner) {
		arena.setState(GameState.ENDING);
		arena.sendMessage(Util.colorize("&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));
		arena.sendMessage(Util.colorize(Util.getCenteredMessage("&f&l" + arena.getGameType().getDisplay())));
		arena.sendMessage("");
		arena.sendMessage(Util.colorize(Util.getCenteredMessage("&eWinner &7- &7" + winner.getName())));
		arena.sendMessage("");
		arena.sendMessage(Util.colorize("&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));

		for (Player player : arena.getAlivePlayers()) {
			if (player != winner) {
				arena.addSpectator(player);
			}
		}

		Bukkit.getScheduler().runTaskLater(Minigame.getInstance(), (e) -> {
			arena.reset(true);
		}, 5 * 20L);
	}

	public void end(Team winner) {
		arena.setState(GameState.ENDING);
		arena.sendMessage(Util.colorize("&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));
		arena.sendMessage(Util.colorize(Util.getCenteredMessage("&f&l" + arena.getGameType().getDisplay())));
		arena.sendMessage("");
		arena.sendMessage(Util.colorize(Util.getCenteredMessage("&eWinner &7- &7" + winner.getDisplay() + " Team")));
		String message = "&7";
		for (Player player : arena.getPlayers(winner)) {
			message += player.getName() + ", ";
		}
		arena.sendMessage(Util.colorize(Util.getCenteredMessage(message.substring(0, message.lastIndexOf(", ")))));
		arena.sendMessage("");
		arena.sendMessage(Util.colorize("&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));

		for (Player player : arena.getAlivePlayers()) {
			if (arena.getTeam(player) != winner) {
				arena.addSpectator(player);
			}
		}

		Bukkit.getScheduler().runTaskLater(Minigame.getInstance(), (e) -> {
			arena.reset(true);
		}, 5 * 20L);
	}

	public void unregister() {
		HandlerList.unregisterAll(this);
	}
}
