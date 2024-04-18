package com.mygdx.tablegame.cards;

import com.mygdx.tablegame.game_logic.GameScreen;
import com.mygdx.tablegame.game_logic.Player;
import com.mygdx.tablegame.game_logic.Server;

import java.util.ArrayList;

public class SunFaced extends Card {
    public SunFaced() {
        super(9);
        power_points=2;
        win_points=2;
        cost=7;
    }

    @Override
    public void played() {
        Server.player_now.setPower_points(Server.player_now.getPower_points()+power_points);
        GameScreen.refreshPowerPoints();
        //Server.player_now.getCard();
        ArrayList<Player> targets = new ArrayList<>();
        for (int i = 0; i < Server.players_count; i++) {
            if (Server.players[i] != Server.player_now) {
                targets.add(Server.players[i]);
            }
        }
        if(targets.size()==1) targets.get(0).refresh_health(-10);
        else Server.attack(GameScreen.attack_target_selection(targets),-10);
    }
}
