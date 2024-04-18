package com.mygdx.tablegame.cards;

import com.mygdx.tablegame.game_logic.GameScreen;
import com.mygdx.tablegame.game_logic.Server;

public class SmallKnight extends Card {
    public SmallKnight() {
        super(8);
        power_points=2;
        win_points=1;
        cost=2;
    }

    @Override
    public void played() {
        Server.player_now.setPower_points(Server.player_now.getPower_points()+power_points);
        GameScreen.refreshPowerPoints();
    }
}
