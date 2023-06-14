package com.mygdx.tablegame.tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.tablegame.game_logic.CanTouch;

// класс, используемый для отображения различных элементов экранного интерфейса, использует систему текстур, как в классе карты
public class ElementUI {
    public Sprite sprite;
    private final int texture_id;

    public ElementUI(int texture_id) {
        this.texture_id = texture_id;
        sprite = new Sprite(TextureStorage.textures_UI[texture_id][0], TextureStorage.textures_UI[texture_id][0].getWidth(), TextureStorage.textures_UI[texture_id][0].getHeight());
        change_texture(1);
        CanTouch.UI_elements.add(this);
    }


    public void change_texture(int type) {
        if (type == -1) {
            sprite.setAlpha(0);
        }
        else sprite.setAlpha(1);
        if (type == 1) {
            if(!CanTouch.UI_elements.contains(this)) CanTouch.UI_elements.add(this);
            sprite.setTexture(TextureStorage.textures_UI[texture_id][0]);
        }
        if (type == 2) {
            if(!CanTouch.UI_elements.contains(this)) CanTouch.UI_elements.add(this);
            sprite.setTexture(TextureStorage.textures_UI[texture_id][1]);
        }
        if (type == 3) {
            if(!CanTouch.UI_elements.contains(this)) CanTouch.UI_elements.add(this);
            sprite.setTexture(TextureStorage.textures_UI[texture_id][2]);
        }
        if (type == 4) {
            if(!CanTouch.UI_elements.contains(this)) CanTouch.UI_elements.add(this);
            sprite.setTexture(TextureStorage.textures_UI[texture_id][3]);
        }
    }
}
