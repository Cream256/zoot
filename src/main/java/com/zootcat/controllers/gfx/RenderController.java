package com.zootcat.controllers.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.zootcat.controllers.Controller;
import com.zootcat.scene.ZootActor;

public interface RenderController extends Controller
{
	void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta);
}
