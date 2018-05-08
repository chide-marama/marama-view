package com.marama.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;


public class Game implements ApplicationListener {
    public World World;
    public UserInterface UserInterface;

    @Override
	public void create () {
	    World = new World();
	    UserInterface = new UserInterface();

	    World.create();
	    UserInterface.create();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(UserInterface.Stage);
        multiplexer.addProcessor(World.CamController);
        Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {
        World.render();
        UserInterface.render();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {

	}
}
