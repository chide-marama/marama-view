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

    /**
     * Instancing the GameScreen that contains a 3D {@link World} and a {@link WorldUserInterface}.
     */
    public GameScreen() {
        inputMultiplexer = new InputMultiplexer();

        this.world = new World(new DirectionalLight(),
                new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), new AssetManager());
        this.worldUserInterface = new WorldUserInterface(this, this.world, new Skin(Gdx.files.internal("skin/uiskin.json")));
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
        inputMultiplexer.addProcessor(worldUserInterface);

        worldUserInterface.setActiveTool(index);

        switch (index) {
            // Select tool
            case 0:
                inputMultiplexer.addProcessor(new SelectObjectInputController(this.world));
                inputMultiplexer.addProcessor(world.getCameraInputController());
                break;
            // Move tool
            case 1:
                inputMultiplexer.addProcessor(new DragObjectInputController(this.world));
                break;
            // Add tool
            case 2:
                inputMultiplexer.addProcessor(new addBlockInputController(this.world));
                break;
        }

        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
