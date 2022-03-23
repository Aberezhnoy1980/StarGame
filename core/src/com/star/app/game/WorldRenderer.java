package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.utils.Assets;

public class WorldRenderer {

    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font26;
    private StringBuilder sb;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font26 = Assets.getInstance().getAssetManager().get("fonts/font26.ttf", BitmapFont.class);
        this.sb = new StringBuilder();
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0.4f, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        sb.setLength(0);
        sb.append("SCORE:").append(gc.getHero().getScoreView()).append("\n");
        sb.append("HP: ").append(gc.getHero().getHp());
        font26.draw(batch, sb, 20, 700);
        batch.end();
    }
}
