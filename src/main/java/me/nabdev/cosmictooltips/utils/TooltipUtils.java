package me.nabdev.cosmictooltips.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.ui.FontRenderer;
import joptsimple.internal.Strings;

public class TooltipUtils {
    public static int padding = 8;
    private static TooltipUIElement tooltip;

    private static TooltipUIElement hotbarTooltip;

    private static long lastHotbarTime = 0;

    private static TooltipUIElement wailaTooltip;

    public static void hideTooltip() {
        if (tooltip != null) tooltip.hide();
        tooltip = null;
    }

    public static void hideHotbarTooltip() {
        if (hotbarTooltip != null) hotbarTooltip.hide();
        hotbarTooltip = null;
    }

    public static Vector2 getPosition(Viewport viewport, Vector2 dim) {
        Vector2 paddedDim = new Vector2(dim.x + padding, dim.y + padding);
        float x = ((float) Gdx.input.getX() / Gdx.graphics.getWidth()) * viewport.getWorldWidth() - (viewport.getWorldWidth() / 2.0F);
        float y = ((float) Gdx.input.getY() / Gdx.graphics.getHeight()) * viewport.getWorldHeight() - (viewport.getWorldHeight() / 2.0F);

        if (x + paddedDim.x + padding > viewport.getWorldWidth() / 2.0F) x -= paddedDim.x / 2;
        else x += paddedDim.x / 2;
        if (y - paddedDim.y - padding < -viewport.getWorldHeight() / 2.0F) y += paddedDim.y / 2;
        else y -= paddedDim.y / 2;

        return new Vector2(x, y);
    }

    public static void setHotbarTooltip(TooltipUIElement tooltip) {
        TooltipUtils.hotbarTooltip = tooltip;
        lastHotbarTime = System.currentTimeMillis();
    }

    public static void setWailaTooltip(TooltipUIElement tooltip) {
        TooltipUtils.wailaTooltip = tooltip;
    }

    public static void setTooltip(TooltipUIElement tooltip) {
        TooltipUtils.tooltip = tooltip;
    }

    public static TooltipUIElement getTooltip() {
        return tooltip;
    }

    public static TooltipUIElement getHotbarTooltip() {
        return hotbarTooltip;
    }

    public static TooltipUIElement getWailaTooltip() {
        return wailaTooltip;
    }

    public static long getHotbarTime() {
        return lastHotbarTime;
    }

    public static Vector2 getTextDims(Viewport viewport, String name) {
        float y = 0;
        float largestX = 0;
        for (String line : name.split("\n")) {
            Vector2 dim = new Vector2();
            FontRenderer.getTextDimensions(viewport, line, dim);
            if (dim.x > largestX) largestX = dim.x;
            y += dim.y;
        }
        return new Vector2(largestX, y);
    }

    public static String parseID(String id, boolean isAdvanced, String tag) {
        String[] split = id.split("\\[");

        if (isAdvanced && (split.length > 1 || tag != null)) {
            String name = split[0];
            String[] data = null;
            if(split.length > 1) data = split[1].substring(0, split[1].length() - 1).split(",");
            return name + (data != null ? "\n" + Strings.join(data, "\n") : "") + (tag != null ? "\n" + tag : "");
        } else {
            return split[0];
        }
    }

    public static boolean shouldBeAdvanced() {
        return Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
    }
}
