package me.humandavey.minigame.instance;

import me.humandavey.minigame.game.GameState;
import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.manager.ConfigManager;
import me.humandavey.minigame.util.Util;
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
			arena.sendTitle(Util.colorize("&aGO!"), "", 0, 20, 15);
			cancel();
			arena.start();
			isRunning = false;
			return;
		}

		if (countdownSeconds <= 5 || countdownSeconds % 5 == 0) {
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
