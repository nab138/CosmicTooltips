package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemCatalog;
import finalforeach.cosmicreach.items.ItemStack;
import me.nabdev.cosmictooltips.utils.TooltipUIElement;
import me.nabdev.cosmictooltips.utils.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCatalog.class)
public abstract class ItemCatalogMixin {
    @Shadow
    public abstract boolean isShown();

    @Unique
    private Vector2 cosmicTooltips$dim;

    @Unique
    private String cosmicTooltips$name;

    @Unique
    private TooltipUIElement cosmicTooltips$tooltip;

    @Unique
    private String cosmicTooltips$rawName;

    @Unique
    boolean cosmicTooltips$wasAdvanced = false;

    @Inject(method = "drawItems", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/utils/viewport/Viewport;setScreenBounds(IIII)V", shift = At.Shift.AFTER, ordinal = 0))
    private void hoveredOver(Viewport uiViewport, CallbackInfo ci, @Local ItemStack itemStack) {
        Viewport viewport = GameState.IN_GAME.ui.uiViewport;

        boolean shouldBeAdvanced = TooltipUtils.shouldBeAdvanced();
        if (cosmicTooltips$dim == null || !cosmicTooltips$rawName.equals(itemStack.getItem().getID()) || cosmicTooltips$wasAdvanced != shouldBeAdvanced) {
            cosmicTooltips$wasAdvanced = shouldBeAdvanced;
            cosmicTooltips$rawName = itemStack.getItem().getID();
            cosmicTooltips$name = TooltipUtils.parseID(itemStack.getName(), cosmicTooltips$rawName, shouldBeAdvanced, null);
            cosmicTooltips$dim = TooltipUtils.getTextDims(viewport, cosmicTooltips$name);
            cosmicTooltips$tooltip = null;
        }


        Vector2 coords = TooltipUtils.getPosition(viewport, cosmicTooltips$dim);
        if (this.cosmicTooltips$tooltip == null) {
            cosmicTooltips$tooltip = new TooltipUIElement(coords.x, coords.y, cosmicTooltips$dim.x + 8, cosmicTooltips$dim.y + 8);
            cosmicTooltips$tooltip.setText(cosmicTooltips$name);
        } else {
            cosmicTooltips$tooltip.setX(coords.x);
            cosmicTooltips$tooltip.setY(coords.y);
        }
        TooltipUtils.setTooltip(cosmicTooltips$tooltip);
        cosmicTooltips$tooltip.show();
    }

    @Inject(method = "drawItems", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/utils/viewport/Viewport;setScreenBounds(IIII)V", shift = At.Shift.AFTER, ordinal = 1))
    private void notHovered(Viewport uiViewport, CallbackInfo ci, @Local ItemStack itemStack) {
        if (cosmicTooltips$tooltip != null && itemStack.getItem().getID().equals(cosmicTooltips$rawName)) {
            TooltipUtils.hideTooltip();
            cosmicTooltips$tooltip = null;
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(Viewport uiViewport, ShapeRenderer shapeRenderer, CallbackInfo ci) {
        if (!this.isShown() && cosmicTooltips$tooltip != null) {
            TooltipUtils.hideTooltip();
            cosmicTooltips$tooltip = null;
        }
    }
}
