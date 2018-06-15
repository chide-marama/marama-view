package com.marama.view.renderables.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.marama.view.entities.EntityManager;
import com.marama.view.entities.Maramafication;
import com.marama.view.renderables.Renderable;
import com.marama.view.screens.GameScreen;

/**
 * The class containing all the elements of the WorldUserInterface, used in the GameScreen.
 */
public class WorldUserInterface extends Stage implements Renderable {
    private final GameScreen gameScreen;
    private final Skin skin;

    public WorldUserInterface(final GameScreen gameScreen, Skin skin) {
        this.skin = skin;
        this.gameScreen = gameScreen;

        addActor(this.maramaList());
        addActor(this.tools());
        this.update();
    }

    public void update() {
        this.getActors().get(1).remove();
        this.getActors().get(0).remove();

        addActor(this.maramaList());
        addActor(this.tools());
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

    private Table maramaList() {
        float blockSize = 100f;
        float padding = 10f;

        Table table = new Table(); // Create the table that contains the UI elements
        table.left().top(); // Make the table stick to the upper left corner

        // Get all maramafications from the EntityManager
        ObjectMap<String, Maramafication> maramaficationsObjectMap = EntityManager.getInstance().getMaramafications();

        // Loop through the different maramafications in the object map
        /* Set a block size including and a padding so we
         * can work with these throughout the rest of the class. */
        for (ObjectMap.Entries<String, Maramafication> maramaficationsIterator =
             maramaficationsObjectMap.entries(); maramaficationsIterator.hasNext(); ) {

            // Get the next maramafication
            ObjectMap.Entry maramaficationEntry = maramaficationsIterator.next();
            final Maramafication maramafication = (Maramafication) maramaficationEntry.value;

            // Get the texture from image and create a button out of it
            Texture texture = new Texture(maramafication.getImageLocation());
            Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

            // Give the non selected maramafications in the list a tint
            if (!maramafication.getName().equals(gameScreen.getActiveMarama())) {
                drawable = new TextureRegionDrawable(new TextureRegion(texture)).tint(new Color(0, 0, 0, 0.2f));
            }

            ImageButton button = new ImageButton(drawable);

            // Set the size of the button and add it to the table.
            // This adds the button to the current row.
            button.setWidth(blockSize);
            button.setHeight(blockSize);

            table.add(button).padLeft(padding).padTop(padding).width(blockSize).height(blockSize);

            // Add a simple button click event. This only prints the name of the image. Not connected to a user story yet.
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameScreen.setActiveMarama(maramafication.getName());
                }
            });

            // Create a new row and add items to from now on.
            table.row();
        }

        // Determine what the height and width of the table should be with all the items of the directory.
        // Also set the table with these calculated values.
        float height = (blockSize + (padding * 2f)) * maramaficationsObjectMap.size;
        table.setWidth(blockSize + (padding * 2));
        table.setHeight(height);
        table.setPosition(0f, getViewport().getScreenHeight() - height);

        return table;
    }

    private Table tools() {
        Array<TextButton> tools = new Array<TextButton>();
        tools.add(new TextButton("Select tool", skin, "default"));
        tools.add(new TextButton("Move tool", skin, "default"));
        tools.add(new TextButton("Add tool", skin, "default"));
        tools.add(new TextButton("Remove tool", skin, "default"));

        int padding = 20;
        int toolHeight = (int) tools.get(0).getHeight() + padding;
        int panelHeight = tools.size * toolHeight + (padding * 2);
        int panelWidth = 140;

        Table toolsPanel = new Table();
        toolsPanel.setWidth(panelWidth);
        toolsPanel.setHeight(panelHeight);
        toolsPanel.setPosition(getViewport().getScreenWidth() - panelWidth, getViewport().getScreenHeight() - panelHeight);

        for (int i = 0; i < tools.size; i++) {
            TextButton tool = tools.get(i);

            if (gameScreen.getActiveTool() == i) {
                TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(tool.getStyle());
                style.fontColor = Color.LIGHT_GRAY;
                tool.setStyle(style);
            }

            final int index = i;
            tool.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameScreen.setTool(index);
                }
            });

            Cell cell = toolsPanel.add(tool);
            cell.width(panelWidth - (padding * 2));
            cell.pad(padding);
            cell.padTop(0);
            if (i == tools.size - 1) { cell.padBottom(0); }
            toolsPanel.row();
        }

        return toolsPanel;
    }
}
