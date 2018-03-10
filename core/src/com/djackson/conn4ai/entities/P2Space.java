package com.djackson.conn4ai.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class P2Space extends AbstractGameObject {
    private TextureRegion p2Space;
    private TextureAtlas atlas;
    public P2Space() { init(); }

    private void init() {
        // 100px orig (now 40, after maths)
        dimension.set(53f, 53f);
        bounds.set(0, 0, dimension.x, dimension.y);
        atlas = new TextureAtlas(Gdx.files.internal("use/con4cs470.pack"));
        p2Space = atlas.findRegion("p2space");
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(p2Space, position.x, position.y, dimension.x, dimension.y);
    }
}