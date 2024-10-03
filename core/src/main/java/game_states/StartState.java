package game_states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.math.Interpolation.*;

public class StartState extends State{
	//Batch and Camera
	SpriteBatch batch;
	OrthographicCamera camera;
	
	//Texture Atlas
	TextureAtlas atlas;
	AtlasRegion red;
	AtlasRegion pink;
	AtlasRegion yellow;
	AtlasRegion white;
	AtlasRegion play;
	AtlasRegion bitea;
	AtlasRegion biteb;
	AtlasRegion shine;
	
	//Scene 2D
	Stage stage;
	
	//fonts
	BitmapFont titlefont;
	BitmapFont normalfont;
	
	public StartState(){
		create();
	}
	
	@Override
	protected void create() {
		// TODO Auto-generated method stub
		//Batch and Camera
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		vp = new FitViewport(640,480,camera);
		
		//Texture Atlas and regions
		atlas = new TextureAtlas("game-imgs-packed//pack.atlas");
		texInit();
		
		//fonts
		fontsInit();
		
		//Scene 2D
		stageInit();
	}

	@Override
	protected void render() {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0f, 0f, 0, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		float delta = Gdx.graphics.getDeltaTime();
		
		batchRen();
		stageRen(delta);
		
	}

	@Override
	protected void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		if(gsm.next_st !=null) return;
		titlefont.dispose();
		normalfont.dispose();
		batch.dispose();
		atlas.dispose();
	}
	
	@Override
	protected void resize(int width, int height) {
		vp.update(width, height,true);
	}
	
 	private void texInit() {
		red = atlas.findRegion("red");
		pink = atlas.findRegion("pink");
		yellow = atlas.findRegion("yellow");
		white = atlas.findRegion("white");
		play = atlas.findRegion("play");
		bitea = atlas.findRegion("bitea");
		biteb = atlas.findRegion("biteb");
		shine = atlas.findRegion("shine");
	}
	
	private void stageInit() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		stage.setViewport(vp);
		
		Table table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		table.setDebug(false);
		
		LabelStyle lsty = new LabelStyle();
		lsty.font = titlefont;
		lsty.fontColor = Color.GREEN;
		
		Label title = new Label("Snake Game",lsty);
		table.add(title).padBottom(10);
	
		ImageButtonStyle ibsty = new ImageButtonStyle();
		ibsty.up = ibsty.down = new TextureRegionDrawable(play);
		
		ImageButton ib = new ImageButton(ibsty);
		Image ba = new Image(bitea);
		Image bb = new Image(biteb);
		Image sh = new Image(shine);
		sh.setOrigin(47,25);
		sh.setColor(1, 1, 1, 0);
		ba.setColor(1, 1, 1, 0);
		bb.setColor(1, 1, 1, 0);
		sh.setTouchable(Touchable.disabled);
		ba.setTouchable(Touchable.disabled);
		bb.setTouchable(Touchable.disabled);
		ib.addActor(sh);
		ib.addActor(ba);
		ib.addActor(bb);
		ib.setTransform(true);
		ib.setOrigin(25,25);
		ib.addListener(new ClickListener() {
			boolean entered = false;
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				gsm.next_st = new WrapMap((StartState)gsm.st);
				entered = false;
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				// TODO Auto-generated method stub
				if(entered) return;
				entered = true;
				ib.addAction(scaleTo(1.2f,1.2f,0.3f));
				ba.addAction(fadeIn(0.3f));
				bb.addAction(fadeIn(0.6f));
				sh.addAction(sequence(delay(0.6f),fadeIn(0),parallel(moveTo(-25,0,0.1f),scaleTo(1,1.8f,0.1f)),parallel(moveTo(-41,0,0.1f),scaleTo(1f,1f,0)),fadeOut(0.1f)));
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				// TODO Auto-generated method stub
				if(!entered) return;
				entered = false;
				for(Action a:ib.getActions()) {
					ib.removeAction(a);
				}
				ib.addAction(scaleTo(1,1,0));
				
				for(Action a:ba.getActions()) {
					ba.removeAction(a);
				}
				ba.addAction(fadeOut(0));
				
				for(Action a:bb.getActions()) {
					bb.removeAction(a);
				}
				bb.addAction(fadeOut(0f));
				
				for(Action a:sh.getActions()) {
					sh.removeAction(a);
				}
				sh.addAction(parallel(fadeOut(0),moveTo(0,0,0)));
			}
			
			
		});
		
		table.row();
		table.add(ib);
		
	}
	
	private void batchRen() {
		
	}
	
	private void stageRen(float delta) {
		stage.act(delta);
		stage.draw();
	}
	
	public void fontsInit() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts\\brettley-signature-font\\BrettleySignatureRegular-RpEMo.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 50;
		titlefont = generator.generateFont(parameter); // font size 20 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts\\typewriter-inked-font\\TypewriterInkedRegular-lgwPZ.ttf"));
		parameter.size = 20;
		normalfont = generator.generateFont(parameter);
		generator.dispose();
	}
	
}
