package com.djackson.conn4ai.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {
        public Vector2 position;
        public Vector2 dimension;
        public Rectangle bounds;
        public boolean active;

        public AbstractGameObject() {
            position = new Vector2();
            dimension = new Vector2(1, 1);
            bounds = new Rectangle();
            active = false;
        }

        public abstract void render (SpriteBatch batch);
}
