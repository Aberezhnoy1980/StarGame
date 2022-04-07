package com.star.app.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class WorldRenderer {

    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font26;
    private BitmapFont font72;
    private StringBuilder sb;

    private Camera camera;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font26 = Assets.getInstance().getAssetManager().get("fonts/font26.ttf", BitmapFont.class);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf", BitmapFont.class);
        this.sb = new StringBuilder();
        this.camera = ScreenManager.getInstance().getCamera();
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0.9f, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        gc.getParticleController().render(batch);
        gc.getPowerUpsController().render(batch);
        gc.getInfoController().render(batch, font26);
        gc.getBotController().render(batch);
        camera.position.set(gc.getHero().getPosition().x, gc.getHero().getPosition().y, 0.0f);
        camera.update();
        ScreenManager.getInstance().getViewport().apply();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < gc.getBotController().getActiveList().size(); i++) {
            Bot b = gc.getBotController().getActiveList().get(i);
            b.renderBotGUI(batch, font26);
        }

        gc.getHero().renderHeroGUI(batch, font26);
        if (gc.getTimer() < 3) {
            sb.setLength(0);
            sb.append("Level ").append(gc.getLevel());
            font72.draw(batch, sb, gc.getHero().getPosition().x - 72.0f, gc.getHero().getPosition().y + 100.0f);
        }
        batch.end();

        gc.getStage().draw();
    }
}
