package me.humandavey.minigame.command.commands;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.command.Command;
import me.humandavey.minigame.game.GameState;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.util.Util;
import org.bukkit.entity.Player;

public class StopCommand extends Command {

	public StopCommand() {
		super("forcestop", null, "Stop an arena in progess!");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (player.hasPermission("minigame.start") || player.isOp()) {
			if (args.length == 1) {
				int id;
				try {
					id = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					if (args[0].equalsIgnoreCase("now")) {
						Arena arena = Minigame.getInstance().getArenaManager().getArena(player);
						if (arena != null) {
							if (arena.getState() == GameState.LIVE) {
								player.sendMessage(Util.colorize("&aStopping arena... Use with caution!"));
								arena.reset(true);
							}
						}
					} else {
						player.sendMessage(Util.colorize("&cThat is not a valid arena ID!"));
					}
					return;
				}
				Arena arena = Minigame.getInstance().getArenaManager().getArena(id);
				if (arena != null) {
					if (arena.getState() == GameState.LIVE) {
						player.sendMessage(Util.colorize("&aStopping arena... Use with caution!"));
						arena.getGame().end();
					} else {
						player.sendMessage(Util.colorize("&cYou cannot do this right now!"));
					}
				} else {
					player.sendMessage(Util.colorize("&cThat is not a valid arena ID!"));
				}
			} else {
				Arena arena = Minigame.getInstance().getArenaManager().getArena(player);
				if (arena != null) {
					if (arena.getState() == GameState.LIVE) {
						player.sendMessage(Util.colorize("&aStopping arena... Use with caution!"));
						arena.getGame().end();
					} else {
						player.sendMessage(Util.colorize("&cYou cannot do this right now!"));
					}
				} else {
					player.sendMessage(Util.colorize("&cYou are not in an area!!"));
				}
			}
		} else {
			player.sendMessage(Util.colorize("&cYou don't have permission to run this command!"));
		}
	}
}