package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.game.items.IModItem;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemCatalog;
import finalforeach.cosmicreach.items.ItemStack;
import me.nabdev.cosmictooltips.api.ToolTipFactory;
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
    private TooltipUIElement cosmicTooltips$tooltip;

    @Unique
    private String cosmicTooltips$rawId = "";

    @Unique
    boolean cosmicTooltips$wasAdvanced = false;

    @Unique
    private Batch cosmicTooltips$batch = new SpriteBatch();

    @Unique
    boolean cosmicTooltips$wasBritish = false;

    @Inject(method = "drawItems", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/utils/viewport/Viewport;setScreenBounds(IIII)V", shift = At.Shift.AFTER, ordinal = 0))
    private void hoveredOver(Viewport uiViewport, CallbackInfo ci, @Local ItemStack itemStack) {
        if (itemStack == null) {
            cosmicTooltips$rawId = "";
            if (cosmicTooltips$tooltip != null) {
                cosmicTooltips$tooltip.remove();
                cosmicTooltips$tooltip = null;
            }
        } else {
            boolean shouldBeAdvanced = TooltipUtils.shouldBeAdvanced();
            boolean hasCustomItem = itemStack.getItem() instanceof IModItem && ToolTipFactory.hasCustomTooltipItem(itemStack);
            boolean hasCustomBlock = itemStack.getItem() instanceof ItemBlock && ToolTipFactory.hasCustomTooltipBlock((ItemBlock) itemStack.getItem());
            if (hasCustomItem || hasCustomBlock  || !cosmicTooltips$rawId.equals(itemStack.getItem().getID()) || cosmicTooltips$wasAdvanced != shouldBeAdvanced || cosmicTooltips$wasBritish != TooltipUtils.british) {
                if(cosmicTooltips$tooltip != null) {
                    cosmicTooltips$tooltip.remove();
                    cosmicTooltips$tooltip = null;
                }
                cosmicTooltips$wasAdvanced = shouldBeAdvanced;
                cosmicTooltips$wasBritish = TooltipUtils.british;
                cosmicTooltips$rawId = itemStack.getItem().getID();
                if(cosmicTooltips$rawId == null) {
                    cosmicTooltips$rawId = "";
                    return;
                }
                String tag = null;
                if(hasCustomItem) {
                    tag = ToolTipFactory.getCustomTooltipItem(itemStack);
                } else if(hasCustomBlock) {
                    tag = ToolTipFactory.getCustomTooltipBlock(((ItemBlock) itemStack.getItem()).getBlockState());
                }
                String name = TooltipUtils.parseName(itemStack.getItem().getName());
                String id = TooltipUtils.parseId(itemStack.getItem().getID());
                String other = TooltipUtils.parseOther(itemStack.getItem().getID(), tag);
                cosmicTooltips$tooltip = new TooltipUIElement(name, id, other, TooltipUtils.getPosition());
                TooltipUtils.getStage().addActor(cosmicTooltips$tooltip);
            }

            if(cosmicTooltips$tooltip != null) {
                cosmicTooltips$tooltip.setPosition(TooltipUtils.getPosition());
            }
        }
    }

    @Inject(method = "drawItems", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/utils/viewport/Viewport;setScreenBounds(IIII)V", shift = At.Shift.AFTER, ordinal = 1))
    private void notHovered(Viewport uiViewport, CallbackInfo ci, @Local ItemStack itemStack) {
        if (cosmicTooltips$tooltip != null && itemStack.getItem().getID().equals(cosmicTooltips$rawId)) {
            cosmicTooltips$tooltip.remove();
            cosmicTooltips$tooltip = null;
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(Viewport uiViewport, ShapeRenderer shapeRenderer, CallbackInfo ci) {
        if (!this.isShown() && cosmicTooltips$tooltip != null) {
            cosmicTooltips$tooltip.remove();
            cosmicTooltips$tooltip = null;
        }
    }
}
