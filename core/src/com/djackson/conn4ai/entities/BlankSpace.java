package com.djackson.conn4ai.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BlankSpace extends AbstractGameObject {
    private TextureRegion blankSpace;
    private TextureAtlas atlas;
    public BlankSpace() { init(); }

    private void init() {
        // 100px orig (now 40, after maths)
        dimension.set(55f, 55f);
        bounds.set(0, 0, dimension.x, dimension.y);
        atlas = new TextureAtlas(Gdx.files.internal("use/con4cs470.pack"));
        blankSpace = atlas.findRegion("blankspace");
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(blankSpace, position.x, position.y, dimension.x, dimension.y);
    }
}
