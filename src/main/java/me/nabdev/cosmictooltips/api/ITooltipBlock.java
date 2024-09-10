package me.nabdev.cosmictooltips.api;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.items.ItemStack;

public interface ITooltipBlock extends ITooltip{

    String getBlockID();
    String getTooltipText(BlockState blockState);
}
