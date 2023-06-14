package com.mygdx.tablegame.cards;

import com.mygdx.tablegame.game_logic.GameScreen;
import com.mygdx.tablegame.game_logic.Server;

public class Crown extends Card {
    public Crown() {
        super(6);
        power_points=5;
        win_points=2;
        cost=7;
    }

    @Override
    public void played() {
        Server.player_now.setPower_points(Server.player_now.getPower_points()+power_points);
        GameScreen.getPlayer_UI_names()[Server.player_now.player_number]=Server.player_now.name+"`s power points  : "+Server.player_now.getPower_points();
    }
}
