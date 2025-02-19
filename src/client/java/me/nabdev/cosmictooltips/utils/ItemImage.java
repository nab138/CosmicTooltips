package me.nabdev.cosmictooltips.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;

public class ItemImage extends Image {
    private final Item item;

    public ItemImage(Item item) {
        this.item = item;
        Gdx.app.postRunnable(this::renderItemToTexture);
    }

    private void renderItemToTexture() {
        Camera itemCam = ItemRenderer.getItemSlotCamera(item);
        Viewport itemViewport = new FitViewport(100.0F, 100.0F, itemCam);
        itemViewport.apply();
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 100, 100, false);
        fbo.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ItemRenderer.drawItem(itemCam, item);
        fbo.end();
        Texture itemTexture = fbo.getColorBufferTexture();
        itemTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region = new TextureRegion(itemTexture);
        region.flip(false, true);
        setDrawable(new TextureRegionDrawable(region));
    }

}