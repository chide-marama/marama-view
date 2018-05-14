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

public class GameScreen implements Screen{
    private World world;
    private UserInterface userInterface;

    public GameScreen(){
        ColorAttribute color = new ColorAttribute(
                ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f
        );
        DirectionalLight light = new DirectionalLight();
        PerspectiveCamera camera = new PerspectiveCamera(
                67,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );
        AssetManager assetManager = new AssetManager();

        this.world = new World(color, light, camera, assetManager);

        this.userInterface =  new UserInterface(
                new ScreenViewport(),
                new Skin(Gdx.files.internal("skin/uiskin.json"))
        );
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(userInterface);
        multiplexer.addProcessor(world.camController);
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
