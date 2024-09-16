package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.Hotbar;
import finalforeach.cosmicreach.items.ItemSlot;
import me.nabdev.cosmictooltips.utils.TooltipUIElement;
import me.nabdev.cosmictooltips.utils.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Hotbar.class)
public class HotbarMixin {
    @Shadow
    private ItemSlot selectedSlot;

    @Unique
    private String cosmicTooltips$name;

    @Unique
    private TooltipUIElement cosmicTooltips$tooltip;

    @Unique
    private String cosmicTooltips$rawName;

    @Inject(method = "selectSlot", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/rendering/items/ItemRenderer;popUpHeldItem()V", shift = At.Shift.AFTER))
    public void selectSlot(int slotNum, CallbackInfo ci) {
        if (selectedSlot.itemStack == null) {
            cosmicTooltips$name = null;
            cosmicTooltips$rawName = null;
            if (cosmicTooltips$tooltip != null) {
                cosmicTooltips$tooltip.hide();
                cosmicTooltips$tooltip = null;
            }
            return;
        } else if (cosmicTooltips$rawName == null || !cosmicTooltips$rawName.equals(selectedSlot.itemStack.getItem().getID())) {
            cosmicTooltips$rawName = selectedSlot.itemStack.getItem().getID();
            cosmicTooltips$name = TooltipUtils.british ? selectedSlot.itemStack.getName() : selectedSlot.itemStack.getName().replace("inium", "inum");
        }

        if (cosmicTooltips$tooltip == null) {
            Viewport viewport = GameState.IN_GAME.ui.uiViewport;
            cosmicTooltips$tooltip = new TooltipUIElement(0, (viewport.getWorldHeight() / 2) - 64, viewport.getWorldWidth() - (TooltipUtils.padding * 2), 0);
        }
        cosmicTooltips$tooltip.setText(cosmicTooltips$name);
        cosmicTooltips$tooltip.show();
        TooltipUtils.setHotbarTooltip(cosmicTooltips$tooltip);
    }

    @Inject(method = "dropSelectedItem", at = @At("HEAD"))
    public void dropSelectedItem(CallbackInfoReturnable<Boolean> cir) {
        if (cosmicTooltips$tooltip != null) {
            cosmicTooltips$name = null;
            cosmicTooltips$tooltip = null;
            TooltipUtils.hideHotbarTooltip();
        }
    }
}
