package com.mygdx.tablegame;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.mygdx.tablegame.game_logic.GameController;
import com.mygdx.tablegame.network.NetworkUtils;

import java.lang.invoke.MethodHandles;

public class AndroidLauncher  extends AndroidApplication implements NetworkUtils{
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GameController(this), config);
	}
	public String getCurrentIP(){
		WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		final String IP_ADDRESS = Formatter.formatIpAddress(wifiInfo.getIpAddress());
		return IP_ADDRESS;
	}
}
