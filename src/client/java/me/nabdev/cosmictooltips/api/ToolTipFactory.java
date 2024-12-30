package me.nabdev.cosmictooltips.api;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToolTipFactory {

    public static final HashMap<String, ITooltipItem> initializeToolTipsItems = new HashMap<>();
    public static final HashMap<String, ITooltipBlock> initializeToolTipsBlocks = new HashMap<>();
    List<ITooltip> newToolTips = new ArrayList<>();

    public ToolTipFactory() {}

    public static boolean hasCustomTooltipItem(ItemStack itemStack) {
        return initializeToolTipsItems.containsKey(itemStack.getItem().getID().split("\\[")[0]);
    }

    public static boolean hasCustomTooltipBlock(ItemBlock itemBlock) {
        return initializeToolTipsBlocks.containsKey(itemBlock.getID().split("\\[")[0]);
    }

    public static String getCustomTooltipItem(ItemStack itemStack) {
        return initializeToolTipsItems.get(itemStack.getItem().getID().split("\\[")[0]).getTooltipText(itemStack);
    }

    public static String getCustomTooltipBlock(BlockState blockState) {
        return initializeToolTipsBlocks.get(blockState.getItem().getID().split("\\[")[0]).getTooltipText(blockState);
    }

    public void loadCustomTooltip () {
        for(ITooltip tooltip : newToolTips){
            if(tooltip instanceof ITooltipItem tooltipItem)  {
                initializeToolTipsItems.put(tooltipItem.getItemID(),tooltipItem);
                continue;
            }
            if(tooltip instanceof ITooltipBlock tooltipBlock) {
                initializeToolTipsBlocks.put(tooltipBlock.getBlockID(), tooltipBlock);
            }
        }
    }

    @SuppressWarnings("unused")
    public void addTooltip(ITooltip tooltip) {
        newToolTips.add(tooltip);
    }
}
