package me.nabdev.cosmictooltips.utils;

import com.badlogic.gdx.math.Vector3;

public enum DirectionVector {
    POS_Z(new Vector3(0, 0, 1), "posZ"),
    NEG_Z(new Vector3(0, 0, -1), "negZ"),
    POS_X(new Vector3(1, 0, 0), "posX"),
    NEG_X(new Vector3(-1, 0, 0), "negX"),
    POS_Y(new Vector3(0, 1, 0), "posY"),
    NEG_Y(new Vector3(0, -1, 0), "negY");

    public static final DirectionVector[] allDirections = new DirectionVector[]{POS_Z, NEG_Z, NEG_X, POS_X, POS_Y, NEG_Y};

    private final Vector3 vector;

    @SuppressWarnings("unused")
    DirectionVector(Vector3 from, String name){
        this.vector = from;
    }
    public Vector3 getVector(){
        return this.vector;
    }

    public static DirectionVector getClosestDirection(Vector3 from){
        return getClosestDirectionVector(from, allDirections);
    }

    public static DirectionVector getClosestDirectionVector(Vector3 from, DirectionVector[] checkDirections){
        from.nor();
        DirectionVector result = POS_Z;
        float biggestDot = -100;
        for (DirectionVector v : checkDirections){
            float thisDot = v.getVector().dot(from);
            if (thisDot > biggestDot){
                result = v;
                biggestDot = thisDot;
            }
        }
        return result;
    }
}