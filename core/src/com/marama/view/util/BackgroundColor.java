package com.marama.view.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class BackgroundColor implements Drawable {

    private Float x;
    private Float y;
    private Float width;
    private Float height;

    private Boolean fillParent;

    private String filename;
    private Texture texture;
    private TextureRegion textureRegion;
    private Sprite sprite;
    private Color color;

    /**
     * @param filename
     */
    public BackgroundColor(String filename) {
        this(filename, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    /**
     * @param filename
     * @param x
     * @param y
     */
    public BackgroundColor(String filename, float x, float y) {
        this(filename, x, y, 0.0f, 0.0f);
    }

    /**
     * @param filename
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public BackgroundColor(String filename, float x, float y, float width, float height) {
        this.setPosition(x, y);
        this.setSize(width, height);
        initialize(filename);
    }

    /**
     * @param filename
     */
    private void initialize(String filename) {
        setFilename(filename);
        if (x == null || y == null)
            setPosition();    // x = 0.0f; y = 0.0f;
        if (width == null || height == null || width < 0.0f || height < 0.0f)
            setSize();    // width = 0.0f; height = 0.0f;
        if (color == null)
            setColor(255, 255, 255, 255);
        if (sprite == null) {
            try {
                setSprite();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        if (fillParent == null)
            fillParent = true;
    }

    /**
     *
     */
    private void setTexture() {
        if (texture != null)
            texture.dispose();
        texture = new Texture(Gdx.files.internal(getFilename()));
    }

    /**
     *
     */
    private void setTextureRegion() {
        textureRegion = new TextureRegion(texture, (int) getWidth(), (int) getHeight());
    }

    /**
     *
     */
    private void setSprite() {
        if (texture == null)
            setTexture();
        setTextureRegion();
        sprite = new Sprite(textureRegion);
        setSpriteColor();
    }

    /**
     *
     */
    private void setSpriteColor() {
        sprite.setColor(color.r, color.g, color.b, color.a);
    }

    /**
     *
     */
    private void setPosition() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    /**
     * @param x
     * @param y
     */
    private void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     */
    private void setSize() {
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
    }

    /**
     * @param width
     * @param height
     */
    private void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public void setColor(int r, int g, int b, int a) {
        color = new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    /**
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public void setColor(float r, float g, float b, float a) {
        color = new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    /**
     * @param x
     * @param y
     */
    private void setSpritePosition(float x, float y) {
        sprite.setX(x);
        sprite.setY(y);
    }

    /**
     * @param x
     * @param y
     * @param width
     * @param height
     */
    private void updateSprite(float x, float y, float width, float height) {
        if (fillParent) {
            setSpritePosition(x, y);
            if (width != textureRegion.getRegionWidth() ||
                    height != textureRegion.getRegionHeight()) {
                setSize(width, height);
                setSprite();
            }
        }
    }

    /**
     * @param batch
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        updateSprite(x, y, width, height);
        sprite.draw(batch);
    }

    /**
     * @return
     */
    @Override
    public float getLeftWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @param leftWidth
     */
    @Override
    public void setLeftWidth(float leftWidth) {
        // TODO Auto-generated method stub

    }

    /**
     * @return
     */
    @Override
    public float getRightWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @param rightWidth
     */
    @Override
    public void setRightWidth(float rightWidth) {
        // TODO Auto-generated method stub
    }

    /**
     * @return
     */
    @Override
    public float getTopHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @param topHeight
     */
    @Override
    public void setTopHeight(float topHeight) {
        // TODO Auto-generated method stub
    }

    /**
     * @return
     */
    @Override
    public float getBottomHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @param bottomHeight
     */
    @Override
    public void setBottomHeight(float bottomHeight) {
        // TODO Auto-generated method stub
    }

    /**
     * @return
     */
    @Override
    public float getMinWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @param minWidth
     */
    @Override
    public void setMinWidth(float minWidth) {
        // TODO Auto-generated method stub

    }

    /**
     * @return
     */
    @Override
    public float getMinHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @param minHeight
     */
    @Override
    public void setMinHeight(float minHeight) {
        // TODO Auto-generated method stub

    }

    /**
     * @return
     */
    private String getFilename() {
        return filename;
    }

    /**
     * @param filename
     */
    private void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return
     */
    public float getWidth() {
        return width;
    }

    /**
     * @param width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * @return
     */
    public float getHeight() {
        return height;
    }

    /**
     * @param height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * @return
     */
    public Boolean getFillParent() {
        return fillParent;
    }

    /**
     * @param fillParent
     */
    public void setFillParent(Boolean fillParent) {
        this.fillParent = fillParent;
    }
}