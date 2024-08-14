package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemSlotWidget;
import finalforeach.cosmicreach.ui.*;
import me.nabdev.cosmictooltips.ITooltipElem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemSlotWidget.class)
public abstract class ItemStackWidgetMixin {
    @Shadow
    protected abstract boolean isHoveredOver();

    @Shadow ItemSlot itemSlot;

    @Unique
    private UIElement cosmicTooltips$tooltip;

    @Unique
    private Vector2 cosmicTooltips$dim;

    @Unique
    private String cosmicTooltips$name;

    @Inject(method = "act", at = @At("TAIL"))
    private void drawTooltip(CallbackInfo ci) {
        Viewport viewport = GameState.IN_GAME.ui.uiViewport;

        if (this.itemSlot.itemStack == null){
            cosmicTooltips$name = null;
            cosmicTooltips$dim = null;
            if(this.cosmicTooltips$tooltip != null) cosmicTooltips$hideTooltip();
            return;
        } else if(cosmicTooltips$dim == null){
            cosmicTooltips$name = this.itemSlot.itemStack.getItem().getID().split("\\[")[0];
            cosmicTooltips$dim = new Vector2();
            FontRenderer.getTextDimensions(viewport, cosmicTooltips$name, cosmicTooltips$dim);
        }

        if (this.isHoveredOver()) {
            float x = ((float) Gdx.input.getX() / Gdx.graphics.getWidth()) * viewport.getWorldWidth() - (viewport.getWorldWidth() / 2.0F) + ((cosmicTooltips$dim.x + 8) / 2.0F);
            float y =  ((float) Gdx.input.getY() / Gdx.graphics.getHeight()) * viewport.getWorldHeight() - (viewport.getWorldHeight() / 2.0F) - ((cosmicTooltips$dim.y + 8) / 2.0F);
            if(this.cosmicTooltips$tooltip == null) {
                this.cosmicTooltips$tooltip = new UIElement(x, y, cosmicTooltips$dim.x + 8, cosmicTooltips$dim.y + 8);
                this.cosmicTooltips$tooltip.setText(cosmicTooltips$name);
                this.cosmicTooltips$tooltip.show();
            } else {
                this.cosmicTooltips$tooltip.setX(x);
                this.cosmicTooltips$tooltip.setY(y);
            }
            ((ITooltipElem)GameState.IN_GAME).cosmicTooltips$setTooltip(this.cosmicTooltips$tooltip);
        } else if(this.cosmicTooltips$tooltip != null) {
            cosmicTooltips$hideTooltip();
        }
    }
    
    @Unique
    private void cosmicTooltips$hideTooltip(){
        this.cosmicTooltips$tooltip.hide();
        ((ITooltipElem)GameState.IN_GAME).cosmicTooltips$setTooltip(null);
        this.cosmicTooltips$tooltip = null;
    }
}
