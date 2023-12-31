package com.mygdx.tablegame.game_logic;
// класс, содержащий названия игровых состояний, которые используются для удобной смены того, что отрисовывается в данный момент в методе рендера
public enum GameState {
    RUN,// основной игровой экран
    SELECT,// выбор цели для атаки
    CHANGE_PLAYER,// начало хода следующего игрока
    START,// стартовый экран
    CREATING,// создание игроков
    CREATING_ONLINE,//создание сессии(игра по сети)
    STARS_SESSION_WAITING,//ожидание начала онлайн игры
    CARD_LOOKING,//просмотр карты
    END;//конец игры
}
