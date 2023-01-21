package me.humandavey.minigame.util.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

	private final ItemStack item;
	private final ItemMeta meta;

	public ItemBuilder(Material material) {
		item = new ItemStack(material);
		meta = item.getItemMeta();
	}

	public ItemStack build() {
		item.setItemMeta(meta);
		return item;
	}

	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}

	public ItemBuilder addItemFlags(ItemFlag... flags) {
		meta.addItemFlags(flags);
		return this;
	}

	public ItemBuilder addItemFlag(ItemFlag flag) {
		meta.addItemFlags(flag);
		return this;
	}

	public ItemBuilder setMaterial(Material material) {
		item.setType(material);
		return this;
	}

	public ItemBuilder setItemName(String name) {
		meta.setDisplayName(name);
		return this;
	}

	public ItemBuilder setUnbreakable(boolean unbreakable) {
		meta.setUnbreakable(unbreakable);
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		meta.addEnchant(enchantment, level, true);
		return this;
	}

	public ItemBuilder setLocalizedName(String localizedName) {
		meta.setLocalizedName(localizedName);
		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		meta.setLore(lore);
		return this;
	}

	public ItemBuilder setLore(String... lore) {
		List<String> l = new ArrayList<>(Arrays.asList(lore));
		meta.setLore(l);
		return this;
	}

	public ItemBuilder setLore(String prefix, String... lore) {
		List<String> l = new ArrayList<>(Arrays.asList(lore));
		l.add(0, prefix);
		meta.setLore(l);
		return this;
	}
}