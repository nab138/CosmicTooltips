package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.ui.UIElement;
import me.nabdev.cosmictooltips.ITooltipElem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGame.class)
public class InGameMixin extends GameState implements ITooltipElem {

    @Unique
    private final Vector2 cosmicTooltips$mouse = new Vector2();

    @Unique
    public UIElement cosmicTooltips$tooltipElem;


    @Inject(method = "render", at = @At("TAIL"))
    private void render(CallbackInfo ci) {
        if(this.cosmicTooltips$tooltipElem == null) return;

        this.uiViewport.apply(false);
        cosmicTooltips$mouse.set((float) Gdx.input.getX(), (float)Gdx.input.getY());
        this.uiViewport.unproject(cosmicTooltips$mouse);
        batch.setProjectionMatrix(this.uiCamera.combined);
        batch.begin();

        cosmicTooltips$tooltipElem.drawBackground(this.uiViewport, batch, cosmicTooltips$mouse.x, cosmicTooltips$mouse.y);
        cosmicTooltips$tooltipElem.drawText(this.uiViewport, batch);

        batch.end();
    }

    @Override
    public void cosmicTooltips$setTooltip(UIElement tooltip) {
        this.cosmicTooltips$tooltipElem = tooltip;
    }
}
