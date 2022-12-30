package me.humandavey.minigame.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends BukkitCommand {

	public Command(String command, String[] aliases, String description) {
		super(command);
		if (aliases != null) {
			this.setAliases(Arrays.asList(aliases));
		}
		if (description != null) {
			this.setDescription(description);
		}

		try {
			Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			field.setAccessible(true);
			CommandMap map = (CommandMap) field.get(Bukkit.getServer());
			map.register(command, this);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final boolean execute(CommandSender commandSender, String s, String[] strings) {
		if (commandSender instanceof Player) {
			execute((Player) commandSender, strings);
		} else {
			execute((ConsoleCommandSender) commandSender, strings);
		}
		return false;
	}

	public void execute(Player player, String[] args) {
		player.sendMessage(ChatColor.RED + "Players cannot use this command!");
	}

	public void execute(ConsoleCommandSender console, String[] args) {
		console.sendMessage(ChatColor.RED + "Console cannot use this command!");
	}

	@Override
	public final List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		return tabComplete(sender, args);
	}

	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}
}