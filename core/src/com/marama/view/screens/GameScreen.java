package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.marama.view.renderables.World;
import com.marama.view.renderables.WorldUserInterface;
import com.marama.view.util.SelectObjectInputController;

/**
 * The {@link GameScreen} implements a {@link Screen} that contains a 3D {@link World} and a {@link WorldUserInterface}.
 */
public class GameScreen implements Screen {
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
                new ScreenViewport(),
                new Skin(Gdx.files.internal("skin/uiskin.json"))
        );
    }

    @Override
    public void show() {
        // Handle all input processors
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(worldUserInterface);
        multiplexer.addProcessor(new SelectObjectInputController(this.world));
        multiplexer.addProcessor(world.getCameraInputController());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);
        world.render(delta);
        worldUserInterface.render(delta);
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

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
