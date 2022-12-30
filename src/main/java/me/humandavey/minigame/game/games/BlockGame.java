package me.humandavey.minigame.game.games;

import me.humandavey.minigame.game.Game;
import me.humandavey.minigame.game.GameState;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.team.Team;
import me.humandavey.minigame.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;

public class BlockGame extends Game {

	private HashMap<Team, Integer> points;

	public BlockGame(Arena arena) {
		super(arena);
		this.points = new HashMap<>();
	}

	@Override
	public void onStart() {
		for (Team team : Team.values()) {
			points.put(team, 0);
		}
	}

	public void addPoint(Player player) {
		int playerTeamPoints = points.get(arena.getTeam(player)) + 1;
		if (playerTeamPoints >= 20) {
			end(arena.getTeam(player));
			return;
		}
		player.sendMessage(Util.colorize("&a+1 point!"));
		points.replace(arena.getTeam(player), playerTeamPoints);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (arena.getPlayers().contains(event.getPlayer()) && arena.getState() == GameState.LIVE) {
			addPoint(event.getPlayer());
		}
	}
}
