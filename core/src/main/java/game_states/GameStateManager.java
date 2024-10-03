package game_states;

public class GameStateManager {
	State st;
	State next_st;
	
	public GameStateManager(){
		st = new StartState();
		st.gsm = this;
	}
	public GameStateManager(State st) {
		this.st = st;
		st.gsm = this;
	}
	
	public void render() {
		st.render();
		
		if(next_st!=null) {
			st.dispose();
			st = next_st;
			next_st = null;
		}
	}
	
	public void dispose() {
		next_st = null;
		st.dispose();
	}
	public void resize(int width,int height) {
		st.resize(width,height);
	}
}
