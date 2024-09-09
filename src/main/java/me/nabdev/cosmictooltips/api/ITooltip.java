package me.nabdev.cosmictooltips.api;

import finalforeach.cosmicreach.items.ItemStack;

public interface ITooltip {

    String getItemID();
    String getTooltipText(ItemStack itemStack);
}
