package com.mygdx.tablegame.game_logic;

public interface EventReceiver {
    void turnComplited();
    void turnStarted();
    void gameEnded();
    void gameStarted();
}
