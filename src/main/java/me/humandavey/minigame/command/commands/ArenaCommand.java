package me.humandavey.minigame.command.commands;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.command.Command;
import me.humandavey.minigame.game.GameState;
import me.humandavey.minigame.game.GameType;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.menu.Menu;
import me.humandavey.minigame.team.Team;
import me.humandavey.minigame.util.Util;
import me.humandavey.minigame.util.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArenaCommand extends Command {

	public ArenaCommand() {
		super("arena", null, "Join or leave an arena!");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
			player.sendMessage(Util.colorize("&aAvailable Arenas:"));
			for (Arena arena : Minigame.getInstance().getArenaManager().getArenas()) {
				player.sendMessage(Util.colorize("&a- " + arena.getID() + " (" + arena.getState().name() + ")"));
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
			Arena arena = Minigame.getInstance().getArenaManager().getArena(player);
			if (arena != null) {
				arena.removePlayer(player);
				player.sendMessage(Util.colorize("&cYou have left the arena!"));
			} else {
				player.sendMessage(Util.colorize("&cYou are not in an arena!"));
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
			if (Minigame.getInstance().getArenaManager().getArena(player) != null) {
				player.sendMessage(Util.colorize("&cYou are already in an arena!"));
				return;
			}
			int id;
			try {
				id = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				GameType type;
				try {
					type = GameType.valueOf(args[1]);
				} catch (IllegalArgumentException ex) {
					player.sendMessage(Util.colorize("&cInvalid ID or game type!"));
					return;
				}
				Arena arena = Minigame.getInstance().getArenaManager().findArena(type);
				if (arena != null) {
					arena.addPlayer(player);
				} else {
					player.sendMessage(Util.colorize("&cNo available arenas found!"));
				}
				return;
			}
			if (id >= 0 && id < Minigame.getInstance().getArenaManager().getArenas().size()) {
				Arena arena = Minigame.getInstance().getArenaManager().getArena(id);
				if (arena.getState() == GameState.WAITING || arena.getState() == GameState.COUNTDOWN) {
					if (!arena.addPlayer(player)) {
						player.sendMessage(Util.colorize("&cThis arena is full!"));
					}
				} else {
					player.sendMessage(Util.colorize("&cThis arena has already started!"));
				}
			} else {
				player.sendMessage(Util.colorize("&cThis is not a valid arena id!"));
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("team")) {
			Arena arena = Minigame.getInstance().getArenaManager().getArena(player);
			if (arena != null && (arena.getState() == GameState.WAITING || arena.getState() == GameState.COUNTDOWN)) {
				Menu menu = new Menu("Team Selection", 3);
				for (int i = 0; i < arena.getGameType().getNumTeams(); i++) {

					ArrayList<String> lore = new ArrayList<>();
					for (Player p : arena.getPlayers(Team.values()[i])) {
						lore.add(Util.colorize("&7- &7" + p.getName()));
					}

					ItemStack item = new ItemBuilder(Team.values()[i].getIcon()).setItemName(Util.colorize(Team.values()[i].getDisplay() + " &7(" + arena.getTeamCount(Team.values()[i]) + "/" + arena.getGameType().getPlayersPerTeam() + ")")).setLore(lore).build();
					menu.addItem(item);
				}
				menu.setOnClick(event -> {
					event.setCancelled(true);
					if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
					Arena teamArena = Minigame.getInstance().getArenaManager().getArena((Player) event.getWhoClicked());
					Team clickedTeam = null;
					for (Team team : Team.values()) {
						if (event.getCurrentItem().getItemMeta().getDisplayName().contains(team.getName())) {
							clickedTeam = team;
							break;
						}
					}
					if (clickedTeam != null && Minigame.getInstance().getArenaManager().getArena((Player) event.getWhoClicked()) != null) {
						if (teamArena.getTeamCount(clickedTeam) < teamArena.getGameType().getPlayersPerTeam()) {
							Team lastTeam = teamArena.getTeam(player);
							if (lastTeam == clickedTeam) {
								player.sendMessage(Util.colorize("&cYou are already on this team!"));
								return;
							}
							teamArena.setTeam((Player) event.getWhoClicked(), clickedTeam);
							event.getWhoClicked().sendMessage(Util.colorize("&aYou have been placed on " + clickedTeam.getDisplay() + " &ateam!"));

							ArrayList<String> lore = new ArrayList<>();
							for (Player p : arena.getPlayers(clickedTeam)) {
								lore.add(Util.colorize("&7- &7" + p.getName()));
							}

							ArrayList<String> lastLore = new ArrayList<>();
							for (Player p : arena.getPlayers(lastTeam)) {
								lastLore.add(Util.colorize("&7- &7" + p.getName()));
							}

							menu.setItemAt(List.of(Team.values()).indexOf(lastTeam), new ItemBuilder(lastTeam.getIcon()).setItemName(Util.colorize(lastTeam.getDisplay() + " &7(" + arena.getTeamCount(lastTeam) + "/" + arena.getGameType().getPlayersPerTeam() + ")")).setLore(lastLore).build());
							menu.setItemAt(event.getRawSlot(), new ItemBuilder(clickedTeam.getIcon()).setItemName(Util.colorize(clickedTeam.getDisplay() + " &7(" + arena.getTeamCount(clickedTeam) + "/" + arena.getGameType().getPlayersPerTeam() + ")")).setLore(lore).build());
						} else {
							event.getWhoClicked().sendMessage(Util.colorize("&cThis team is full!"));
						}
					}
				});
				menu.open(player);
			} else {
				player.sendMessage(Util.colorize("&cYou cannot do this right now!"));
			}
		} else {
			player.sendMessage(Util.colorize("&cInvalid Usage: /arena <list|leave|team|join> [id]"));
		}
	}
}
