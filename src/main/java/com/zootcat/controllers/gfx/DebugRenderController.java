package com.zootcat.controllers.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class DebugRenderController extends ControllerAdapter implements RenderController 
{
	private static ShapeRenderer shapeRender = null;
	
	@CtrlParam private String color = Color.PINK.toString();
	@CtrlParam(global = true) private ZootScene scene;
	
	private Color renderColor;
	
	@Override
	public void init(ZootActor actor)
	{
		renderColor = Color.valueOf(color);
		if(shapeRender == null)
		{
			//initialization must happen here due to failure in test initialization
			shapeRender = new ShapeRenderer(64);
		}
	}
	
	@Override
	public void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta)
	{
		if(!scene.isDebugMode())
		{
			return;
		}
		
		//setup
		Color oldColor = batch.getColor();		
		batch.end();
		
		//draw body	
		shapeRender.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRender.begin(ShapeType.Filled);
		shapeRender.setColor(renderColor);
		shapeRender.rect(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
		shapeRender.end();
		
		//draw border
		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(Color.BLACK);						
		shapeRender.rect(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
		shapeRender.end();
				
		//reset
		batch.begin();
		batch.setColor(oldColor);		
	}
}
