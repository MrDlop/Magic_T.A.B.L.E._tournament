package com.mygdx.tablegame.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.tablegame.tools.MyModelInstance;

//не игровая карта, а моделька колод игроков , наследуется от карты для универсальности(массив отрисовываемых объектов класса Card)
//#TODO сделать систему отрисовки объектов универсальнее
public class Deck extends Card {
    Model card_model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("Card_model.g3dj"));
    public Deck() {
        super(0);
        instance = new MyModelInstance(card_model);
        getModelInstance().transform.setToScaling(8.5f,185.5f,8.5f);
        change_texture(3);
    }
}
