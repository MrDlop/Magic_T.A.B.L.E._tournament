package com.mygdx.tablegame;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.tablegame.game_logic.GameController;

public class AndroidLauncher extends AndroidApplication {
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GameController(), config);
	}
//	public void kill_app(){
//		Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//		homeIntent.addCategory( Intent.CATEGORY_HOME );
//		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(homeIntent);
//	}
}
