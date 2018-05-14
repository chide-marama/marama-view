package com.marama.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.marama.view.screens.MainMenuScreen;


public class View extends Game implements ApplicationListener {

    @Override
    public void create () {
        setScreen(new MainMenuScreen(
            this,
            new ScreenViewport(),
            new Skin(Gdx.files.internal("skin/uiskin.json"))
        ));
    }

	@Override
	public void resize(int width, int height) {
//        getScreen().getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.resize(width, height);
	}

	@Override
	public void render () {
        float bgColor = 0.8f;
        Gdx.gl.glClearColor(bgColor, bgColor, bgColor, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        super.render();
	}

	@Override
	public void pause() {
        super.pause();
	}

	@Override
	public void resume() {
        super.resume();
	}

	@Override
	public void dispose () {
        super.dispose();
	}
}
