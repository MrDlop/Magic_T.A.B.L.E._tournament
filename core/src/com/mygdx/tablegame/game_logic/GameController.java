package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.mygdx.tablegame.tools.AssetStorage;

import java.util.Timer;
import java.util.function.Function;

// основной класс, его экземпляр создается при запуске приложения
//использует систему экранов, предлагаемую LibGDX
public  class GameController extends Game {
	private MainMenuScreen menu;  //экран главного меню
	public static GameState state;  //энумератор, используемый для смены состояний экранов
	public static final String log_tag="MyApp"; //тег для логов
	public static Timer timer;
	public static final Constants constants=new Constants();
	@Override
	public void create () {
		AssetStorage tx=new AssetStorage(); //инициализация хранилища текстур
		menu=new MainMenuScreen(this); //создание экрана меню
		this.setScreen(menu);//установка экрана
		timer=new Timer();
	}

	@Override
	public void render () {
		super.render(); // вызывает метод рендера установленного на данный момент экрана
	}

	@Override
	public void dispose () {
	}
}

