package com.mygdx.tablegame.cards;

import com.mygdx.tablegame.cards.Card;

public class Pshik extends Card {
    public Pshik() {
        super(1);
        win_points=0;
        power_points=0;
        cost=0;
    }

    @Override
    public void played() {
    }
}
