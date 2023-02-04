package me.humandavey.minigame.instance;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.game.GameState;
import me.humandavey.minigame.manager.ConfigManager;
import me.humandavey.minigame.manager.ScoreboardManager;
import me.humandavey.minigame.util.Util;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

	private Arena arena;
	private int countdownSeconds;
	private boolean cancel;
	private boolean isRunning;

	public Countdown(Arena arena) {
		this.arena = arena;
		this.countdownSeconds = ConfigManager.getCountdownSeconds();
	}

	public void start() {
		arena.setState(GameState.COUNTDOWN);
		runTaskTimer(Minigame.getInstance(), 0, 20);
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public void run() {
		isRunning = true;
		if (countdownSeconds == 0 || cancel) {
			for (Player player : arena.getPlayers()) {
				player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 0.3f, 1f);
			}
			arena.sendTitle(Util.colorize("&aGO!"), "", 0, 20, 15);
			cancel();
			arena.start();
			isRunning = false;
			return;
		}

		for (Player player : arena.getPlayers()) {
			player.setLevel(countdownSeconds);
			ScoreboardManager.updateLine(player, 2, Util.colorize("&fStarting in &e" + countdownSeconds + "&fs!"));
			player.setExp((float) countdownSeconds / ConfigManager.getCountdownSeconds());
		}

		if (countdownSeconds <= 5 || countdownSeconds % 5 == 0) {
			for (Player player : arena.getPlayers()) {
				player.playSound(player.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1f, 5f);
			}
			if (countdownSeconds == 10) {
				arena.sendMessage(Util.colorize("&eThe game will start in &610 &eseconds" + "!"));
			} else if (countdownSeconds <= 5) {
				arena.sendTitle(Util.colorize("&c" + countdownSeconds), Util.colorize("&eGet ready to fight!"), 0, 21, 0);
				arena.sendMessage(Util.colorize("&eThe game will start in &c" + countdownSeconds + " &esecond" + (countdownSeconds == 1 ? "" : "s") + "!"));
			} else {
				arena.sendMessage(Util.colorize("&eThe game will start in &a" + countdownSeconds + " &eseconds" + "!"));
			}
		}

		countdownSeconds--;
	}

	public boolean isRunning() {
		return isRunning;
	}
}
