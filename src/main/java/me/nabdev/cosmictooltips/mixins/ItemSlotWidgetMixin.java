package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.PuzzleRegistries;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.widgets.ItemSlotWidget;
import me.nabdev.cosmictooltips.ITooltipBlock;
import me.nabdev.cosmictooltips.ITooltipItem;
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

    @Shadow
    public ItemSlot itemSlot;

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

    @Inject(method = "act", at = @At("TAIL"))
    private void drawTooltip(CallbackInfo ci) {
        Viewport viewport = GameState.IN_GAME.ui.uiViewport;


        if (this.itemSlot.itemStack == null) {
            cosmicTooltips$name = null;
            cosmicTooltips$dim = null;
            if (cosmicTooltips$tooltip != null) {
                TooltipUtils.hideTooltip();
                cosmicTooltips$tooltip = null;
            }
            return;
        } else {
            boolean shouldBeAdvanced = TooltipUtils.shouldBeAdvanced();
            if (cosmicTooltips$dim == null || !cosmicTooltips$rawName.equals(this.itemSlot.itemStack.getItem().getID()) || cosmicTooltips$wasAdvanced != shouldBeAdvanced) {
                cosmicTooltips$wasAdvanced = shouldBeAdvanced;
                cosmicTooltips$rawName = this.itemSlot.itemStack.getItem().getID();
//                if (this.itemSlot.itemStack.getItem() instanceof IModItem) {
//                    DataTag<?> tag = ((IModItem)this.itemSlot.itemStack.getItem()).getTagManifest().getTag("tooltip");
//                    cosmicTooltips$name = TooltipUtils.parseID(cosmicTooltips$rawName, shouldBeAdvanced, tag);
//                }
                if (this.itemSlot.itemStack.getItem() instanceof ITooltipItem) {
                    String additionalText = ((ITooltipItem) this.itemSlot.itemStack.getItem()).getTooltipText();
                    cosmicTooltips$name = TooltipUtils.parseID(cosmicTooltips$rawName, shouldBeAdvanced, additionalText);
                } else if (this.itemSlot.itemStack.getItem() instanceof ItemBlock itemBlock &&
                        PuzzleRegistries.BLOCKS.contains(Identifier.fromString(itemBlock.getBlockState().getBlockId())) &&
                        PuzzleRegistries.BLOCKS.get(Identifier.fromString(itemBlock.getBlockState().getBlockId())) instanceof ITooltipBlock tooltipItem
                ) {
                    String additionalText = tooltipItem.getTooltipText(itemBlock.getBlockState());
                    cosmicTooltips$name = TooltipUtils.parseID(cosmicTooltips$rawName, shouldBeAdvanced, additionalText);
                } else cosmicTooltips$name = TooltipUtils.parseID(cosmicTooltips$rawName, shouldBeAdvanced, null);

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
}
