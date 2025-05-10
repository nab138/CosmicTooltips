package me.nabdev.cosmictooltips.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import finalforeach.cosmicreach.gamestates.GameState;
import joptsimple.internal.Strings;

public class TooltipUtils {
    public static final int padding = 6;
    public static final int fontHeight = 16;

    public static boolean british = false;

    public static int advanced = 0;

    public static TooltipUIElement curTooltip = null;

    public static Vector2 getPosition() {
        float x = Gdx.input.getX();
        float y = Gdx.input.getY();

        return getStage().getViewport().unproject(new Vector2(x, y));
    }


    public static String parseName(String prettyNameRaw) {
        return british ? prettyNameRaw : prettyNameRaw.replace("inium", "inum");
    }

    public static String parseId(String id) {
        if (advanced < 1) return null;
        return parseIdForced(id);
    }

    public static String parseIdForced(String id) {
        return id.split("\\[")[0];
    }


    public static String parseOther(String id, String tag) {
        if (advanced < 2) {
            return tag;
        }
        String[] split = id.split("\\[");
        String[] data = null;
        if (split.length > 1) data = split[1].substring(0, split[1].length() - 1).split(",");
        String result = (data != null ? Strings.join(data, "\n") : "");
        if (tag != null) {
            if (!result.isEmpty()) result += "\n";
            result += tag;
        }
        if (result.isEmpty()) return null;
        return result;
    }

    public static Stage getStage() {
        return ((IStageGetter) GameState.IN_GAME).cosmicTooltips$getStage();
    }
}
