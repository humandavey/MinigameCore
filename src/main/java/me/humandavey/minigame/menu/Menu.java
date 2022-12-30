package me.humandavey.minigame.menu;

import me.humandavey.minigame.Minigame;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Menu implements Listener {

	private final Inventory inventory;
	private final String name;
	private final int rows;
	private Consumer<InventoryClickEvent> onClick;

	public Menu(String name, int rows) {
		this.inventory = Bukkit.createInventory(null, rows * 9, name);
		this.name = name;
		this.rows = rows;
		Minigame.getInstance().getServer().getPluginManager().registerEvents(this, Minigame.getInstance());
	}

	public Menu(String name, HashMap<Integer, ItemStack> items) {
		this.name = name;
		int highest = 0;
		for (int i : items.keySet()) {
			if (i > highest)
				highest = i;
		}
		int rows = (int) Math.ceil(highest / 9.0);
		inventory = Bukkit.createInventory(null, rows, name);
		for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
			setItemAt(item.getKey(), item.getValue());
		}
		this.rows = rows;
		Minigame.getInstance().getServer().getPluginManager().registerEvents(this, Minigame.getInstance());
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (canCallConsumer(event)) {
			onClick.accept(event);
		}
	}

	public boolean canCallConsumer(InventoryClickEvent event) {
		if (event.getInventory().equals(inventory)) {
			return onClick != null;
		}
		return false;
	}

	public void setOnClick(Consumer<InventoryClickEvent> onClick) {
		this.onClick = onClick;
	}

	public Consumer<InventoryClickEvent> getOnClick() {
		return onClick;
	}

	public ItemStack getItemAt(int i) {
		return inventory.getItem(i);
	}

	public ItemStack getItemAt(int row, int column) {
		return inventory.getItem((9 * row) + column);
	}

	public void addItem(ItemStack item) {
		inventory.addItem(item);
		update();
	}

	public ItemStack setItemAt(int i, ItemStack item) {
		ItemStack last = getItemAt(i);
		inventory.setItem(i, item);
		update();
		return last;
	}

	public ItemStack setItemAt(int row, int column, ItemStack item) {
		ItemStack last = getItemAt(row, column);
		inventory.setItem((9 * row) + column, item);
		update();
		return last;
	}

	private void update() {
		for (Player player : getViewers()) {
			player.openInventory(inventory);
		}
	}

	public Inventory getInventory() {
		return inventory;
	}

	public String getName() {
		return name;
	}

	public int getRows() {
		return rows;
	}

	public ArrayList<Player> getViewers() {
		ArrayList<Player> players = new ArrayList<>();
		for (HumanEntity e : inventory.getViewers()) {
			players.add((Player) e);
		}
		return players;
	}

	public void open(Player player) {
		player.openInventory(inventory);
	}
}
