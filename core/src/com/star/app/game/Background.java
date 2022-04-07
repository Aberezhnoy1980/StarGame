package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Background {
    private class Star {
        private final Vector2 position;
        private final Vector2 velocity;
        private float scale;

        public Star() {
            this.position = new Vector2(MathUtils.random(-200, ScreenManager.SPACE_WIDTH + 200),
                    MathUtils.random(-200, ScreenManager.SPACE_HEIGHT + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            scale = Math.abs(velocity.x / 40f) * 0.8f;
        }

        public void update(float dt) {
            if (gc != null) {
                position.x += (velocity.x - gc.getHero().getVelocity().x * 0.1f) * dt;
                position.y += (velocity.y - gc.getHero().getVelocity().y * 0.1f) * dt;
            } else {
                position.mulAdd(velocity, dt);
            }

            if (position.x < -200) {
                position.x = ScreenManager.SPACE_WIDTH + 200;
                position.y = MathUtils.random(0, ScreenManager.SPACE_HEIGHT);
                scale = Math.abs(velocity.x / 40f) * 0.8f;
            }
        }
    }

    private class BlackHole {
        private final Vector2 position;
        private final Vector2 velocity;
        private final Circle hitArea;
        private final float rotationSpeed;
        private float angle;

        public BlackHole() {
            this.position = new Vector2(MathUtils.random(ScreenManager.SPACE_WIDTH),
                    MathUtils.random(ScreenManager.SPACE_HEIGHT));
            this.velocity = new Vector2(MathUtils.random(-100, -100), MathUtils.random(-100, -100));
            this.hitArea = new Circle(position.x, position.y, 90);
            this.rotationSpeed = MathUtils.random(-180.0f, 180.0f);
            this.angle = MathUtils.random(0.0f, 360.0f);
        }

        public void update(float dt) {
            position.mulAdd(velocity, dt);
            angle += rotationSpeed * dt;

            if (position.x < 0) {
                position.x = ScreenManager.SPACE_WIDTH;
                position.y = MathUtils.random(0, ScreenManager.SPACE_HEIGHT);

            }
            if (position.x > ScreenManager.SPACE_WIDTH) {
                position.x = 0;
                position.y = MathUtils.random(0, ScreenManager.SPACE_HEIGHT);
            }

            if (position.y < 0) {
                position.y = ScreenManager.SPACE_HEIGHT;
                position.x = MathUtils.random(0, ScreenManager.SPACE_WIDTH);
            }
            if (position.y > ScreenManager.SPACE_HEIGHT) {
                position.y = 0;
                position.x = MathUtils.random(0, ScreenManager.SPACE_WIDTH);
            }
            hitArea.setPosition(position);
        }
    }

    private final int STAR_COUNT = 600;
    private final Texture textureCosmos;
    private final Texture textureBlackHole;
    private final TextureRegion textureStar;
    private final GameController gc;
    private final Star[] stars;
    private final BlackHole blackHole;

    public Background(GameController gc) {
        this.gc = gc;
        this.textureCosmos = new Texture("images/galaxy3840x2160.jpg");
        this.textureBlackHole = new Texture("images/blackHole.png");
        this.textureStar = Assets.getInstance().getAtlas().findRegion("star16");
        this.stars = new Star[STAR_COUNT];
        this.blackHole = new BlackHole();
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);
        batch.draw(textureBlackHole, blackHole.position.x - 128, blackHole.position.y - 128);
        for (int i = 0; i < stars.length; i++) {
            batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8,
                    16, 16, stars[i].scale, stars[i].scale,
                    0);
            if (MathUtils.random(300) < 1) {
                batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8,
                        16, 16, stars[i].scale * 2, stars[i].scale * 2,
                        0);
            }
        }
    }

    public void update(float dt) {
        blackHole.update(dt);
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(dt);
        }
    }

    public void dispose() {
        textureCosmos.dispose();
    }

}
