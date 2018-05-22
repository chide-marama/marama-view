package com.marama.view.renderables.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marama.view.renderables.Renderable;

/**
 * The class containing all the elements of the WorldUserInterface, used in the GameScreen.
 */
public class WorldUserInterface extends Stage implements Renderable {

    /* Set a block size including and a padding so we
     * can work with these throughout the rest of the class. */
    private final float blockSize = 80f;
    private final float padding = 10f;

    public WorldUserInterface(Viewport viewport, Skin skin) {
        super(viewport);

        Table table = new Table(); /* Create the table that contains the UI elements */
        table.left().top(); /* Make the table stick to the upper left corner */

        FileHandle[] files = Gdx.files.internal("test_Ms").list(); /* Get the files of the test_Ms directory */
        for (final FileHandle file : files) {
            Texture texture = new Texture(file.path());
            Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
            ImageButton button = new ImageButton(drawable); /* Creating an ImageButton from the image in the directory.*/

            /* Set the size of the button and add it to the table. */
            /* This adds the button to the current row */
            button.setWidth(blockSize);
            button.setHeight(blockSize);
            table.add(button).padLeft(padding).padTop(padding).width(blockSize).height(blockSize);

            /* Add a simple button click event. This only prints the name of the image. Not connected to a user story yet. */
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println(file.name());
                }
            });
            /* Create a new row and add items to from now on. */
            table.row();
        }

        /* Determine what the height and width of the table should be with all the items of the directory.
         * Also set the table with these calculated values. */
        float height = (blockSize + padding) * files.length;
        table.setWidth(blockSize + (padding * 2));
        table.setHeight(height);
        table.setPosition(0f, viewport.getScreenHeight() - height);

        /* Set the background color of the table using a pixmap. */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(200f, 0f, 255f, 0.5f);
        pixmap.fill();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pixmap))));

        addActor(table);
    }

    @Override
    public void render(float delta) {
        act(delta);
        draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
