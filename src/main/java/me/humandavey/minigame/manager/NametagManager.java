package me.humandavey.minigame.manager;

import me.humandavey.minigame.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class NametagManager {

	public static void createTeam(Player player) {
		if (Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName()) == null)
			Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(player.getName()).addEntry(player.getName());
	}

	private static Team getTeam(Player player) {
		return Bukkit.getScoreboardManager().getMainScoreboard().getTeam(player.getName());
	}

	public static void setPrefix(Player player, String prefix) {
		if (getTeam(player) != null)
			getTeam(player).setPrefix(Util.colorize(prefix));
	}

	public static void setSuffix(Player player, String suffix) {
		if (getTeam(player) != null)
			getTeam(player).setSuffix(Util.colorize(suffix));
	}

	public static void setNameColor(Player player, ChatColor color) {
		if (getTeam(player) != null)
			getTeam(player).setColor(color);
	}
}
