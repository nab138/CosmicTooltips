package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemCatalog;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.UIElement;
import me.nabdev.cosmictooltips.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCatalog.class)
public class ItemCatalogMixin {
    @Unique
    private Vector2 cosmicTooltips$dim;

    @Unique
    private String cosmicTooltips$name;

    @Unique
    private UIElement cosmicTooltips$tooltip;

    @Unique
    private String cosmicTooltips$rawName;

    @Inject(method = "drawItems", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/utils/viewport/Viewport;setScreenBounds(IIII)V", shift = At.Shift.AFTER, ordinal = 0))
    private void hoveredOver(Viewport uiViewport, CallbackInfo ci, @Local ItemStack itemStack) {
        Viewport viewport = GameState.IN_GAME.ui.uiViewport;

        if(cosmicTooltips$dim == null || !cosmicTooltips$rawName.equals(itemStack.getItem().getID())){
            cosmicTooltips$rawName = itemStack.getItem().getID();
            cosmicTooltips$name = itemStack.getItem().getID().split("\\[")[0];
            cosmicTooltips$dim = new Vector2();
            cosmicTooltips$tooltip = null;
            FontRenderer.getTextDimensions(viewport, cosmicTooltips$name, cosmicTooltips$dim);
        }

        Vector2 coords = TooltipUtils.getPosition(viewport, cosmicTooltips$dim);
        if(this.cosmicTooltips$tooltip == null) {
            cosmicTooltips$tooltip = new UIElement(coords.x, coords.y, cosmicTooltips$dim.x + 8, cosmicTooltips$dim.y + 8);
            cosmicTooltips$tooltip.setText(cosmicTooltips$name);
        } else {
            cosmicTooltips$tooltip.setX(coords.x);
            cosmicTooltips$tooltip.setY(coords.y);
        }
        TooltipUtils.tooltip = cosmicTooltips$tooltip;
        cosmicTooltips$tooltip.show();
    }

    @Inject(method = "drawItems", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/utils/viewport/Viewport;setScreenBounds(IIII)V", shift = At.Shift.AFTER, ordinal = 1))
    private void notHovered(Viewport uiViewport, CallbackInfo ci, @Local ItemStack itemStack) {
        if(cosmicTooltips$tooltip != null && itemStack.getItem().getID().equals(cosmicTooltips$rawName)) {
            TooltipUtils.hideTooltip();
            cosmicTooltips$tooltip = null;
        }
    }

}
