package com.zootcat.controllers.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.zootcat.controllers.Controller;
import com.zootcat.scene.ZootActor;

/**
 * Interface used by controllers that has rendering ability.
 * @author Cream
 * @see Controller
 */
public interface RenderController extends Controller
{
	void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta);
	void setOffset(float x, float y);
}
