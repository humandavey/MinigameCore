package me.humandavey.minigame.manager;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

	public static void setScoreboard(Player player, String title, String... lines) {
		Scoreboard sb = player.getScoreboard();

		removeScoreboard(player);

		for (Team team : sb.getTeams()) {
			try {
				Integer.parseInt(team.getName());
				team.unregister();
			} catch (NumberFormatException ignored) {}
		}

		Objective obj = sb.registerNewObjective(player.getName(), "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(title);

		int spot = 0;
		for (int i = lines.length - 1; i >= 0; i--) {

			Team team = sb.registerNewTeam(i + "");
			team.addEntry(multiplyString(" ", i));
			team.setPrefix(lines[i]);
			obj.getScore(multiplyString(" ", i)).setScore(spot);

			spot++;
		}
	}

	public static void removeScoreboard(Player player) {
		Scoreboard sb = player.getScoreboard();
		for (Objective obj : sb.getObjectives()) {
			obj.unregister();
		}
	}

	public static void updateLine(Player player, int line, String content) {
		Scoreboard sb = player.getScoreboard();

		Team team = sb.getTeam(line + "");
		team.setPrefix(content);
	}

	private static String multiplyString(String content, int i) {
		String output = content;
		for (int j = 0; j < i; j++) {
			output += content;
		}
		return output;
	}
}
