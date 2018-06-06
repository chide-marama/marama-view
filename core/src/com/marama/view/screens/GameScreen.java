package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.marama.view.controllers.addBlockInputController;
import com.marama.view.renderables.World;
import com.marama.view.renderables.stages.WorldUserInterface;
import com.marama.view.controllers.DragObjectInputController;
import com.marama.view.controllers.SelectObjectInputController;

/**
 * The {@link GameScreen} extends a {@link ScreenAdapter} that contains a 3D {@link World} and a {@link WorldUserInterface}.
 */
public class GameScreen extends ScreenAdapter {
    private World world;
    private WorldUserInterface worldUserInterface;
    private InputMultiplexer inputMultiplexer;

    private SelectObjectInputController selectObjectInputController;
    private DragObjectInputController dragObjectInputController;
    private addBlockInputController addBlockInputController;

    /**
     * Instancing the GameScreen that contains a 3D {@link World} and a {@link WorldUserInterface}.
     */
    public GameScreen() {
        inputMultiplexer = new InputMultiplexer();

        this.world = new World(new DirectionalLight(),
                new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), new AssetManager());
        this.worldUserInterface = new WorldUserInterface(this, this.world, new Skin(Gdx.files.internal("skin/uiskin.json")));

        selectObjectInputController = new SelectObjectInputController(this.world);
        dragObjectInputController = new DragObjectInputController(this.world);
        addBlockInputController = new addBlockInputController(this.world);
    }

    @Override
    public void show() {
        this.updateTool(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);
        world.render(delta);
        worldUserInterface.render(delta);
    }

    public void updateTool(int index) {
        inputMultiplexer.clear();
        worldUserInterface.setActiveTool(index);

        // The user interface has the highest priority.
        inputMultiplexer.addProcessor(worldUserInterface);

        // Add the tool controllers in between.
        switch (index) {
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
        }

        // Always add the camera controller but make it a last priority.
        inputMultiplexer.addProcessor(world.getCameraInputController());

        // Apply the input multiplexer.
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
