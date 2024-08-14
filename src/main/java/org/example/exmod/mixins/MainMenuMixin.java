package org.example.exmod.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemSlotWidget;
import finalforeach.cosmicreach.ui.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(ItemSlotWidget.class)
public abstract class MainMenuMixin {
    @Shadow
    protected abstract boolean isHoveredOver();

    @Shadow ItemSlot itemSlot;

    @Unique
    private UIElement testMod$tooltip;

    @Unique
    private Viewport testMod$viewport;

    @Unique
    private Vector2 testMod$dim;

    @Unique
    private String testMod$name;

    @Inject(method = "act", at = @At("TAIL"))
    private void drawTooltip(CallbackInfo ci) {
        if(this.testMod$viewport == null) {
            try {
                Field uiField = InGame.class.getDeclaredField("ui");
                uiField.setAccessible(true);
                UI ui = (UI) uiField.get(GameState.IN_GAME);

                Field uiViewportField = UI.class.getDeclaredField("uiViewport");
                uiViewportField.setAccessible(true);

                this.testMod$viewport = (Viewport) uiViewportField.get(ui);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
        if (this.itemSlot.itemStack == null){
            if(this.testMod$tooltip != null) {
                this.testMod$tooltip.hide();
                GameState.IN_GAME.uiObjects.removeValue(this.testMod$tooltip, true);
                this.testMod$tooltip = null;
            }
            return;
        }
        if (this.isHoveredOver()) {
            // Get the cursor position
            if(testMod$dim == null){
                testMod$name = this.itemSlot.itemStack.getItem().getID().split("\\[")[0];
                testMod$dim = new Vector2();
                FontRenderer.getTextDimensions(testMod$viewport, testMod$name, testMod$dim);
            }
            float x = ((float) Gdx.input.getX() / Gdx.graphics.getWidth()) * testMod$viewport.getWorldWidth() - (testMod$viewport.getWorldWidth() / 2.0F) + ((testMod$dim.x + 8) / 2.0F);
            float y =  ((float) Gdx.input.getY() / Gdx.graphics.getHeight()) * testMod$viewport.getWorldHeight() - (testMod$viewport.getWorldHeight() / 2.0F) - ((testMod$dim.y + 8) / 2.0F);
            if(this.testMod$tooltip == null) {
                this.testMod$tooltip = new UIElement(x, y, testMod$dim.x + 8, testMod$dim.y + 8);
                this.testMod$tooltip.setText(testMod$name);
                this.testMod$tooltip.show();
                GameState.IN_GAME.uiObjects.add(this.testMod$tooltip);
            } else {
                this.testMod$tooltip.setX(x);
                this.testMod$tooltip.setY(y);
            }
        } else if(this.testMod$tooltip != null) {
            this.testMod$tooltip.hide();
            GameState.IN_GAME.uiObjects.removeValue(this.testMod$tooltip, true);
            this.testMod$tooltip = null;
        }
    }
}
