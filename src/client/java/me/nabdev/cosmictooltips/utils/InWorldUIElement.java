package me.nabdev.cosmictooltips.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.ui.GameStyles;

import static finalforeach.cosmicreach.ui.GameStyles.menuButton9Patch;


public class InWorldUIElement extends Table
{
    private static final int fontHeight = 16;
    Label.LabelStyle intermediateText = new Label.LabelStyle(GameStyles.styleText.font, Color.LIGHT_GRAY);
    Label.LabelStyle advancedText = new Label.LabelStyle(GameStyles.styleText.font, Color.DARK_GRAY);

    public InWorldUIElement(Item item, String name, String id, String other, Stage stage) {
        super();
        updateText(item, name, id, other);
        setBackground(new NinePatchDrawable(menuButton9Patch));
        float x = (stage.getWidth() - this.getPrefWidth()) / 2;
        float y = stage.getHeight() - this.getPrefHeight();
        setPosition(x, y);
    }


    private void updateText(Item item, String name, String id, String other) {
        ItemImage itemImage = new ItemImage(item);
        this.add(itemImage).width(fontHeight * 2 + TooltipUtils.padding).height(fontHeight * 2+ TooltipUtils.padding).left().top().padTop(TooltipUtils.padding * 0.5f);


        Table nameIdTable = new Table();
        Label nameLabel = new Label(name, GameStyles.styleText);
        nameIdTable.add(nameLabel).expand().fill().padTop(fontHeight + TooltipUtils.padding).padBottom(0);
        nameIdTable.row();
        Label idLabel = new Label(id, intermediateText);

        nameIdTable.add(idLabel).left().padTop(fontHeight);
        this.add(nameIdTable).expand().fill().left().padLeft(TooltipUtils.padding);

        row();

        if (other != null) {
            Label otherLabels = new Label(other, advancedText);
            this.add(otherLabels).colspan(2).left().expand().fill().pad(TooltipUtils.padding).padTop(fontHeight);
        }

        this.setSize(this.getPrefWidth(), this.getPrefHeight());
    }
}
