package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.Hotbar;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.ui.GameStyles;
import me.nabdev.cosmictooltips.utils.TooltipUIElement;
import me.nabdev.cosmictooltips.utils.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hotbar.class)
public class HotbarMixin {
    @Shadow
    private ItemSlot selectedSlot;

    @Unique
    private String cosmicTooltips$rawName;

    @Unique
    private Label cosmicTooltips$label;


    @Inject(method = "selectSlot", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/rendering/items/ItemRenderer;popUpHeldItem()V", shift = At.Shift.AFTER))
    public void selectSlot(short slotNum, CallbackInfo ci) {
        if (selectedSlot.getItemStack() == null) {
            if (cosmicTooltips$label != null) {
                cosmicTooltips$label.remove();
                cosmicTooltips$label = null;
                cosmicTooltips$rawName = null;
            }
            return;
        }
        if (cosmicTooltips$rawName == null || !cosmicTooltips$rawName.equals(selectedSlot.getItemStack().getItem().getID())) {
            cosmicTooltips$rawName = selectedSlot.getItemStack().getItem().getID();
            String name = TooltipUtils.british ? selectedSlot.getItemStack().getName() : selectedSlot.getItemStack().getName().replace("inium", "inum");
            if (cosmicTooltips$label != null) {
                cosmicTooltips$label.remove();
            }
            cosmicTooltips$label = new Label(name, GameStyles.styleText) {
                private float lifetime;

                @Override
                public void act(float delta) {
                    super.act(delta);
                    lifetime += delta;
                    float timeUntilFade = 2.5f;
                    float fadeTime = 0.5f;
                    if (lifetime > timeUntilFade) {
                        setColor(1, 1, 1, 1 - (lifetime - timeUntilFade) / fadeTime);
                    }
                }
            };
            Stage stage = TooltipUtils.getStage();
            cosmicTooltips$label.setPosition(stage.getWidth() / 2 - cosmicTooltips$label.getPrefWidth() / 2, 50);
            TooltipUtils.getStage().addActor(cosmicTooltips$label);
        }

//        if (cosmicTooltips$tooltip == null) {
//            Viewport viewport = GameState.IN_GAME.ui.uiViewport;
//            //cosmicTooltips$tooltip = new TooltipUIElement(0, (viewport.getWorldHeight() / 2) - 64);
//        }
        //cosmicTooltips$tooltip.setText(cosmicTooltips$name);
        //cosmicTooltips$tooltip.show();
    }

    @Inject(method = "dropSelectedItem", at = @At("HEAD"))
    public void dropSelectedItem(CallbackInfo ci) {
        if (cosmicTooltips$label != null) {
            cosmicTooltips$label.remove();
            cosmicTooltips$label = null;
            cosmicTooltips$rawName = null;
        }
    }
}
