package me.nabdev.cosmictooltips;

import com.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import com.github.puzzle.util.PuzzleEntrypointUtil;
import me.nabdev.cosmictooltips.api.ToolTipFactory;

public class CosmicToolTips implements ModInitializer {

    @Override
    public void onInit() {
        PuzzleEntrypointUtil.invoke("tooltip", ToolTipFactory.class, ToolTipFactory::loadCustomTooltip);
    }
}
