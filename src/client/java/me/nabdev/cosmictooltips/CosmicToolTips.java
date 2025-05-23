package me.nabdev.cosmictooltips;

import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;
import finalforeach.cosmicreach.settings.types.GameSetting;
import me.nabdev.cosmictooltips.api.ToolTipFactory;
import me.nabdev.cosmictooltips.utils.TooltipUtils;

@SuppressWarnings("unused")
public class CosmicToolTips implements ClientModInitializer {
    @Override
    public void onInit() {
        PuzzleEntrypointUtil.invoke("tooltip", ToolTipFactory.class, ToolTipFactory::loadCustomTooltip);
        GameSetting.loadSettings();
        TooltipUtils.advanced = ((Number) GameSetting.getSetting("advancedTooltipMode", null, 0)).intValue();
        TooltipUtils.british = (boolean) GameSetting.getSetting("britishTooltips", null, false);
    }
}
