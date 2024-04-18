package com.mygdx.tablegame.cards;

import com.mygdx.tablegame.game_logic.Server;

public class SpellBook extends Card {
    public SpellBook() {
        super(10);
        power_points=0;
        win_points=1;
        cost=5;
    }

    @Override
    public void played() {
        Server.player_now.getCardToHand();
        Server.player_now.getCardToHand();
    }
}
