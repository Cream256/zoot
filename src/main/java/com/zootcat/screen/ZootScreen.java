package com.zootcat.screen;

import com.badlogic.gdx.Screen;

public interface ZootScreen extends Screen
{
	void onRender(float delta);
	void onUpdate(float delta);
}