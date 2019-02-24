package com.zootcat.gfx;

import com.badlogic.gdx.utils.Disposable;
import com.zootcat.camera.ZootCamera;

public interface ZootRender extends Disposable
{
	void render(float delta);
	void setView(ZootCamera camera); 
}
