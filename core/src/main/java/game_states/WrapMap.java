package game_states;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.snake.scell;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.math.Interpolation.*;

public class WrapMap extends State{
	//Batch and Camera
		SpriteBatch batch;
		OrthographicCamera camera;
		
		//Atlas and regions
		TextureAtlas atlas;
		AtlasRegion head;
		AtlasRegion body;
		AtlasRegion back;
		AtlasRegion apple;
		AtlasRegion yellow;
		
		//Sprites
		
		//Viewports
		FitViewport leftvp;
		FitViewport midvp;
		FitViewport rightvp;
		
		//Snake array and other
		scell headc;
		scell tailc;
		scell applec;
		
		//Scene2d
		Stage stageleft;
		Label scorelab;
		
		Stage stagemid;
		Label pauselab;
		Label yrscore;
		TextButton restart;
		
		//Fonts
		BitmapFont titlefont;
		BitmapFont normalfont;
		
		//Random
		Random rand;
		
		//primitives
		final float ratio = 560/480f;
		boolean started = false;
		boolean ended = false;
		boolean pressed = false;
		boolean paused = false;
		int dir=0;
		float speed = 10;
		float steptime = 1/speed;
		float timer = 0;
		int score = 0;
		
	public WrapMap() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		atlas = new TextureAtlas("game-imgs-packed//pack.atlas");
		
		vp = new FitViewport(640,480,camera);
		
		fontsInit();
		
