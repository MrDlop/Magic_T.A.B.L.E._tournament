package com.mygdx.tablegame.cards;

import com.mygdx.tablegame.game_logic.Server;

public class Magic_dog extends Card {
    public Magic_dog() {
        super(3);
        power_points=0;
        win_points=1;
        cost=2;
    }

    @Override
    public void played() {
        //в некоторых случаях приводит к падению приложения #TODO исправить критическую ошибку!(условия возникновения неизвестны, проявлялась лишь на части тестовых устройств)
        Server.attack(Server.player_now,2);
        Server.player_now.getCard();// с большей вероятность приводит к ошибке
    }
}
