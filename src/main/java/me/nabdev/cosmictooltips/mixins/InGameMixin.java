package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.ui.UIElement;
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
        if(TooltipUtils.getTooltip() == null && TooltipUtils.getHotbarTooltip() == null) return;

        this.uiViewport.apply(false);
        cosmicTooltips$mouse.set((float) Gdx.input.getX(), (float)Gdx.input.getY());
        this.uiViewport.unproject(cosmicTooltips$mouse);
        batch.setProjectionMatrix(this.uiCamera.combined);
        batch.begin();

        cosmicTooltips$renderTooltip(TooltipUtils.getTooltip(), true, 1.0F);
        long curTime = System.currentTimeMillis();
        long hotbarTime = TooltipUtils.getHotbarTime();
        // For the first 3 seconds, opacity is one
        // Then fade out for one second
        float opacity = 1.0F;
        if(curTime - hotbarTime > 3000 && curTime - hotbarTime < 3500){
            opacity = 1.0F - ((curTime - hotbarTime - 3000) / 500.0F);
        } else if(curTime - hotbarTime > 3500){
            TooltipUtils.setHotbarTooltip(null);
        }
        cosmicTooltips$renderTooltip(TooltipUtils.getHotbarTooltip(), false, opacity);

        batch.end();
    }

    @Unique
    private void cosmicTooltips$renderTooltip(UIElement tooltip, boolean drawBG, float textOpacity){
        if(tooltip == null) return;

        if(drawBG) tooltip.drawBackground(this.uiViewport, batch, cosmicTooltips$mouse.x, cosmicTooltips$mouse.y);

        tooltip.setTextColor(new Color(1.0F, 1.0F, 1.0F, textOpacity));
        tooltip.drawText(this.uiViewport, batch);
        tooltip.setTextColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
    }
}
