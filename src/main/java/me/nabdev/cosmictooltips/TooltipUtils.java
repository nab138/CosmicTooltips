package me.nabdev.cosmictooltips;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.ui.UIElement;

public class TooltipUtils {
    public static int padding = 8;
    private static UIElement tooltip;

    private static UIElement hotbarTooltip;

    private static long lastHotbarTime = 0;

    public static void hideTooltip(){
        if(tooltip != null) tooltip.hide();
        tooltip = null;
    }

    public static void hideHotbarTooltip(){
        if(hotbarTooltip != null) hotbarTooltip.hide();
        hotbarTooltip = null;
    }
    public static Vector2 getPosition(Viewport viewport, Vector2 dim) {
        Vector2 paddedDim = new Vector2(dim.x + padding, dim.y + padding);
        float x = ((float) Gdx.input.getX() / Gdx.graphics.getWidth()) * viewport.getWorldWidth() - (viewport.getWorldWidth() / 2.0F);
        float y =  ((float) Gdx.input.getY() / Gdx.graphics.getHeight()) * viewport.getWorldHeight() - (viewport.getWorldHeight() / 2.0F);

        if(x + paddedDim.x + padding > viewport.getWorldWidth() / 2.0F) x -= paddedDim.x / 2;
        else x += paddedDim.x / 2;
        if(y - paddedDim.y - padding < -viewport.getWorldHeight() / 2.0F) y += paddedDim.y / 2;
        else y -= paddedDim.y / 2;

        return new Vector2(x, y);
    }

    public static void setHotbarTooltip(UIElement tooltip){
        TooltipUtils.hotbarTooltip = tooltip;
        lastHotbarTime = System.currentTimeMillis();
    }

    public static void setTooltip(UIElement tooltip){
        TooltipUtils.tooltip = tooltip;
    }

    public static UIElement getTooltip(){
        return tooltip;
    }

    public static UIElement getHotbarTooltip(){
        return hotbarTooltip;
    }

    public static long getHotbarTime(){
        return lastHotbarTime;
    }
}
