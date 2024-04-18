package com.mygdx.tablegame.cards;

import com.mygdx.tablegame.cards.Card;
import com.mygdx.tablegame.game_logic.GameScreen;
import com.mygdx.tablegame.game_logic.Player;
import com.mygdx.tablegame.game_logic.Server;

import java.util.ArrayList;

public class Princess extends Card {
    public Princess() {
        super(7);
        power_points=2;
        win_points=1;
        cost=3;
    }

    @Override
    public void played() {
        ArrayList<Player> targets = new ArrayList<>();
        for (int i = 0; i < Server.players_count; i++) {
            if (Server.players[i] != Server.player_now) {
                targets.add(Server.players[i]);
            }
        }
        Player target=GameScreen.attack_target_selection(targets);
        Card card=target.getCard();
        Server.attack(target,-1*card.cost);
        target.trash.add(card);
        Server.player_now.setPower_points(Server.player_now.getPower_points()+power_points);
        GameScreen.refreshPowerPoints();
    }
}
