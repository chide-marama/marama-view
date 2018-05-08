package com.marama.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class Game implements ApplicationListener {
    final String SPACE_SPHERE_PATH = "models/spacesphere/spacesphere.obj" ;
    final String BLOCK_PATH = "models/block/block.obj" ;
    final String INVADER_PATH = "models/invader/invader.obj" ;
    final String SHIP_PATH = "models/ship/ship.obj" ;

	public PerspectiveCamera cam;
    public CameraInputController camController;
	public ModelBatch modelBatch;
	public AssetManager assets;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public Environment environment;
	public boolean loading;

	public Array<ModelInstance> blocks = new Array<ModelInstance>();
	public Array<ModelInstance> invaders = new Array<ModelInstance>();

	public ModelInstance ship;
	public ModelInstance space;

	public Skin skin;
	public Stage stage;


	@Override
	public void create () {
        modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        final TextButton button =  new TextButton("Click me", skin, "default");
        button.setWidth(200f);
        button.setHeight(50f);

        final Dialog dialog = new Dialog("Nee, dat is niet hoe het werkt, vriend!", skin);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
            	System.out.println("AAA");
                dialog.show(stage);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run(){
                        dialog.hide();
                    }
                }, 3);
            }
        });
        stage.addActor(button);
        Gdx.input.setInputProcessor(stage);

//
//		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		cam.position.set(7f, 7f, 7f);
//		cam.lookAt(0,0,0);
//		cam.near = 1f;
//		cam.far = 300f;
//		cam.update();
//
//		camController = new CameraInputController(cam);
//		Gdx.input.setInputProcessor(camController);
//
//		assets = new AssetManager();
//		assets.load(SHIP_PATH, Model.class);
//		assets.load(BLOCK_PATH, Model.class);
//		assets.load(INVADER_PATH, Model.class);
//		assets.load(SPACE_SPHERE_PATH, Model.class);
//		loading = true;
	}

	private void doneLoading(){
        ship = new ModelInstance(assets.get(SHIP_PATH, Model.class));
        ship.transform.setToRotation(Vector3.Y, 180).trn(0, 0, 6f);
        instances.add(ship);

        Model blockModel = assets.get(BLOCK_PATH, Model.class);
        for(float x = -5f; x <= 5f; x+=2f){
            ModelInstance block = new ModelInstance(blockModel);
            block.transform.setToTranslation(x, 0, 3f);
            instances.add(block);
            blocks.add(block);
        }

        Model invaderModel = assets.get(INVADER_PATH, Model.class);
        for(float x = -5f; x <= 5f; x+=2f){
            for(float z = -8f; z <= 0f; z+=2f) {
                ModelInstance invader = new ModelInstance(invaderModel);
                invader.transform.setToTranslation(x, 0, z);
                instances.add(invader);
                blocks.add(invader);
            }
        }

        space = new ModelInstance(assets.get(SPACE_SPHERE_PATH, Model.class));

        loading = false;
    }

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {
//		if(loading && assets.update())
//		    doneLoading();
//		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

//		modelBatch.begin(cam);
//		modelBatch.render(instances, environment);
//		if(space != null)
//		    modelBatch.render(space);
//		modelBatch.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
//		modelBatch.dispose();
//		instances.clear();
//		assets.dispose();
	}
}
