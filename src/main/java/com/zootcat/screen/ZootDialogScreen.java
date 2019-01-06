package com.zootcat.screen;

import java.util.function.Consumer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.zootcat.dialogs.ZootDialog;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.game.ZootGame;
import com.zootcat.input.ZootBindableInputProcessor;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

/**
 * ZootDialogScreen - screen for displaying a dialog. The dialog is overlayed on top
 * of the previous screen. Previous screen and dialog must be supplied.
 * 
 * @author Cream
 * @remarks Needs BETA version improvements: custom layout (from file?), remove need for previous screen
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
	private Sprite faceSprite;
	
	private Consumer<ZootGame> onShowAction;
	private Consumer<ZootGame> onHideAction;
		
	public ZootDialogScreen(ZootGame game)
	{
		this.game = game;
		this.batch = game.getGraphicsFactory().createSpriteBatch();
		this.shapeRenderer = game.getGraphicsFactory().createShapeRenderer();
		this.font = game.getGraphicsFactory().createBitmapFont();
		this.faceSprite = game.getGraphicsFactory().createSprite();
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
		inputProcessor.bindUp(Keys.ENTER, () -> advanceDialog());
		inputProcessor.bindUp(Keys.SPACE, () -> advanceDialog());
		inputProcessor.bindUp(Keys.ESCAPE, () -> quitDialog());
		game.getInputManager().addProcessor(inputProcessor);
		
		//on show action
		if(onShowAction != null) onShowAction.accept(game);		
	}
	
	@Override
	public void hide() 
	{
		game.getInputManager().removeAllProcessors();
		game.getInputManager().clearPressedKeys();
		if(onHideAction != null) onHideAction.accept(game);
	}
		
	public boolean advanceDialog()
	{
		dialog.nextFrame();	
		return true;
	}
	
	public boolean quitDialog()
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
			faceSprite.setTexture(face);
			faceSprite.setRegion(face);
			faceSprite.flip(dialog.getFacePosition() == ZootDirection.Left ? false : true, false);
			
			float faceX = dialog.getFacePosition() == ZootDirection.Left ? 0.0f : backWidth - face.getWidth();
			faceSprite.setBounds(faceX, 0, face.getWidth(), face.getHeight());
			faceSprite.draw(batch);
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
	
	public void setOnShowAction(Consumer<ZootGame> action)
	{
		onShowAction = action;
	}
	
	public Consumer<ZootGame> getOnShowAction()
	{
		return onShowAction;
	}
	
	public void setOnHideAction(Consumer<ZootGame> action)
	{
		onHideAction = action;
	}
	
	public Consumer<ZootGame> getOnHideAction()
	{
		return onHideAction;
	}
}
