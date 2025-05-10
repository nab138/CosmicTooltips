package me.nabdev.cosmictooltips.mixins;

import finalforeach.cosmicreach.ui.widgets.CRLabel;
import finalforeach.cosmicreach.ui.widgets.ItemCatalogWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemCatalogWidget.class)
public class ItemCatalogWidgetMixin {
    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/ui/widgets/CRLabel;setVisible(Z)V"))
    private void draw(CRLabel instance, boolean b) {
        instance.setVisible(false);
    }
}
