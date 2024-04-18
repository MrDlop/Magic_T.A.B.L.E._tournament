package com.mygdx.tablegame.cards;

import com.mygdx.tablegame.cards.Card;
import com.mygdx.tablegame.game_logic.GameScreen;
import com.mygdx.tablegame.game_logic.Server;

public class Summon extends Card {
    public Summon() {
        super(12);
        power_points=3;
        win_points=2;
        cost=5;
    }
    public void played() {
        Server.player_now.setPower_points(Server.player_now.getPower_points()+power_points);
        GameScreen.refreshPowerPoints();
    }
}
