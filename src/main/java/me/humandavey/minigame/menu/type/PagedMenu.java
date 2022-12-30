package me.humandavey.minigame.menu.type;

import me.humandavey.minigame.menu.Menu;
import me.humandavey.minigame.util.Util;
import me.humandavey.minigame.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PagedMenu extends Menu {

	private final List<ItemStack> items;
	private final int page;

	public PagedMenu(String name, int rows, List<ItemStack> items, int page) {
		super(name + " - " + page, rows);
		this.page = page;
		this.items = items;

		ItemStack right = new ItemBuilder(Material.BARRIER).setItemName(Util.colorize("&cNext")).build();
		ItemStack left = new ItemBuilder(Material.BARRIER).setItemName(Util.colorize("&cBack")).build();

		if (isPageValid(items, page - 1, (rows * 9) - 2)) {
			left = new ItemBuilder(Material.ARROW).setItemName(Util.colorize("&eBack")).build();
		}
		if (isPageValid(items, page + 1, (rows * 9) - 2)) {
			right = new ItemBuilder(Material.ARROW).setItemName(Util.colorize("&eNext")).build();
		}

		setItemAt((rows * 9) - 1, right);
		setItemAt((rows * 9) - 9, left);

		for (ItemStack item : getPageItems(items, page, (rows * 9) - 2)) {
			addItem(item);
		}
	}

	public PagedMenu(String name, int rows, List<ItemStack> items, int page, Consumer<InventoryClickEvent> onClick) {
		super(name + " - " + page, rows);
		this.page = page;
		this.items = items;
		setOnClick(onClick);

		ItemStack right = new ItemBuilder(Material.BARRIER).setItemName(Util.colorize("&cNext")).build();
		ItemStack left = new ItemBuilder(Material.BARRIER).setItemName(Util.colorize("&cBack")).build();

		if (isPageValid(items, page - 1, (rows * 9) - 2)) {
			left = new ItemBuilder(Material.ARROW).setItemName(Util.colorize("&eBack")).build();
		}
		if (isPageValid(items, page + 1, (rows * 9) - 2)) {
			right = new ItemBuilder(Material.ARROW).setItemName(Util.colorize("&eNext")).build();
		}

		setItemAt((rows * 9) - 1, right);
		setItemAt((rows * 9) - 9, left);

		for (ItemStack item : getPageItems(items, page, (rows * 9) - 2)) {
			addItem(item);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory().equals(getInventory())) {
			if (event.getCurrentItem() == null) return;
			if (event.getCurrentItem().getItemMeta() == null) return;
			if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().getItemMeta().getDisplayName().contains("Back")) {
				if (getOnClick() == null) {
					new PagedMenu(getName().substring(0, getName().length() - 3 - String.valueOf(page).length()), getRows(), items, page - 1).open((Player) event.getWhoClicked());
				} else {
					new PagedMenu(getName().substring(0, getName().length() - 3 - String.valueOf(page).length()), getRows(), items, page - 1, getOnClick()).open((Player) event.getWhoClicked());
				}
				event.setCancelled(true);
			} else if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().getItemMeta().getDisplayName().contains("Next")) {
				if (getOnClick() == null) {
					new PagedMenu(getName().substring(0, getName().length() - 3 - String.valueOf(page).length()), getRows(), items, page + 1).open((Player) event.getWhoClicked());
				} else {
					new PagedMenu(getName().substring(0, getName().length() - 3 - String.valueOf(page).length()), getRows(), items, page + 1, getOnClick()).open((Player) event.getWhoClicked());
				}
				event.setCancelled(true);
			} else if (event.getCurrentItem().getType() == Material.BARRIER && (event.getCurrentItem().getItemMeta().getDisplayName().contains("Back")) || event.getCurrentItem().getItemMeta().getDisplayName().contains("Next")) {
				event.setCancelled(true);
			}
		}
	}

	@Override
	public boolean canCallConsumer(InventoryClickEvent event) {
		if (event.getCurrentItem() == null) return false;
		if (event.getCurrentItem().getItemMeta() == null) return false;
		if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().getItemMeta().getDisplayName().contains("Back")) {
			return false;
		} else if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().getItemMeta().getDisplayName().contains("Next")) {
			return false;
		} else if (event.getCurrentItem().getType() == Material.BARRIER && (event.getCurrentItem().getItemMeta().getDisplayName().contains("Back")) || event.getCurrentItem().getItemMeta().getDisplayName().contains("Next")) {
			return false;
		}
		return super.canCallConsumer(event);
	}

	public static ArrayList<ItemStack> getPageItems(List<ItemStack> items, int page, int spaces) {
		int upperBound = page * spaces;
		int lowerBound = upperBound - spaces;

		ArrayList<ItemStack> newItems = new ArrayList<>();
		for (int i = lowerBound; i < upperBound; i++) {
			try {
				newItems.add(items.get(i));
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}

		return newItems;
	}

	public static boolean isPageValid(List<ItemStack> items, int page, int spaces) {
		if (page <= 0) return false;

		int upperBound = page * spaces;
		int lowerBound = upperBound - spaces;

		return items.size() > lowerBound;
	}
}
