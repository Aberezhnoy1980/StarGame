package com.star.app.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class WorldRenderer {

    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font26;
    private StringBuilder sb;

    private Camera camera;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font26 = Assets.getInstance().getAssetManager().get("fonts/font26.ttf", BitmapFont.class);
        this.sb = new StringBuilder();
        this.camera = ScreenManager.getInstance().getCamera();
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0.4f, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        gc.getParticleController().render(batch);
        gc.getPowerUpsController().render(batch);
        camera.position.set(gc.getHero().getPosition().x, gc.getHero().getPosition().y, 0.0f);
        camera.update();
        ScreenManager.getInstance().getViewport().apply();
        batch.setProjectionMatrix(camera.combined);


        gc.getHero().renderGUI(batch, font26);
        batch.end();

        gc.getStage().draw();
    }
}
