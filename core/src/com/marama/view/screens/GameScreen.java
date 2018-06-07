package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
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
    public final WorldUserInterface worldUserInterface;
    private InputMultiplexer inputMultiplexer;

    private SelectObjectInputController selectObjectInputController;
    private DragObjectInputController dragObjectInputController;
    private AddBlockInputController addBlockInputController;
    private DeleteObjectInputController deleteObjectInputController;

    private String activeMarama = "block";
    private int activeTool = 0;

    /**
     * Instancing the GameScreen that contains a 3D {@link World} and a {@link WorldUserInterface}.
     */
    public GameScreen() {
        inputMultiplexer = new InputMultiplexer();

        this.world = new World(this, new DirectionalLight(),
                new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), new AssetManager());
        this.worldUserInterface = new WorldUserInterface(this, this.world, new Skin(Gdx.files.internal("skin/uiskin.json")));

        selectObjectInputController = new SelectObjectInputController(this);
        dragObjectInputController = new DragObjectInputController(this);
        addBlockInputController = new AddBlockInputController(this);
        deleteObjectInputController = new DeleteObjectInputController(this);
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

    public void initializeInputControllers() {
        inputMultiplexer.clear();

        worldUserInterface.update();

        // The user interface has the highest priority.
        inputMultiplexer.addProcessor(worldUserInterface);

        // Add the tool controllers in between.
        switch (activeTool) {
            // Select tool
            case 0:
                inputMultiplexer.addProcessor(selectObjectInputController);
                break;
            // Move tool
            case 1:
                inputMultiplexer.addProcessor(dragObjectInputController);
                break;
            // Add tool
            case 2:
                inputMultiplexer.addProcessor(addBlockInputController);
                break;
            case 3:
                inputMultiplexer.addProcessor(deleteObjectInputController);
                break;
        }

        // Always add the camera controller but make it a last priority, except with the add tool.
        if (activeTool != 2)
        inputMultiplexer.addProcessor(world.getCameraInputController());

        // Apply the input multiplexer.
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
