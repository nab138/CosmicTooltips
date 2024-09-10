package me.nabdev.cosmictooltips.api;

import finalforeach.cosmicreach.items.ItemStack;

public interface ITooltipItem extends ITooltip {

    String getItemID();
    String getTooltipText(ItemStack itemStack);
}
