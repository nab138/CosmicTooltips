package me.nabdev.cosmictooltips.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import finalforeach.cosmicreach.ui.GameStyles;

import static finalforeach.cosmicreach.ui.GameStyles.menuButton9Patch;


public class TooltipUIElement extends Table {
    private static final int fontHeight = 16;
    Label.LabelStyle intermediateText = new Label.LabelStyle(GameStyles.styleText.font, Color.LIGHT_GRAY);
    Label.LabelStyle advancedText = new Label.LabelStyle(GameStyles.styleText.font, Color.DARK_GRAY);

    public TooltipUIElement(String name, String id, String other, Vector2 position) {
        super();
        updateText(name, id, other);
        setBackground(new NinePatchDrawable(menuButton9Patch));
        setPosition(position.x, position.y);
    }

    public TooltipUIElement(String name, String id, String other, Stage stage) {
        super();
        updateText(name, id, other);
        setBackground(new NinePatchDrawable(menuButton9Patch));
        float x = (stage.getWidth() - this.getPrefWidth()) / 2;
        float y = stage.getHeight() - this.getPrefHeight();
        setPosition(x, y);
    }


    public void updateText(String name, String id, String other) {
        Label nameLabel = new Label(name, GameStyles.styleText);
        this.add(nameLabel).left().pad(TooltipUtils.padding).padTop(fontHeight + TooltipUtils.padding).padBottom(0);

        row();

        Label idLabel = new Label(id, intermediateText);
        this.add(idLabel).left().pad(TooltipUtils.padding).padTop(fontHeight).padBottom(other == null ? TooltipUtils.padding : 0);

        if (other != null) {
            row();
            Label otherLabels = new Label(other, advancedText);
            this.add(otherLabels).left().expand().fill().pad(TooltipUtils.padding).padTop(fontHeight);
        }

        updateDims();
    }

    public void setPosition(Vector2 position) {
        if(position.x + this.getPrefWidth() > TooltipUtils.getStage().getWidth()) {
            position.x -= this.getPrefWidth() - 8;
        }

        if(position.y + this.getPrefHeight() > TooltipUtils.getStage().getHeight()) {
            position.y -= this.getPrefHeight() - 8;
        }

        this.setPosition(position.x, position.y);
    }

    private void updateDims() {
        this.setSize(this.getPrefWidth(), this.getPrefHeight());
    }
}
