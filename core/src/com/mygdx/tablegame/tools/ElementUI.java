package com.mygdx.tablegame.tools;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.tablegame.game_logic.RenderController;

// класс, используемый для отображения различных элементов экранного интерфейса, использует систему текстур, как в классе карты
public class ElementUI {
    public Sprite sprite;
    private final int texture_id;
    public BitmapFont font;

    public ElementUI(int texture_id,BitmapFont font) {
        this.texture_id = texture_id;
        sprite = new Sprite(AssetStorage.textures_UI[texture_id][0], AssetStorage.textures_UI[texture_id][0].getWidth(), AssetStorage.textures_UI[texture_id][0].getHeight());
        change_texture(1);
        RenderController.UI_elements.add(this);
        this.font=font;
    }


    public void change_texture(int type) {
        if (type == -1) {
            sprite.setAlpha(0);
        }
        else sprite.setAlpha(1);
        if (type == 1) {
            if(!RenderController.UI_elements.contains(this)) RenderController.UI_elements.add(this);
            sprite.setTexture(AssetStorage.textures_UI[texture_id][0]);
        }
        if (type == 2) {
            if(!RenderController.UI_elements.contains(this)) RenderController.UI_elements.add(this);
            sprite.setTexture(AssetStorage.textures_UI[texture_id][1]);
        }
        if (type == 3) {
            if(!RenderController.UI_elements.contains(this)) RenderController.UI_elements.add(this);
            sprite.setTexture(AssetStorage.textures_UI[texture_id][2]);
        }
        if (type == 4) {
            if(!RenderController.UI_elements.contains(this)) RenderController.UI_elements.add(this);
            sprite.setTexture(AssetStorage.textures_UI[texture_id][3]);
        }
    }
}
