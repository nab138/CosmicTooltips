package me.nabdev.cosmictooltips.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.core.loader.util.Reflection;
import finalforeach.cosmicreach.ui.FontRenderer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// TODO: Switch to CRButton/other stage widgets
@SuppressWarnings("removal")
public class TooltipUIElement extends finalforeach.cosmicreach.ui.UIElement {
    public TooltipUIElement(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    @Override
    public void drawBackground(Viewport uiViewport, SpriteBatch batch, float mouseX, float mouseY) {
        if (this.shown) {
            this.buttonTex = uiPanelTex;

            batch.setColor(0.0f, 0.0f, 0.0f, 0.6f);
            this.drawElementBackground(uiViewport, batch);
        }
    }

    public void drawText(Viewport uiViewport, SpriteBatch batch, float opacity, Color[] colors) {
        if (this.shown && this.text != null && !this.text.isEmpty()) {
            float x = this.getDisplayX(uiViewport);
            float y = this.getDisplayY(uiViewport);
            String[] lines = this.text.split("\n");

            FontRenderer.getTextDimensions(uiViewport, this.text, this.tmpVec);
            if (this.tmpVec.x > this.w) {
                FontRenderer.drawTextbox(batch, uiViewport, this.text, x, y, this.w);
            } else {
                float maxX = x;
                float maxY = y;

                for (int i = 0; i < this.text.length(); ++i) {
                    char c = this.text.charAt(i);

                    Method getTextRegForChar;
                    Method getCharStartPos;
                    Method getCharSize;
                    try {
                        Class<?> fontTextureClass = Class.forName("finalforeach.cosmicreach.FontTexture");
                        getTextRegForChar = Reflection.getMethod(fontTextureClass, "getTexRegForChar", char.class);
                        getCharStartPos = Reflection.getMethod(fontTextureClass, "getCharStartPos", char.class);
                        getCharSize = Reflection.getMethod(fontTextureClass, "getCharSize", char.class);

                        var f = FontRenderer.getFontTexOfChar(c);
                        if (f == null) {
                            c = '?';
                            f = FontRenderer.getFontTexOfChar(c);
                        }

                        TextureRegion texReg = (TextureRegion) getTextRegForChar.invoke(f, c);
                        x -= ((Vector2) getCharStartPos.invoke(f, c)).x % (float) texReg.getRegionWidth();
                        switch (c) {
                            case '\n':
                                y += (float) texReg.getRegionHeight();
                                x = this.getDisplayX(uiViewport);
                                maxX = Math.max(maxX, x);
                                maxY = Math.max(maxY, y);
                                break;
                            case ' ':
                                x += ((Vector2) getCharSize.invoke(f, c)).x / 4.0F;
                                maxX = Math.max(maxX, x);
                                break;
                            default:
                                x += ((Vector2) getCharSize.invoke(f, c)).x + ((Vector2) getCharStartPos.invoke(f, c)).x % (float) texReg.getRegionWidth() + 2.0F;
                                maxX = Math.max(maxX, x);
                                maxY = Math.max(maxY, y + (float) texReg.getRegionHeight());
                        }
                    } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                        System.out.println("Failed to reflect FontTexture methods");
                        e.printStackTrace();
                        return;
                    }
                }
                x = this.getDisplayX(uiViewport);
                y = this.getDisplayY(uiViewport);
                x += this.w / 2.0F - (maxX - x) / 2.0F;
                y += this.h / 2.0F - (maxY - y) / 2.0F;

                for (int j = 0; j < lines.length; ++j) {
                    String line = lines[j];
                    float displayY = y + ((this.tmpVec.y / lines.length) * (j));
                    Color textColor;
                    if (j < colors.length) {
                        textColor = colors[j];
                    } else if (colors.length > 0) {
                        textColor = colors[colors.length - 1];
                    } else {
                        textColor = new Color(this.textColor);
                    }
                    Color text = new Color(textColor.r, textColor.g, textColor.b, opacity);
                    batch.setColor(text);
                    FontRenderer.drawText(batch, uiViewport, line, x, displayY);
                    batch.setColor(new Color(Color.WHITE));
                }
            }
        }
    }
}
