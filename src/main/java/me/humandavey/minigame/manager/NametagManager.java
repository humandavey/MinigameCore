package me.humandavey.minigame.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;

public class NametagManager implements Listener {

	private static final HashMap<UUID, Scoreboard> scoreboards = new HashMap<>();

	private static void addPlayer(Player player) {
		if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
			scoreboards.put(player.getUniqueId(), player.getScoreboard());
		} else {
			Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
			scoreboards.put(player.getUniqueId(), sb);
			player.setScoreboard(sb);
		}

		Scoreboard sb = player.getScoreboard();

		for (UUID uuid : scoreboards.keySet()) {
			if (uuid == player.getUniqueId()) continue;
			Player target = Bukkit.getPlayer(uuid);
			Team t = player.getScoreboard().registerNewTeam(target.getName() + "#" + player.getUniqueId());
			t.addEntry(target.getName());
			t.setPrefix(target.getScoreboard().getTeam(target.getName() + "#" + uuid.toString()).getPrefix());
			t.setSuffix(target.getScoreboard().getTeam(target.getName() + "#" + uuid.toString()).getSuffix());
			t.setColor(target.getScoreboard().getTeam(target.getName() + "#" + uuid.toString()).getColor());
		}

		for (UUID uuid : scoreboards.keySet()) {
			Team t = null;
			try {
				t = scoreboards.get(uuid).getEntryTeam(player.getName() + "#" + uuid.toString());
			} catch (IllegalArgumentException ignored) {}

			if (t == null) {
				scoreboards.get(uuid).registerNewTeam(player.getName() + "#" + uuid.toString());
			}
			scoreboards.get(uuid).getTeam(player.getName() + "#" + uuid.toString()).addEntry(player.getName());
		}

		for (UUID uuid : scoreboards.keySet()) {
			Team t = null;
			try {
				t = sb.registerNewTeam(Bukkit.getPlayer(uuid).getName() + "#" + player.getUniqueId());
			} catch (IllegalArgumentException ignored) {}

			Team pt = null;
			try {
				pt = Bukkit.getPlayer(uuid).getScoreboard().getEntryTeam(Bukkit.getPlayer(uuid).getName());
			} catch (IllegalArgumentException ignored) {}

			if (t != null && pt != null) {
				t.setColor(pt.getColor());
				t.setPrefix(pt.getPrefix());
				t.setSuffix(pt.getSuffix());
			}
		}
	}

	public static void update() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!scoreboards.containsKey(player.getUniqueId())) {
				scoreboards.put(player.getUniqueId(), player.getScoreboard());
			}
		}
	}

	public static void setPrefix(Player player, String prefix) {
		for (UUID uuid : scoreboards.keySet()) {
			scoreboards.get(uuid).getTeam(player.getName() + "#" + uuid.toString()).setPrefix(prefix);
		}
	}

	public static void setSuffix(Player player, String suffix) {
		for (UUID uuid : scoreboards.keySet()) {
			scoreboards.get(uuid).getTeam(player.getName() + "#" + uuid.toString()).setSuffix(suffix);
		}
	}

	public static void setColor(Player player, ChatColor color) {
		for (UUID uuid : scoreboards.keySet()) {
			scoreboards.get(uuid).getTeam(player.getName() + "#" + uuid.toString()).setColor(color);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		addPlayer(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		scoreboards.remove(event.getPlayer().getUniqueId());

		for (UUID uuid : scoreboards.keySet()) {
			scoreboards.get(uuid).getTeam(event.getPlayer().getName() + "#" + uuid.toString()).unregister();
		}
	}

}