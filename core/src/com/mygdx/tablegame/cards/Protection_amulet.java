package com.mygdx.tablegame.cards;

import com.mygdx.tablegame.game_logic.Server;

public class Protection_amulet extends Card {
    public Protection_amulet(

    ) {
        super(5);
        power_points=0;
        win_points=1;
        cost=6;
    }

    @Override
    public void played() {
        Server.player_now.getArmor(5);
    }
}
