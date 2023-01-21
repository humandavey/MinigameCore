package me.humandavey.minigame.command.commands;

import me.humandavey.minigame.Minigame;
import me.humandavey.minigame.command.Command;
import me.humandavey.minigame.game.GameType;
import me.humandavey.minigame.instance.Arena;
import me.humandavey.minigame.menu.Menu;
import me.humandavey.minigame.util.Util;
import me.humandavey.minigame.util.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayCommand extends Command {

	public PlayCommand() {
		super("play", new String[]{"join"}, "Join a game!");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (Minigame.getInstance().getArenaManager().getArena(player) == null) {
			if (args.length == 0) {
				Menu menu = new Menu("Game Selector", 3);

				// TODO: Make this scale with new games from GameType

				ArrayList<String> l = new ArrayList<>(List.of(GameType.WATERCLUTCHER.getFormattedDescription(ChatColor.GRAY)));
				l.add(0, "");
				ItemStack waterClutcher = new ItemBuilder(GameType.WATERCLUTCHER.getIcon()).setItemName(Util.colorize("&e" + GameType.WATERCLUTCHER.getDisplay())).setLore(l).build();
				menu.setItemAt(4, waterClutcher);

				menu.setOnClick(event -> {
					event.setCancelled(true);
					if (event.getCurrentItem() != null) {
						if (event.getCurrentItem().equals(waterClutcher)) {
							Arena arena = Minigame.getInstance().getArenaManager().findArena(GameType.WATERCLUTCHER);
							arena.addPlayer(player);
							player.closeInventory();
						}
					}
				});

				menu.open(player);
			}
		} else {
			player.sendMessage(Util.colorize("&cYou cannot use this command while in a game!"));
		}
	}
}
