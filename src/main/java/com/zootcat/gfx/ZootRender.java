package com.zootcat.gfx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;

public interface ZootRender extends Disposable
{
	void render(float delta);
	void setView(OrthographicCamera camera);
}
