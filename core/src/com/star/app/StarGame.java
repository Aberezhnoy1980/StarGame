package com.star.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class StarGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Background background;




    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Background(this);
    }

    @Override
    public void render() {
		float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        ScreenUtils.clear(0, 0, 0.4f, 1);
        batch.begin();
        batch.end();
    }

    public void update(float dt) {

        background.update(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
