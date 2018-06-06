package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.marama.view.controllers.addBlockInputController;
import com.marama.view.renderables.World;
import com.marama.view.renderables.stages.WorldUserInterface;
import com.marama.view.util.DragObjectInputController;
import com.marama.view.util.SelectObjectInputController;

/**
 * The {@link GameScreen} extends a {@link ScreenAdapter} that contains a 3D {@link World} and a {@link WorldUserInterface}.
 */
public class GameScreen extends ScreenAdapter {
    private World world;
    private WorldUserInterface worldUserInterface;

    /**
     * Instancing the GameScreen that contains a 3D {@link World} and a {@link WorldUserInterface}.
     */
    public GameScreen() {
        this.world = new World(
                new DirectionalLight(),
                new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()),
                new AssetManager()
        );

        this.worldUserInterface = new WorldUserInterface(
                this.world,
                new ScreenViewport()
        );
    }

    @Override
    public void show() {
        // Handle all input processors
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(worldUserInterface);
//        multiplexer.addProcessor(new DragObjectInputController(this.world));
        multiplexer.addProcessor(new addBlockInputController(this.world));
        multiplexer.addProcessor(world.getCameraInputController());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);
        world.render(delta);
        worldUserInterface.render(delta);
    }
}
