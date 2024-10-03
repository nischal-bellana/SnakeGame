package com.snake;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import game_states.GameStateManager;
import game_states.WrapMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class snakegame extends ApplicationAdapter {
	GameStateManager gsm;
	@Override
	public void create() {
		// TODO Auto-generated method stub
		gsm = new GameStateManager();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		gsm.resize(width,height);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		gsm.render();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		gsm.dispose();
	}
	
}