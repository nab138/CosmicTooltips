package me.nabdev.cosmictooltips.mixins;

import finalforeach.cosmicreach.items.screens.BaseItemScreen;
import me.nabdev.cosmictooltips.utils.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseItemScreen.class)
public class BaseItemScreenMixin {
    @Inject(method = "onRemove", at = @At("TAIL"))
    private void onRemove(CallbackInfo ci) {
        TooltipUtils.hideTooltip();
    }
}
