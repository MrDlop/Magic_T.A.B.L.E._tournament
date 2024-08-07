package com.mygdx.tablegame.cards;

import com.mygdx.tablegame.game_logic.GameScreen;
import com.mygdx.tablegame.game_logic.Server;

import java.util.Random;

public class Znak extends Card {
    public Znak() {
        super(2);
        win_points=0;
        power_points=1;
        cost=0;
    }

    @Override
    public void played() {
        Random random=new Random();
        if(random.nextInt(2)==1) Server.player_now.setPower_points(Server.player_now.getPower_points()+power_points);
        Server.player_now.setPower_points(Server.player_now.getPower_points()+power_points);
        GameScreen.refreshPowerPoints();
    }


}
