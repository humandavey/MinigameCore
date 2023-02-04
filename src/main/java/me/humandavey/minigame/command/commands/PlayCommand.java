package me.humandavey.minigame.command.commands;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.command.Command;
import me.humandavey.minigame.game.GameType;
import me.humandavey.minigame.menu.Menu;
import me.humandavey.minigame.util.Util;
import me.humandavey.minigame.util.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class PlayCommand extends Command {

	public PlayCommand() {
		super("play", new String[]{"join"}, "Join a game!");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (Minigame.getInstance().getArenaManager().getArena(player) == null) {
			if (args.length == 0) {
				// Supports up to 9 games right now
				Menu menu = new Menu("Game Selector", 3);

				int numGames = GameType.values().length;
				int slot = 4 - (numGames / 2);

				for (GameType type : GameType.values()) {
					ItemStack item = new ItemBuilder(type.getIcon()).setItemName(Util.colorize("&e" + type.getDisplay())).setLore("", type.getFormattedDescription(ChatColor.GRAY)).addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build();
					menu.setItemAt(1, slot, item);
					slot++;
				}

				// TODO: ADD GAME JOINING FUNCTIONALITY

				menu.open(player);
			}
		} else {
			player.sendMessage(Util.colorize("&cYou cannot use this command while in a game!"));
		}
	}
}
