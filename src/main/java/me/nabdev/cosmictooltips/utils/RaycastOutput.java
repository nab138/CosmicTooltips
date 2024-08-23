// Credit: https://github.com/NikRasOff/CosmicReach-Seamless-Portals/blob/master/src/main/java/com/nikrasoff/seamlessportals/extras/RaycastOutput.java

package me.nabdev.cosmictooltips.utils;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.blocks.BlockPosition;

public record RaycastOutput(Vector3 hitPos, DirectionVector hitNormal, BlockPosition hitBlock) {
}