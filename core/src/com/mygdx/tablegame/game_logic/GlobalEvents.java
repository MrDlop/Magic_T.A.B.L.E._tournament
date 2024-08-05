package com.mygdx.tablegame.game_logic;

import com.badlogic.ashley.signals.Listener;

import java.util.ArrayList;


public class GlobalEvents {
    public static ArrayList<EventReceiver> turnComplitedEventSigners = new ArrayList<EventReceiver>();
    public static ArrayList<EventReceiver> turnCompliteIntentEventSigners = new ArrayList<EventReceiver>();
    public static ArrayList<EventReceiver> turnStartedEventSigners = new ArrayList<EventReceiver>();
    public static ArrayList<EventReceiver> gameEndedEventSigners = new ArrayList<EventReceiver>();
    public static ArrayList<EventReceiver> gameStartedEventSigners = new ArrayList<EventReceiver>();

    public static void activate_turnComplitedEvent() {
        for (EventReceiver e : turnComplitedEventSigners) {
            e.turnComplited();
        }
    }

    public static void activate_turnCompliteIntentEvent() {
        for (EventReceiver e : turnCompliteIntentEventSigners) {
            e.turnCompliteIntent();
        }
    }

    public static void activate_turnStartedEvent() {
        for (EventReceiver e : turnStartedEventSigners) {
            e.turnStarted();
        }
    }

    public static void activate_gameEndedEvent() {
        for (EventReceiver e : gameEndedEventSigners) {
            e.gameEnded();
        }
    }

    public static void activate_gameStartedEvent() {
        for (EventReceiver e : gameStartedEventSigners) {
            e.gameStarted();
        }
    }

}
