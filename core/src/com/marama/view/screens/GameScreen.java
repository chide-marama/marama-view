package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.marama.view.controllers.AddBlockInputController;
import com.marama.view.controllers.DeleteObjectInputController;
import com.marama.view.controllers.DragObjectInputController;
import com.marama.view.controllers.SelectObjectInputController;
import com.marama.view.renderables.World;
import com.marama.view.renderables.stages.WorldUserInterface;

/**
 * The {@link GameScreen} extends a {@link ScreenAdapter} that contains a 3D {@link World} and a {@link WorldUserInterface}.
 */
public class GameScreen extends ScreenAdapter {
    public final World world;
    private final WorldUserInterface worldUserInterface;
    private final InputMultiplexer inputMultiplexer;

    private String activeMarama = "block";
    private int activeTool = 0;

    /**
     * Instancing the GameScreen that contains a 3D {@link World} and a {@link WorldUserInterface}.
     */
    public GameScreen() {
        inputMultiplexer = new InputMultiplexer();

        this.world = new World(this, new DirectionalLight(),
                new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.worldUserInterface = new WorldUserInterface(this, new Skin(Gdx.files.internal("skin/uiskin.json")));
    }

    public String getActiveMarama() {
        return activeMarama;
    }

    public void setActiveMarama(String activeMarama) {
        this.activeMarama = activeMarama;
        worldUserInterface.update();
    }

    public int getActiveTool() {
        return activeTool;
    }

    public void setTool(int index) {
        this.activeTool = index;
        worldUserInterface.update();
        this.initializeInputControllers();
    }

    @Override
    public void show() {
        this.setActiveMarama(this.activeMarama);
        this.setTool(activeTool);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);
        world.render(delta);
        worldUserInterface.render(delta);
    }

    private void initializeInputControllers() {
        inputMultiplexer.clear();

        // The user interface has the highest priority.
        inputMultiplexer.addProcessor(worldUserInterface);

        // Add the tool controllers in between.
        switch (activeTool) {
            // Select tool
            case 0:
                inputMultiplexer.addProcessor(new SelectObjectInputController(this));
                break;
            // Move tool
            case 1:
                inputMultiplexer.addProcessor(new DragObjectInputController(this));
                break;
            // Add tool
            case 2:
                inputMultiplexer.addProcessor(new AddBlockInputController(this));
                break;
            case 3:
                inputMultiplexer.addProcessor(new DeleteObjectInputController(this));
                break;
        }

        // Always add the camera controller but make it a last priority
        inputMultiplexer.addProcessor(world.getCameraInputController());

        // Apply the input multiplexer.
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
