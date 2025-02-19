package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;
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

@Mixin(value = ItemSlotWidget.class)
public abstract class ItemSlotWidgetMixin extends Stack {
    @Shadow
    protected abstract boolean isHoveredOver();

    @Shadow
    public abstract ItemSlot getItemSlot();

    @Unique
    private TooltipUIElement cosmicTooltips$tooltip;

    @Unique
    private String cosmicTooltips$rawId = "";

    @Unique
    int cosmicTooltips$prevAdvanced = 0;

    @Unique
    boolean cosmicTooltips$wasBritish = false;

    @Inject(method = "act", at = @At("TAIL"))
    private void act(float delta, CallbackInfo ci) {
        if (this.getItemSlot().getItemStack() == null) {
            cosmicTooltips$rawId = "";
            if (cosmicTooltips$tooltip != null) {
                cosmicTooltips$tooltip = null;
            }
            return;
        } else {
            int shouldBeAdvanced = TooltipUtils.advanced;
            ItemStack itemStack = this.getItemSlot().getItemStack();
            boolean hasCustomItem = itemStack.getItem() instanceof IModItem && ToolTipFactory.hasCustomTooltipItem(itemStack);
            boolean hasCustomBlock = itemStack.getItem() instanceof ItemBlock && ToolTipFactory.hasCustomTooltipBlock((ItemBlock) itemStack.getItem());
            if (hasCustomItem || hasCustomBlock || !cosmicTooltips$rawId.equals(itemStack.getItem().getID()) || cosmicTooltips$prevAdvanced != shouldBeAdvanced || cosmicTooltips$wasBritish != TooltipUtils.british) {
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
                cosmicTooltips$tooltip = new TooltipUIElement(name, id, other, TooltipUtils.getPosition());
            }
        }

        if (this.isHoveredOver()) {
            cosmicTooltips$tooltip.setPosition(TooltipUtils.getPosition());
        }
    }

    @Inject(method = "drawTooltip", at = @At("HEAD"), cancellable = true)
    private void dontDrawTooltip(Batch batch, CallbackInfo ci) {
        if (cosmicTooltips$tooltip != null && this.isHoveredOver()) cosmicTooltips$tooltip.draw(batch, 1);
        ci.cancel();
    }
}
