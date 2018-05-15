package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.marama.view.renderables.UserInterface;
import com.marama.view.renderables.World;
import com.marama.view.util.SelectObjectInputController;

public class GameScreen implements Screen {
    private World world;
    private UserInterface userInterface;

    public GameScreen() {
        ColorAttribute color = new ColorAttribute(
                ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f
        );

        PerspectiveCamera camera = new PerspectiveCamera(
                67,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        this.world = new World(color, new DirectionalLight(), camera, new AssetManager());
        this.userInterface = new UserInterface(
                new ScreenViewport(),
                new Skin(Gdx.files.internal("skin/uiskin.json"))
        );
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(userInterface);
        multiplexer.addProcessor(new SelectObjectInputController(this.world.perspectiveCamera, this.world.modelInstances));
        multiplexer.addProcessor(world.cameraInputController);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        world.render(delta);
        userInterface.render(delta);
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
