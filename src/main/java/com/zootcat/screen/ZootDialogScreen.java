package com.zootcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.zootcat.dialogs.ZootDialog;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.game.ZootGame;
import com.zootcat.gfx.ZootGraphicsFactory;
import com.zootcat.input.ZootBindableInputProcessor;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

/**
 * ZootDialogScreen - screen for displaying a dialog. The dialog is overlayed on top
 * of the previous screen. Previous screen and dialog must be supplied.
 * 
 * @author Cream
 * @remarks Needs BETA version improvements
 */
public class ZootDialogScreen extends ZootScreenAdapter
{
	private ZootGame game;
	private ZootDialog dialog;
	private ZootActor triggeringActor;
	private ZootScreen previousScreen;
	
	private BitmapFont font;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
		
	public ZootDialogScreen(ZootGame game, ZootGraphicsFactory graphicsFactory)
	{
		this.game = game;
		this.batch = graphicsFactory.createSpriteBatch();
		this.shapeRenderer = graphicsFactory.createShapeRenderer();
		this.font = graphicsFactory.createBitmapFont();
	}
	
	@Override
	public void show()
	{
		if(dialog == null) throw new RuntimeZootException("No dialog was set for DialogScreen");
		dialog.rewind();
		
		previousScreen = game.getPreviousScreen();
		if(previousScreen == null) throw new RuntimeZootException("No previous screen was set for DialogScreen");
		
		//input
		ZootBindableInputProcessor inputProcessor = new ZootBindableInputProcessor();
		inputProcessor.bindDown(Keys.ENTER, () -> advanceDialog());
		inputProcessor.bindDown(Keys.SPACE, () -> advanceDialog());
		inputProcessor.bindDown(Keys.ESCAPE, () -> quitDialog());
		game.getInputManager().addProcessor(inputProcessor);
	}
	
	@Override
	public void hide() 
	{
		game.getInputManager().clear();
		game.getInputManager().clearPressedKeys();
	}
		
	private boolean advanceDialog()
	{		
		dialog.nextFrame();	
		return true;
	}
	
	private boolean quitDialog()
	{
		dialog.forceFinish();
		return true;
	}
	
	@Override
	public void render(float delta)
	{
		if(dialog.finished())
		{
			game.setScreen(previousScreen);
			return;
		}
				
		//render previous screen
		if(dialog.isRealTime()) previousScreen.onUpdate(delta);
		previousScreen.onRender(delta);
				
		//get face
		Texture face = dialog.getCurrentFace();
		
		//draw background
		float backWidth = Gdx.graphics.getWidth();
		float backHeight = Gdx.graphics.getHeight() / 3.0f;
		
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 0.5f);
		shapeRenderer.rect(0.0f, 0.0f, backWidth, backHeight);			
		shapeRenderer.end();
		
		//draw face		
		batch.begin();		
		if(face != null) 
		{			
			float faceX = dialog.getFacePosition() == ZootDirection.Left ? 0.0f : backWidth - face.getWidth();
			batch.draw(face, faceX, 0);
		}
		
		//draw text
		String text = dialog.getVisibleText();
		float textX = face != null ? face.getWidth() + 10 : 0.0f;
		float textY = face != null ? face.getHeight() / 2.0f : 0.0f;
		font.setColor(Color.BLACK);
		font.draw(batch, text, textX, textY);		
		batch.end();
				
		//update dialog
		dialog.update(delta);		
	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}
	
	public void setDialog(ZootDialog dialog)
	{
		this.dialog = dialog;
	}
	
	public ZootDialog getDialog()
	{
		return dialog;
	}
	
	public void setTriggeringActor(ZootActor actor)
	{
		this.triggeringActor = actor;
	}
	
	public ZootActor getTriggeringActor()
	{
		return triggeringActor;
	}
}
