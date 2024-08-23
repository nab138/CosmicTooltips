package me.nabdev.cosmictooltips.mixins;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.BlockSelection;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.Zone;
import me.nabdev.cosmictooltips.utils.RaycastOutput;
import me.nabdev.cosmictooltips.utils.Raycaster;
import me.nabdev.cosmictooltips.utils.TooltipUIElement;
import me.nabdev.cosmictooltips.utils.TooltipUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSelection.class)
public class BlockSelectionMixin {
    @Shadow
    private float maximumRaycastDist;

    @Unique
    private String cosmicTooltips$name;

    @Unique
    private TooltipUIElement cosmicTooltips$tooltip;

    @Unique
    private String cosmicTooltips$rawName;

    @Unique
    private Vector2 cosmicTooltips$dim;

    @Inject(method = "raycast", at = @At("TAIL"))
    private void customRaycast(Zone zone, Camera worldCamera, CallbackInfo ci) {
        if (InGame.getLocalPlayer() != null) {
            RaycastOutput result = Raycaster.raycast(zone, worldCamera.position, worldCamera.direction, maximumRaycastDist);
            if (result == null) {
                cosmicTooltips$name = null;
                cosmicTooltips$rawName = null;
                if (cosmicTooltips$tooltip != null) {
                    cosmicTooltips$tooltip.hide();
                    cosmicTooltips$tooltip = null;
                }
                return;
            } else if (cosmicTooltips$dim == null || cosmicTooltips$rawName == null || !cosmicTooltips$rawName.equals(result.hitBlock().getBlockState().getItem().getID())) {
                cosmicTooltips$rawName = result.hitBlock().getBlockState().getItem().getID();
                cosmicTooltips$name = TooltipUtils.parseID(cosmicTooltips$rawName, true, null);
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

    }
}