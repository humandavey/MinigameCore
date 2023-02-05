package me.humandavey.minigame.instance;

import com.google.common.collect.TreeMultimap;
import me.humandavey.minigame.game.Game;
import me.humandavey.minigame.game.GameState;
import me.humandavey.minigame.game.GameType;
import me.humandavey.minigame.manager.ConfigManager;
import me.humandavey.minigame.manager.ScoreboardManager;
import me.humandavey.minigame.menu.Menu;
import me.humandavey.minigame.team.Team;
import me.humandavey.minigame.util.Util;
import me.humandavey.minigame.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Arena {

	private final int id;
	private final Location spawn;
	private final GameType gameType;

	private GameState gameState;
	private final ArrayList<UUID> players;
	private final ArrayList<UUID> spectators;
	private final HashMap<UUID, Team> teams;
	private Countdown countdown;
	private Game game;
	private Menu menu;

	public Arena(int id, Location spawn, GameType gameType) {
		this.id = id;
		this.spawn = spawn;
		this.gameType = gameType;

		this.gameState = GameState.WAITING;
		this.players = new ArrayList<>();
		this.spectators = new ArrayList<>();
		this.teams = new HashMap<>();
		this.countdown = new Countdown(this);
		this.game = gameType.getNewInstance(this);

		menu = new Menu("Team Selection", 3);
		for (int i = 0; i < getGameType().getNumTeams(); i++) {

			ArrayList<String> lore = new ArrayList<>();
			for (Player p : getPlayers(Team.values()[i])) {
				lore.add(Util.colorize("&7- &7" + p.getName()));
			}

			ItemStack item = new ItemBuilder(Team.values()[i].getIcon()).setItemName(Util.colorize(Team.values()[i].getDisplay() + " &7(" + getTeamCount(Team.values()[i]) + "/" + getGameType().getPlayersPerTeam() + ")")).setLore(lore).build();
			menu.addItem(item);
		}
	}

	public void start() {
		game.start();
	}

	public void reset(boolean kickPlayers) {
		if (kickPlayers) {
			setState(GameState.RESETTING);
			for (Player player : getPlayers()) {
				player.teleport(ConfigManager.getLobbySpawn());
				Util.resetPlayer(player);
				ScoreboardManager.removeScoreboard(player);
			}
			for (UUID uuid : spectators) {
				Bukkit.getPlayer(uuid).teleport(ConfigManager.getLobbySpawn());
				Util.resetPlayer(Bukkit.getPlayer(uuid));
				ScoreboardManager.removeScoreboard(Bukkit.getPlayer(uuid));
			}
			players.clear();
			teams.clear();
			spectators.clear();
		}
		if (countdown.isRunning()) {
			countdown.cancel();
		}
		countdown = new Countdown(this);
		game.unregister();
		game = gameType.getNewInstance(this);
		setState(GameState.WAITING);
	}

	public boolean addPlayer(Player player) {
		if (players.size() < gameType.getMaxPlayers()) {
			players.add(player.getUniqueId());
			player.teleport(spawn);
			Util.resetPlayer(player);
			ScoreboardManager.setScoreboard(player, Util.colorize("&e&l" + gameType.getDisplay().toUpperCase()),
					Util.colorize("&7" + Util.getDate()),
					"",
					Util.colorize("&fWaiting for players..."),
					" ",
					Util.colorize("&ewww.dartanetwork.net"));
			sendMessage(Util.colorize("&7" + player.getName() + " &ehas joined (&b" + players.size() + "&e/&b" + gameType.getMaxPlayers() + "&e)!"));

			TreeMultimap<Integer, Team> count = TreeMultimap.create();
			for (int i = 0; i < gameType.getNumTeams(); i++) {
				count.put(getTeamCount(Team.values()[i]), Team.values()[i]);
			}

			Team lowest = (Team) count.values().toArray()[0];
			setTeam(player, lowest);

			if (gameState == GameState.WAITING && numTeamsWithMoreThan(0) >= gameType.getMinTeams()) {
				countdown.start();
			}
			return true;
		}
		return false;
	}

	public void removePlayer(Player player) {
		players.remove(player.getUniqueId());
		spectators.remove(player.getUniqueId());
		player.teleport(ConfigManager.getLobbySpawn());
		sendMessage(Util.colorize("&7" + player.getName() + " &ehas quit!"));

		removeTeam(player);
		Util.resetPlayer(player);
		ScoreboardManager.removeScoreboard(player);

		if (gameState == GameState.COUNTDOWN && numTeamsWithMoreThan(0) < gameType.getMinTeams()) {
			sendMessage(Util.colorize("&cNot enough teams to continue, countdown stopped!"));
			reset(false);
			return;
		}
		if (gameState == GameState.LIVE && numTeamsWithMoreThan(0) < gameType.getMinTeams()) {
			game.end();
		}
	}

	public ArrayList<Player> getAlivePlayers() {
		ArrayList<Player> alive = new ArrayList<>();
		for (Player player : getPlayers()) {
			if (!spectators.contains(player.getUniqueId())) {
				alive.add(player);
			}
		}
		return alive;
	}

	public int numTeamsWithMoreThan(int x) {
		int i = 0;
		for (Team team : Team.values()) {
			if (getPlayers(team).size() > x) {
				i++;
			}
		}
		return i;
	}

	public ArrayList<Team> getTeamsWithMoreThan(int x) {
		ArrayList<Team> te = new ArrayList<>();
		for (Team team : Team.values()) {
			if (getPlayers(team).size() > x) {
				te.add(team);
			}
		}
		return te;
	}

	public ArrayList<Team> getAliveTeams() {
		ArrayList<Team> te = new ArrayList<>();
		for (Team team : Team.values()) {
			for (Player player : getPlayers(team)) {
				if (getAlivePlayers().contains(player)) {
					if (!te.contains(team)) {
						te.add(team);
					}
				}
			}
		}
		return te;
	}

	public void addSpectator(Player player) {
		player.setGameMode(GameMode.SPECTATOR);
		spectators.add(player.getUniqueId());
		player.sendMessage(Util.colorize("&cYou are now spectating!"));
	}

	public void removeSpectator(Player player) {
		Util.resetPlayer(player);
		player.teleport(ConfigManager.getLobbySpawn());
		spectators.remove(player.getUniqueId());
		player.sendMessage(Util.colorize("&cYou are no longer spectating!"));
	}

	public ArrayList<Player> getSpectators() {
		ArrayList<Player> pl = new ArrayList<>();
		for (UUID uuid : spectators) {
			pl.add(Bukkit.getPlayer(uuid));
		}
		return pl;
	}

	public ArrayList<Player> getAllPlayers() {
		ArrayList<Player> pl = new ArrayList<>(getAlivePlayers());
		pl.addAll(getSpectators());
		return pl;
	}

	public boolean sameTeam(Player x, Player y) {
		return getTeam(x) == getTeam(y);
	}

	public void setTeam(Player player, Team team) {
		removeTeam(player);
		teams.put(player.getUniqueId(), team);

		if (gameState == GameState.COUNTDOWN && numTeamsWithMoreThan(0) < gameType.getMinTeams()) {
			sendMessage(Util.colorize("&cNot enough teams to continue, countdown stopped!"));
			reset(false);
		}
		if (gameState == GameState.WAITING && numTeamsWithMoreThan(0) >= gameType.getMinTeams()) {
			countdown.start();
		}
	}

	public Team getTeam(Player player) {
		return teams.get(player.getUniqueId());
	}

	public void removeTeam(Player player) {
		if (teams.containsKey(player.getUniqueId())) {
			teams.remove(player.getUniqueId());
		}
	}

	public int getTeamCount(Team team) {
		int amount = 0;
		for (Team t : teams.values()) {
			if (t == team) {
				amount++;
			}
		}
		return amount;
	}

	public Countdown getCountdown() {
		return countdown;
	}

	public ArrayList<Player> getPlayers(Team team) {
		ArrayList<Player> teamPlayers = new ArrayList<>();
		for (UUID uuid : teams.keySet()) {
			if (teams.get(uuid).equals(team)) {
				teamPlayers.add(Bukkit.getPlayer(uuid));
			}
		}
		return teamPlayers;
	}

	public void sendMessage(String message) {
		for (Player player : getAllPlayers()) {
			player.sendMessage(message);
		}
	}

	public void sendTitle(String title, String subtitle) {
		for (UUID uuid : players) {
			Bukkit.getPlayer(uuid).sendTitle(title, subtitle);
		}
	}

	public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		for (UUID uuid : players) {
			Bukkit.getPlayer(uuid).sendTitle(title, subtitle, fadeIn, stay, fadeOut);
		}
	}

	public Menu getMenu() {
		return menu;
	}

	public int getID() {
		return id;
	}

	public GameState getState() {
		return gameState;
	}

	public GameType getGameType() {
		return gameType;
	}

	public Game getGame() {
		return game;
	}

	public ArrayList<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<>();
		for (UUID uuid : this.players) {
			players.add(Bukkit.getPlayer(uuid));
		}
		return players;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setState(GameState gameState) {
		this.gameState = gameState;
	}
}
