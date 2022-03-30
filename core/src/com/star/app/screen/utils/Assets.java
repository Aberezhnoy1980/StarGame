package com.star.app.screen.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.star.app.screen.ScreenManager;

public class Assets {

    private static final Assets ourInstance = new Assets();
    private final AssetManager assetManager;
    private TextureAtlas textureAtlas;
    private Assets() {
        assetManager = new AssetManager();
    }

    public static Assets getInstance() {
        return ourInstance;
    }

    public TextureAtlas getAtlas() {
        return textureAtlas;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void loadAssets(ScreenManager.ScreenType type) {
        switch (type) {
            case GAME:
                assetManager.load("images/game.pack.atlas", TextureAtlas.class);
                createStandardFont(26);
                createStandardFont(20);
                break;
            case MENU:
                assetManager.load("images/game.pack.atlas", TextureAtlas.class);
                createStandardFont(72);
                createStandardFont(24);
                createStandardFont(18);
                break;
            case GAME_OVER:
                assetManager.load("images/game.pack.atlas", TextureAtlas.class);
                createStandardFont(72);
                createStandardFont(24);
                createStandardFont(36);
                break;
        }
    }

    private void createStandardFont(int size) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameter.fontFileName = "fonts/Roboto-Medium.ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.WHITE;
        fontParameter.fontParameters.shadowOffsetX = 1;
        fontParameter.fontParameters.shadowOffsetY = 1;
        fontParameter.fontParameters.shadowColor = Color.DARK_GRAY;
        assetManager.load("fonts/font" + size + ".ttf", BitmapFont.class, fontParameter);
    }

    public void clear() {
        assetManager.clear();
    }

    public void makeLinks() {
        textureAtlas = assetManager.get("images/game.pack.atlas", TextureAtlas.class);
    }
}
