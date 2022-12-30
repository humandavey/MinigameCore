package me.humandavey.minigame.team;

import org.bukkit.Material;

public enum Team {

	RED("&cRed", Material.RED_WOOL),
	BLUE("&9Blue", Material.BLUE_WOOL),
	GREEN("&aGreen", Material.LIME_WOOL),
	YELLOW("&eYellow", Material.YELLOW_WOOL),
	ORANGE("&6Orange", Material.ORANGE_WOOL),
	PINK("&dPink", Material.PINK_WOOL),
	MAGENTA("&5Magenta", Material.PURPLE_WOOL),
	WHITE("&fWhite", Material.WHITE_WOOL);

	private final String display;
	private final Material icon;

	Team(String display, Material icon) {
		this.display = display;
		this.icon = icon;
	}

	public String getColor() {
		return display.substring(0, 2);
	}

	public String getName() {
		return display.substring(2);
	}

	public String getDisplay() {
		return display;
	}

	public Material getIcon() {
		return icon;
	}
}
