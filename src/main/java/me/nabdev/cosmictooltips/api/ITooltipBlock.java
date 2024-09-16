package me.nabdev.cosmictooltips.api;

import finalforeach.cosmicreach.blocks.BlockState;

public interface ITooltipBlock extends ITooltip{

    String getBlockID();
    String getTooltipText(BlockState blockState);
}
