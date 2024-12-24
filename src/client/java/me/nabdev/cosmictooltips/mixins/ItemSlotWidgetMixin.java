package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.widgets.ItemSlotWidget;
import me.nabdev.cosmictooltips.api.ToolTipFactory;
import me.nabdev.cosmictooltips.utils.TooltipUIElement;
import me.nabdev.cosmictooltips.utils.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemSlotWidget.class)
public abstract class ItemSlotWidgetMixin extends Stack {
    @Shadow
    protected abstract boolean isHoveredOver();

    @Shadow public abstract ItemSlot getItemSlot();

    @Unique
    private TooltipUIElement cosmicTooltips$tooltip;

    @Unique
    private Vector2 cosmicTooltips$dim;

    @Unique
    private String cosmicTooltips$name;

    @Unique
    String cosmicTooltips$rawName;

    @Unique
    boolean cosmicTooltips$wasAdvanced = false;

    @Unique
    boolean cosmicTooltips$wasBritish = false;

    @Inject(method = "act", at = @At("TAIL"))
    private void drawTooltip(CallbackInfo ci) {
        Viewport viewport = GameState.IN_GAME.ui.uiViewport;


        if (this.getItemSlot().itemStack == null) {
            cosmicTooltips$name = null;
            cosmicTooltips$dim = null;
            if (cosmicTooltips$tooltip != null) {
                TooltipUtils.hideTooltip();
                cosmicTooltips$tooltip = null;
            }
            return;
        } else {
            boolean shouldBeAdvanced = TooltipUtils.shouldBeAdvanced();
            if (ToolTipFactory.hasCustomTooltipBlock((ItemBlock) this.getItemSlot().itemStack.getItem()) || cosmicTooltips$dim == null || !cosmicTooltips$rawName.equals(this.getItemSlot().itemStack.getItem().getID()) || cosmicTooltips$wasAdvanced != shouldBeAdvanced || cosmicTooltips$wasBritish != TooltipUtils.british) {
                cosmicTooltips$wasAdvanced = shouldBeAdvanced;
                cosmicTooltips$wasBritish = TooltipUtils.british;
                cosmicTooltips$rawName = this.getItemSlot().itemStack.getItem().getID();
                if(this.getItemSlot().itemStack.getItem() instanceof IModItem && ToolTipFactory.hasCustomTooltipItem(this.getItemSlot().itemStack)) {
                    String additionalText = ToolTipFactory.getCustomTooltipItem(this.getItemSlot().itemStack);
                    cosmicTooltips$name = TooltipUtils.parseID(this.getItemSlot().itemStack.getName(), cosmicTooltips$rawName, shouldBeAdvanced, additionalText);
                } else if(this.getItemSlot().itemStack.getItem() instanceof ItemBlock && ToolTipFactory.hasCustomTooltipBlock((ItemBlock) this.getItemSlot().itemStack.getItem())) {
                    String additionalText = ToolTipFactory.getCustomTooltipBlock(((ItemBlock) this.getItemSlot().itemStack.getItem()).getBlockState());
                    cosmicTooltips$name = TooltipUtils.parseID(this.getItemSlot().itemStack.getName(), cosmicTooltips$rawName, shouldBeAdvanced, additionalText);
                } else cosmicTooltips$name = TooltipUtils.parseID(this.getItemSlot().itemStack.getName(), cosmicTooltips$rawName, shouldBeAdvanced, null);


                cosmicTooltips$dim = new Vector2();
                FontRenderer.getTextDimensions(viewport, cosmicTooltips$name, cosmicTooltips$dim);
            }
        }

        if (this.isHoveredOver()) {
            Vector2 coords = TooltipUtils.getPosition(viewport, cosmicTooltips$dim);
            if (this.cosmicTooltips$tooltip == null || !this.cosmicTooltips$tooltip.text.equals(cosmicTooltips$name)) {
                cosmicTooltips$tooltip = new TooltipUIElement(coords.x, coords.y, cosmicTooltips$dim.x + 8, cosmicTooltips$dim.y + 8);
                cosmicTooltips$tooltip.setText(cosmicTooltips$name);
            } else {
                cosmicTooltips$tooltip.setX(coords.x);
                cosmicTooltips$tooltip.setY(coords.y);
            }
            cosmicTooltips$tooltip.show();
            TooltipUtils.setTooltip(cosmicTooltips$tooltip);
        } else if (cosmicTooltips$tooltip != null) {
            TooltipUtils.hideTooltip();
            cosmicTooltips$tooltip = null;
        }
    }

    @Inject(method="drawTooltip", at=@At("HEAD"), cancellable = true)
    private void dontDrawTooltip(CallbackInfo ci) {
        ci.cancel();
    }
}
