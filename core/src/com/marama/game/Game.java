package com.marama.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class Game implements ApplicationListener {
    public World World;
    public UserInterface UserInterface;

    @Override
    public void create () {
        DirectionalLight light = new DirectionalLight();

        ColorAttribute color = new ColorAttribute(
                ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f
        );

        PerspectiveCamera camera = new PerspectiveCamera(
                67,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        AssetManager assetManager = new AssetManager();

        World = new World(color, light, camera, assetManager);

        UserInterface = new UserInterface(new ScreenViewport(), "skin/uiskin.json");

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(UserInterface);
        multiplexer.addProcessor(World.CamController);
        Gdx.input.setInputProcessor(multiplexer);
    }

	@Override
	public void resize(int width, int height) {
        UserInterface.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render () {
//        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float bgColor = 0.8f;
        Gdx.gl.glClearColor(bgColor, bgColor, bgColor, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        UserInterface.render();
        World.render();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
        World.dispose();
	}
}
