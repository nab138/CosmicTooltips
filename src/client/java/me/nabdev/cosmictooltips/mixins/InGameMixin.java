package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import finalforeach.cosmicreach.BlockSelection;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.settings.GameSetting;
import me.nabdev.cosmictooltips.utils.IStageGetter;
import me.nabdev.cosmictooltips.utils.InWorldUIElement;
import me.nabdev.cosmictooltips.utils.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGame.class)
public class InGameMixin extends GameState implements IStageGetter {

    @Shadow
    public BlockSelection blockSelection;

    @Unique
    private InWorldUIElement cosmicTooltips$tooltip;

    @Unique
    private String cosmicTooltips$rawId;

    @Unique
    private boolean cosmicTooltips$wasBritish = false;

    @Unique
    private int cosmicTooltips$prevAdvanced = 0;

    @Inject(method = "update", at = @At("TAIL"))
    private void update(CallbackInfo ci) {
        BlockState result = this.blockSelection.getBlockLookingAt();
        if (result == null) {
            cosmicTooltips$rawId = null;
            if (cosmicTooltips$tooltip != null) {
                cosmicTooltips$tooltip.remove();
                cosmicTooltips$tooltip = null;
            }
            return;
        }
        if (cosmicTooltips$rawId != null && cosmicTooltips$rawId.equals(result.getItem().getID()) && cosmicTooltips$wasBritish == TooltipUtils.british && cosmicTooltips$prevAdvanced == TooltipUtils.advanced)
            return;

        cosmicTooltips$prevAdvanced = TooltipUtils.advanced;
        cosmicTooltips$wasBritish = TooltipUtils.british;
        cosmicTooltips$rawId = result.getItem().getID();
        String id = TooltipUtils.parseIdForced(result.getItem().getID());
        String name = TooltipUtils.parseName(result.getItem().getName());
        String others = TooltipUtils.parseOther(result.getItem().getID(), null);
        if (cosmicTooltips$tooltip != null) {
            cosmicTooltips$tooltip.remove();
            cosmicTooltips$tooltip = null;
        }
        cosmicTooltips$tooltip = new InWorldUIElement(result.getItem(), name, id, others, stage);
        this.stage.addActor(cosmicTooltips$tooltip);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(CallbackInfo ci) {
        if (Gdx.input.isKeyPressed(Input.Keys.F3)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                TooltipUtils.british = !TooltipUtils.british;
                GameSetting.setSetting("britishTooltips", TooltipUtils.british);
                GameSetting.saveSettings();
                Chat.MAIN_CLIENT_CHAT.addMessage(null, TooltipUtils.british ? "[CosmicTooltips] WARNING: BRITISH MODE ENABLED" : "[CosmicTooltips] British mode disabled");
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                TooltipUtils.advanced = (TooltipUtils.advanced + 1) % 3;
                GameSetting.setSetting("advancedTooltipMode", TooltipUtils.advanced);
                GameSetting.saveSettings();
                String msg = switch (TooltipUtils.advanced) {
                    case 0 -> "Advanced tooltips disabled";
                    case 1 -> "Advanced tooltips enabled (press again to see more)";
                    case 2 -> "Ultra-advanced tooltips enabled";
                    default -> "How did you do this?";
                };
                Chat.MAIN_CLIENT_CHAT.addMessage(null, "[CosmicTooltips] " + msg);
            }
        }
    }

    @Override
    public Stage cosmicTooltips$getStage() {
        return this.stage;
    }
}
