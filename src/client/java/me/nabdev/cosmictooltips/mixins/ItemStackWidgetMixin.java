package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.ui.widgets.ItemStackWidget;
import io.github.puzzle.cosmic.api.item.IItem;
import me.nabdev.cosmictooltips.api.ToolTipFactory;
import me.nabdev.cosmictooltips.utils.TooltipUIElement;
import me.nabdev.cosmictooltips.utils.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackWidget.class)
public abstract class ItemStackWidgetMixin extends Stack {
    @Shadow
    public ItemStack itemStack;

    @Shadow
    public boolean isHovered;

    @Unique
    private TooltipUIElement cosmicTooltips$tooltip;

    @Unique
    private String cosmicTooltips$rawId = "";

    @Unique
    int cosmicTooltips$prevAdvanced = 0;

    @Unique
    boolean cosmicTooltips$wasBritish = false;

    @Inject(method="drawItem", at=@At("HEAD"))
    private void hoveredOver(Viewport itemViewport, CallbackInfo ci) {
        if (!this.isHovered || Gdx.input.isCursorCatched()){
            if (cosmicTooltips$tooltip != null && itemStack.getItem().getID().equals(cosmicTooltips$rawId)) {
                cosmicTooltips$tooltip.remove();
                cosmicTooltips$tooltip = null;
            }
            return;
        }
        if (itemStack == null) {
            cosmicTooltips$rawId = "";
            if (cosmicTooltips$tooltip != null) {
                cosmicTooltips$tooltip.remove();
                cosmicTooltips$tooltip = null;
            }
        } else {
            int shouldBeAdvanced = TooltipUtils.advanced;
            boolean hasCustomItem = itemStack.getItem() instanceof IItem && ToolTipFactory.hasCustomTooltipItem(itemStack);
            boolean hasCustomBlock = itemStack.getItem() instanceof ItemBlock && ToolTipFactory.hasCustomTooltipBlock((ItemBlock) itemStack.getItem());
            if (cosmicTooltips$tooltip == null || hasCustomItem || hasCustomBlock || !cosmicTooltips$rawId.equals(itemStack.getItem().getID()) || cosmicTooltips$prevAdvanced != shouldBeAdvanced || cosmicTooltips$wasBritish != TooltipUtils.british) {
                if (cosmicTooltips$tooltip != null) {
                    cosmicTooltips$tooltip.remove();
                    cosmicTooltips$tooltip = null;
                }
                cosmicTooltips$prevAdvanced = shouldBeAdvanced;
                cosmicTooltips$wasBritish = TooltipUtils.british;
                cosmicTooltips$rawId = itemStack.getItem().getID();
                if (cosmicTooltips$rawId == null) {
                    cosmicTooltips$rawId = "";
                    return;
                }
                String tag = null;
                if (hasCustomItem) {
                    tag = ToolTipFactory.getCustomTooltipItem(itemStack);
                } else if (hasCustomBlock) {
                    tag = ToolTipFactory.getCustomTooltipBlock(((ItemBlock) itemStack.getItem()).getBlockState());
                }
                String name = TooltipUtils.parseName(itemStack.getItem().getName());
                String id = TooltipUtils.parseId(itemStack.getItem().getID());
                String other = TooltipUtils.parseOther(itemStack.getItem().getID(), tag);
                cosmicTooltips$tooltip = new TooltipUIElement(name, id, other, TooltipUtils.getPosition(), (ItemStackWidget)((Object)this));
            }

            if (cosmicTooltips$tooltip != null) {
                TooltipUtils.curTooltip = cosmicTooltips$tooltip;
                if(GameState.currentGameState == GameState.IN_GAME) cosmicTooltips$tooltip.setPosition(TooltipUtils.getPosition());
            }
        }
    }

    @Inject(method = "drawTooltip", at = @At("HEAD"), cancellable = true)
    private void dontDrawTooltip(Batch batch, CallbackInfo ci) {
        ci.cancel();
    }
}
