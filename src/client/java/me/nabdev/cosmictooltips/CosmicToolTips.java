package me.nabdev.cosmictooltips;

import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;
import finalforeach.cosmicreach.settings.GameSetting;
import me.nabdev.cosmictooltips.api.ToolTipFactory;
import me.nabdev.cosmictooltips.utils.TooltipUtils;

@SuppressWarnings("unused")
public class CosmicToolTips implements ClientModInitializer {
    @Override
    public void onInit() {
        PuzzleEntrypointUtil.invoke("tooltip", ToolTipFactory.class, ToolTipFactory::loadCustomTooltip);
        GameSetting.loadSettings();
        TooltipUtils.advanced = (boolean) GameSetting.getSetting("advancedTooltips", null, false);
        TooltipUtils.british = (boolean) GameSetting.getSetting("britishTooltips", null, false);
    }
}
