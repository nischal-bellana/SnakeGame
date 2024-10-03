package game_states;

import com.badlogic.gdx.utils.viewport.Viewport;

public class State {
	protected GameStateManager gsm;
	protected Viewport vp;
	
	protected void create() {
		
	}
	protected void render() {
		
	}
	protected void dispose() {
		
	}
	protected void resize(int width, int height) {
		vp.update(width, height);
	}
}
