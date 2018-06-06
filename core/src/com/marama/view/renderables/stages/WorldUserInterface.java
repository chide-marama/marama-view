package com.marama.view.renderables.stages;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marama.view.entities.EntityManager;
import com.marama.view.entities.Maramafication;
import com.marama.view.renderables.Renderable;
import com.marama.view.renderables.World;

/**
 * The class containing all the elements of the WorldUserInterface, used in the GameScreen.
 */
public class WorldUserInterface extends Stage implements Renderable {
    private World world;
    private EntityManager entityManager;

    /* Set a block size including and a padding so we
     * can work with these throughout the rest of the class. */
    private final float blockSize = 80f;
    private final float padding = 10f;

    public WorldUserInterface(Viewport viewport, World world) {
    public WorldUserInterface(final World world, Viewport viewport, Skin skin) {
        super(viewport);
        this.world = world;
        this.entityManager = EntityManager.getInstance();

        Table table = new Table(); // Create the table that contains the UI elements
        table.left().top(); // Make the table stick to the upper left corner

        // Get all maramafications from the EntityManager
        ObjectMap<String, Maramafication> maramaficationsObjectMap = entityManager.getMaramafications();

        // Loop through the different maramafications in the object map
        for (ObjectMap.Entries<String, Maramafication> maramaficationsIterator =
             maramaficationsObjectMap.entries(); maramaficationsIterator.hasNext(); ) {

            // Get the next maramafication
            ObjectMap.Entry maramaficationEntry = maramaficationsIterator.next();
            final Maramafication maramafication = (Maramafication) maramaficationEntry.value;

            // Get the texture from image and create a button out of it
            Texture texture = new Texture(maramafication.getImageLocation());
            Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
            ImageButton button = new ImageButton(drawable);

            /* Set the size of the button and add it to the table. */
            /* This adds the button to the current row */
            button.setWidth(blockSize);
            button.setHeight(blockSize);
            table.add(button).padLeft(padding).padTop(padding).width(blockSize).height(blockSize);

            /* Add a simple button click event. This only prints the name of the image. Not connected to a user story yet. */
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println(maramafication.getName());
                }
            });
            /* Create a new row and add items to from now on. */
            table.row();
        }

        /* Determine what the height and width of the table should be with all the items of the directory.
         * Also set the table with these calculated values. */
        float height = (blockSize + padding) * maramaficationsObjectMap.size;
        table.setWidth(blockSize + (padding * 2));
        table.setHeight(height);
        table.setPosition(0f, viewport.getScreenHeight() - height);

        /* Set the background color of the table using a pixmap. */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(5f, 5f, 5f, 1f);
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