		create();
	}
	
	public WrapMap(StartState st) {
		this.gsm = st.gsm;
		this.camera = st.camera;
		this.vp = st.vp;
		this.batch = st.batch;
		this.atlas = st.atlas;
		this.titlefont = st.titlefont;
		this.normalfont = st.normalfont;
		create();
	}
	
	@Override
	protected void create() {
		// TODO Auto-generated method stub
		
		//Viewports
		vp.setCamera(new OrthographicCamera());
		
		leftvp = new FitViewport(80,480);
		leftvp.setScreenBounds(0, 0,80,480);
		
		midvp = new FitViewport(480,480,camera);
		midvp.setScreenBounds(80, 0, 480, 480);
		
		rightvp = new FitViewport(80,480);
		rightvp.setScreenBounds(560, 0, 80, 480);
		
		//Atlas and regions
		texInit();
		
		//Scene2d
		stageInit();
		
		//Random
		rand = new Random();
				
		//Snake array
		createSn(6);
		applec = new scell(rand.nextInt(40),rand.nextInt(40),apple);
	}

	@Override
	protected void render() {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0.3f, 0.3f, 0.3f, 1);
		camera.update();
		
		float delta = Gdx.graphics.getDeltaTime();
		
		batch.setProjectionMatrix(camera.combined);
		batchRen();
		
		stageRen(delta);
		
		inpUpd();
		if(started && !paused && !ended) {
			if(timer>steptime) {
				posUpd();
				pressed = false;
				timer=0;
			}
			else {
				inbet();
			}
			timer+= delta;
		}
	}

	@Override
	protected void dispose() {
		// TODO Auto-generated method stub
		stageleft.dispose();
		stagemid.dispose();
		if(gsm.next_st!=null) return;
		normalfont.dispose();
		titlefont.dispose();
		batch.dispose();
		atlas.dispose();
	}
	
	protected void resize(int width, int height) {
		vp.update(width, height);
		int w = vp.getScreenWidth(),h = vp.getScreenHeight();
		int x = vp.getScreenX();
		int y = vp.getScreenY();
		
		leftvp.update(w, h);
		midvp.update(w, h);
		rightvp.update(w, h);
		
		leftvp.setScreenPosition(x,y);
		midvp.setScreenPosition(x+leftvp.getScreenWidth(),y);
		rightvp.setScreenPosition(midvp.getScreenX()+midvp.getScreenWidth(),y);
	}
	
	private void inpUpd() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			paused = !paused;
			pauselab.setVisible(paused);
			return;
		}
		if(paused) return;
		if(Gdx.input.isKeyPressed(Input.Keys.A) && !pressed) {
			pressed = true;
			started = true;
			if(dir%2!=0) dir=2;
			return;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D) && !pressed ) {
			pressed = true;
			started = true;
			if(dir%2!=0) dir=0;
			return;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W) && !pressed) {
			pressed = true;
			started = true;
			if(dir%2==0) dir=1;
			return;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S) && !pressed) {
			pressed = true;
			started = true;
			if(dir%2==0) dir=3;
			return;
		}
	}
	
	private void posUpd() {
		if(headc.x!=applec.x || headc.y!=applec.y) { //apple not in head
			scell cur = tailc;
			tailc = cur.prev;
			tailc.next = null;
			
			cur.x = headc.x;
			cur.y = headc.y;
			cur.sprpos();
			
			cur.prev = headc;
			cur.next = headc.next;
			cur.next.prev = cur;
			headc.next = cur;
		}
		else { //apple in head
			scell newc = new scell(headc.x,headc.y,body);
			newc.prev = headc;
			newc.next = headc.next;
			newc.next.prev = newc;
			headc.next = newc;
			
			applec.x = rand.nextInt(40);
			applec.y = rand.nextInt(40);
			applec.sprpos();
			
			scrUpd();
		}
		switch(dir) {
		case 0:
			headc.x++;
			if(headc.x==40) headc.x=0;
			break;
		case 1:
			headc.y++;
			if(headc.y==40) headc.y=0;
			break;
		case 2:
			headc.x--;
			if(headc.x<0) headc.x=39;
			break;
		case 3:
			headc.y--;
			if(headc.y<0) headc.y=39;
			break;
		default:
		}
		headc.sprpos();
		if(chcoll()) {
			ended = true;
			yrscore.setVisible(true);
			yrscore.setText("Your score is "+score);
			restart.setVisible(true);
		}
	}
	
	private void inbet() {
		float ratio = timer/steptime;
		
		if(dir%2==0) {
			float x = headc.x + (dir==0?1:-1)*ratio;
			headc.sp.setCenterX(scell.ox+12*x);
		}
		else {
			float y = headc.y + (dir==1?1:-1)*ratio;
			headc.sp.setCenterY(scell.oy+12*y);
		}
		
		scell cur = headc.next;
		while(cur!=null) {
			float x = cur.x + (cur.prev.x-cur.x)*ratio;
			float y = cur.y + (cur.prev.y-cur.y)*ratio;
			if(cur.x!=cur.prev.x) {
				if(Math.abs(cur.prev.x-cur.x)>1) {
					if(ratio<0.5f) {
						x = cur.x + (cur.x==0?-1:1)*(ratio);
					}
					else {
						x = (cur.x==0?-0.5f:39.5f) + (cur.x==0?1:-1)*(ratio-0.5f);
					}
				}
				cur.sp.setCenterX(scell.ox+12*x);
			}
			else {
				if(Math.abs(cur.prev.y-cur.y)>1) {
					if(ratio<0.5f) {
						y = cur.y + (cur.y==0?-1:1)*(ratio);
					}
					else {
						y = (cur.y==0?-0.5f:39.5f) + (cur.y==0?1:-1)*(ratio-0.5f);
					}
				}
				cur.sp.setCenterY(scell.oy+12*y);
			}
			
			cur = cur.next;
		}
	}
	
	private boolean chcoll() {
		scell cur = headc.next;
		while(cur!=null) {
			if(cur.x==headc.x && cur.y==headc.y) {
				return true;
			}
			cur = cur.next;
		}
		return false;
	}
	
	private void batchRen() {
		midvp.apply(true);
		batch.begin();
		batch.draw(back,0,0);
		applec.sp.draw(batch);
		scell cur = tailc;
		while(cur!=null) {
			cur.sp.draw(batch);
			cur = cur.prev;
		}
		batch.end();
	}
	
	private void stageRen(float delta) {
		leftvp.apply(true);
		stageleft.act(delta);
		stageleft.draw();
		
		midvp.apply();
		stagemid.act(delta);
		stagemid.draw();
	}
	
	private void createSn(int len) {
		headc = new scell(len-1,0,head);
		int count=1;
		scell cur = headc;
		while(count<len) {
			cur.next = new scell(len-count-1,0,body);
			cur.next.prev = cur;
			count++;
			cur = cur.next;
		}
		tailc = cur;
	}
	
	private void texInit() {
		head = atlas.findRegion("head");
		body = atlas.findRegion("body");
		back = atlas.findRegion("back");
		apple = atlas.findRegion("apple");
		yellow = atlas.findRegion("yellow");
	}
	
	private void stageInit() {
		stageleft = new Stage();
		stageleft.setViewport(leftvp);
		
		Table tableleft = new Table();
		tableleft.setFillParent(true);
		tableleft.setDebug(false);
		stageleft.addActor(tableleft);
		
		LabelStyle lsty = new LabelStyle();
		lsty.font = normalfont;
		lsty.fontColor = Color.WHITE;
		
		Label label1 = new Label("Score:",lsty);
		tableleft.add(label1);
		
		scorelab = new Label("0",lsty);
		tableleft.row();
		tableleft.add(scorelab);
		
		stagemid = new Stage();
		stagemid.setViewport(midvp);
		Gdx.input.setInputProcessor(stagemid);
		
		Table tablemid = new Table();
		tablemid.setFillParent(true);
		tablemid.setDebug(false);
		stagemid.addActor(tablemid);
		
		pauselab = new Label("Paused",lsty);
		pauselab.setVisible(false);
		tablemid.add(pauselab);
		
		yrscore = new Label("",lsty);
		yrscore.setVisible(false);
		tablemid.row();
		tablemid.add(yrscore);
		
		TextButtonStyle tbsty = new TextButtonStyle();
		tbsty.font = normalfont;
		tbsty.fontColor = Color.BLACK;
		tbsty.up =tbsty.over=tbsty.down= new TextureRegionDrawable(yellow);
		
		restart = new TextButton("Restart",tbsty);
		restart.setVisible(false);
		restart.setOrigin(25,25);
		restart.setTransform(true);
		restart.addListener(new ClickListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				// TODO Auto-generated method stub
				yrscore.addAction(moveBy(0,5));
				restart.addAction(scaleTo(1.2f,1.2f,0.3f));
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				// TODO Auto-generated method stub
				yrscore.addAction(moveBy(0,-5));
				restart.addAction(scaleTo(1,1f,0.3f));
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				yrscore.setVisible(false);
				restart.setVisible(false);
				pauselab.setVisible(false);
				scorelab.setText("0");
				
				started = false;
				ended = false;
				pressed = false;
				paused = false;
				dir=0;
				speed = 10;
				steptime = 1/speed;
				timer = 0;
				score = 0;
				
				applec.x = rand.nextInt(40);
				applec.y = rand.nextInt(40);
				applec.sprpos();
				
				createSn(6);
			}
			
		});
		tablemid.row();
		tablemid.add(restart).padTop(10);
		
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
	
	private void scrUpd() {
		score+=100;
		scorelab.setText(score);
		
		if(score%500==0) {
			speed += 5;
			steptime = 1/speed;
		}
	}
	
}
