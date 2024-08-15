package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemSlotWidget;
import finalforeach.cosmicreach.ui.*;
import me.nabdev.cosmictooltips.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemSlotWidget.class)
public abstract class ItemSlotWidgetMixin {
    @Shadow
    protected abstract boolean isHoveredOver();

    @Shadow ItemSlot itemSlot;

    @Unique
    private UIElement cosmicTooltips$tooltip;

    @Unique
    private Vector2 cosmicTooltips$dim;

    @Unique
    private String cosmicTooltips$name;

    @Unique String cosmicTooltips$rawName;

    @Inject(method = "act", at = @At("TAIL"))
    private void drawTooltip(CallbackInfo ci) {
        Viewport viewport = GameState.IN_GAME.ui.uiViewport;

        if (this.itemSlot.itemStack == null){
            cosmicTooltips$name = null;
            cosmicTooltips$dim = null;
            if(cosmicTooltips$tooltip != null) TooltipUtils.hideTooltip();
            return;
        } else if(cosmicTooltips$dim == null || !cosmicTooltips$rawName.equals(this.itemSlot.itemStack.getItem().getID())) {
            cosmicTooltips$rawName = this.itemSlot.itemStack.getItem().getID();
            cosmicTooltips$name = this.itemSlot.itemStack.getItem().getID().split("\\[")[0];
            cosmicTooltips$dim = new Vector2();
            FontRenderer.getTextDimensions(viewport, cosmicTooltips$name, cosmicTooltips$dim);
        }

        if (this.isHoveredOver()) {
            Vector2 coords = TooltipUtils.getPosition(viewport, cosmicTooltips$dim);
            if(this.cosmicTooltips$tooltip == null) {
                cosmicTooltips$tooltip = new UIElement(coords.x, coords.y, cosmicTooltips$dim.x + 8, cosmicTooltips$dim.y + 8);
                cosmicTooltips$tooltip.setText(cosmicTooltips$name);
                cosmicTooltips$tooltip.show();
            } else {
                cosmicTooltips$tooltip.setX(coords.x);
                cosmicTooltips$tooltip.setY(coords.y);
            }
            TooltipUtils.tooltip = cosmicTooltips$tooltip;
        } else if(cosmicTooltips$tooltip != null) {
            TooltipUtils.hideTooltip();
            cosmicTooltips$tooltip = null;
        }
    }

    @Inject(method="onInputEvent", at=@At(value = "FIELD", target = "Lfinalforeach/cosmicreach/items/ItemSlotWidget;canSpreadItemTo:Z", shift = At.Shift.AFTER, ordinal = 1))
    private void onInputEvent(InputEvent inputEvent, CallbackInfoReturnable<Boolean> cir){
        System.out.println("Input event");
    }
}
