package me.nabdev.cosmictooltips.api;

import finalforeach.cosmicreach.items.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToolTipFactory {

    public static final HashMap<String, ITooltip> initializeToolTips = new HashMap<>();
    List<ITooltip> newToolTips = new ArrayList<>();

    public ToolTipFactory() {}

    public static boolean hasCustomTooltip(ItemStack itemStack) {
        return initializeToolTips.containsKey(itemStack.getItem().getID().split("\\[")[0]);
    }

    public static String getCustomTooltip(ItemStack itemStack) {
        return initializeToolTips.get(itemStack.getItem().getID().split("\\[")[0]).getTooltipText(itemStack);
    }

    public void loadCustomTooltip () {
        for(ITooltip tooltip : newToolTips){
            initializeToolTips.put(tooltip.getItemID(), tooltip);
        }
    }

    public void addTooltip(ITooltip tooltip) {
        newToolTips.add(tooltip);
    }
}
