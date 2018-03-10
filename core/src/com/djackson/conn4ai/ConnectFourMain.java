package com.djackson.conn4ai;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ConnectFourMain extends ApplicationAdapter {
	public BoardManager manager;

	@Override
	public void create () {
		int initBoard[][] = createBoard();
		manager = new BoardManager(initBoard);
	}

	@Override
	public void render () {
		manager.render();
		if (manager.gameover) {
			create();
		}
	}


	public int[][] createBoard() {

		int[][]spaces = {{0,0,0,0,0,0,0}, //r1
				{0,0,0,0,0,0,0}, //r2
				{0,0,0,0,0,0,0}, //r3
				{0,0,0,0,0,0,0}, //r4
				{0,0,0,0,0,0,0}, //r5
				{0,0,0,0,0,0,0}}; //r6
		return spaces;
	}
}
