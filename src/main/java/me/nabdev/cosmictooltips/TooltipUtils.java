package me.nabdev.cosmictooltips;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.ui.UIElement;

public class TooltipUtils {
    public static UIElement tooltip;

    public static void hideTooltip(){
        if(tooltip != null) tooltip.hide();
        tooltip = null;
    }
    public static Vector2 getPosition(Viewport viewport, Vector2 dim) {
        float x = ((float) Gdx.input.getX() / Gdx.graphics.getWidth()) * viewport.getWorldWidth() - (viewport.getWorldWidth() / 2.0F) + ((dim.x + 8) / 2.0F);
        float y =  ((float) Gdx.input.getY() / Gdx.graphics.getHeight()) * viewport.getWorldHeight() - (viewport.getWorldHeight() / 2.0F) - ((dim.y + 8) / 2.0F);
        return new Vector2(x, y);
    }
}
