package me.nabdev.cosmictooltips;

import com.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import com.github.puzzle.util.PuzzleEntrypointUtil;
import finalforeach.cosmicreach.settings.GameSetting;
import me.nabdev.cosmictooltips.api.ToolTipFactory;
import me.nabdev.cosmictooltips.utils.TooltipUtils;

@SuppressWarnings("unused")
public class CosmicToolTips implements ModInitializer {
    @Override
    public void onInit() {
        PuzzleEntrypointUtil.invoke("tooltip", ToolTipFactory.class, ToolTipFactory::loadCustomTooltip);
        GameSetting.loadSettings();
        TooltipUtils.advanced = (boolean) GameSetting.getSetting("advancedTooltips", null, false);
        TooltipUtils.british = (boolean) GameSetting.getSetting("britishTooltips", null, false);
    }
}
