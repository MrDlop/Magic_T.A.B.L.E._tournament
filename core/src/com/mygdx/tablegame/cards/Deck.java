package com.mygdx.tablegame.cards;

//не игровая карта, а моделька колод игроков , наследуется от карты для универсальности(массив отрисовываемых объектов класса Card)
//#TODO сделать систему отрисовки объектов универсальнее
public class Deck extends Card {
    public Deck() {
        super(0);
        getModelInstance().transform.setToScaling(8.5f,180,8.5f);
    }
}
