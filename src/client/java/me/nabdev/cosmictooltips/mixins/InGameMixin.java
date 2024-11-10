package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.BlockSelection;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.settings.GameSetting;
import me.nabdev.cosmictooltips.utils.TooltipUIElement;
import me.nabdev.cosmictooltips.utils.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGame.class)
public class InGameMixin extends GameState {

    @Unique
    private final Vector2 cosmicTooltips$mouse = new Vector2();

    @Unique
    private Vector2 cosmicTooltips$dim;

    @Unique
    private String cosmicTooltips$name;

    @Unique
    private TooltipUIElement cosmicTooltips$tooltip;

    @Unique
    private String cosmicTooltips$rawName;

    @Unique
    private boolean cosmicTooltips$wasBritish = false;

    @Inject(method="update", at=@At("TAIL"))
    private void update(CallbackInfo ci) {
        BlockState result = BlockSelection.getBlockLookingAt();
        if (result == null) {
            cosmicTooltips$name = null;
            cosmicTooltips$rawName = null;
            if (cosmicTooltips$tooltip != null) {
                cosmicTooltips$tooltip.hide();
                cosmicTooltips$tooltip = null;
            }
            return;
        } else if (cosmicTooltips$dim == null || cosmicTooltips$rawName == null || !cosmicTooltips$rawName.equals(result.getItem().getID()) || cosmicTooltips$wasBritish != TooltipUtils.british) {
            cosmicTooltips$wasBritish = TooltipUtils.british;
            cosmicTooltips$rawName = result.getItem().getID();
            cosmicTooltips$name = TooltipUtils.parseID(result.getItem().getName(), cosmicTooltips$rawName, true, null);
            cosmicTooltips$dim = TooltipUtils.getTextDims(GameState.IN_GAME.ui.uiViewport, cosmicTooltips$name);
            cosmicTooltips$tooltip = null;
        }

        if (cosmicTooltips$tooltip == null) {
            Viewport viewport = GameState.IN_GAME.ui.uiViewport;
            cosmicTooltips$tooltip = new TooltipUIElement(0, -(viewport.getWorldHeight() / 2) + (cosmicTooltips$dim.y / 2) + TooltipUtils.padding * 3, cosmicTooltips$dim.x + TooltipUtils.padding * 2.5f, cosmicTooltips$dim.y + TooltipUtils.padding * 2.5f);
        }
        cosmicTooltips$tooltip.setText(cosmicTooltips$name);
        cosmicTooltips$tooltip.show();
        TooltipUtils.setWailaTooltip(cosmicTooltips$tooltip);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(CallbackInfo ci) {
        if(Gdx.input.isKeyPressed(Input.Keys.F3)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                TooltipUtils.british = !TooltipUtils.british;
                GameSetting.setSetting("britishTooltips", TooltipUtils.british);
                GameSetting.saveSettings();
                Chat.MAIN_CLIENT_CHAT.addMessage( null, TooltipUtils.british ? "[CosmicTooltips] WARNING: BRITISH MODE ENABLED" : "[CosmicTooltips] British mode disabled");
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.H)){
                TooltipUtils.advanced = !TooltipUtils.advanced;
                GameSetting.setSetting("advancedTooltips", TooltipUtils.advanced);
                GameSetting.saveSettings();
                Chat.MAIN_CLIENT_CHAT.addMessage(null, TooltipUtils.advanced ? "[CosmicTooltips] Advanced tooltips enabled" : "[CosmicTooltips] Advanced tooltips disabled");
            }
        }
        if (TooltipUtils.getTooltip() == null && TooltipUtils.getHotbarTooltip() == null && TooltipUtils.getWailaTooltip() == null)
            return;

        this.uiViewport.apply(false);
        cosmicTooltips$mouse.set((float) Gdx.input.getX(), (float) Gdx.input.getY());
        this.uiViewport.unproject(cosmicTooltips$mouse);
        batch.setProjectionMatrix(this.uiCamera.combined);
        batch.begin();

        cosmicTooltips$renderTooltip(TooltipUtils.getTooltip(), true, 1.0F);
        cosmicTooltips$renderTooltip(TooltipUtils.getWailaTooltip(), true, 1.0F);

        long curTime = System.currentTimeMillis();
        long hotbarTime = TooltipUtils.getHotbarTime();

        float opacity = 1.0F;
        if (curTime - hotbarTime > 3000 && curTime - hotbarTime < 3500) {
            opacity = 1.0F - ((curTime - hotbarTime - 3000) / 500.0F);
        } else if (curTime - hotbarTime > 3500) {
            TooltipUtils.setHotbarTooltip(null);
        }
        cosmicTooltips$renderTooltip(TooltipUtils.getHotbarTooltip(), false, opacity);

        batch.end();
    }

    @Unique
    private void cosmicTooltips$renderTooltip(TooltipUIElement tooltip, boolean drawBG, float textOpacity) {
        if (tooltip == null) return;

        if (drawBG) tooltip.drawBackground(this.uiViewport, batch, cosmicTooltips$mouse.x, cosmicTooltips$mouse.y);

        tooltip.drawText(this.uiViewport, batch, textOpacity, new Color[]{
                new Color(Color.WHITE),
                new Color(Color.LIGHT_GRAY),
                new Color(Color.GRAY),
        });
    }
}
