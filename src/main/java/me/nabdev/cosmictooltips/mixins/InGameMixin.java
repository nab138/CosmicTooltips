package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import me.nabdev.cosmictooltips.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGame.class)
public class InGameMixin extends GameState {

    @Unique
    private final Vector2 cosmicTooltips$mouse = new Vector2();


    @Inject(method = "render", at = @At("TAIL"))
    private void render(CallbackInfo ci) {
        if(TooltipUtils.tooltip == null) return;

        this.uiViewport.apply(false);
        cosmicTooltips$mouse.set((float) Gdx.input.getX(), (float)Gdx.input.getY());
        this.uiViewport.unproject(cosmicTooltips$mouse);
        batch.setProjectionMatrix(this.uiCamera.combined);
        batch.begin();

        TooltipUtils.tooltip.drawBackground(this.uiViewport, batch, cosmicTooltips$mouse.x, cosmicTooltips$mouse.y);
        TooltipUtils.tooltip.drawText(this.uiViewport, batch);

        batch.end();
    }
}
