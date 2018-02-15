package com.zootcat.hud;

import java.lang.reflect.Field;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;

public class ZootDebugWindow extends Window
{
	private static final int PAD_PX = 10;
	private static final int DEFAULT_WIDTH_PX = 400;
	private static final int DEFAULT_HEIGHT_PX = 400;
	private static final String WINDOW_TITLE = "Debug info";
	
	private ZootActor actor;
	
	public ZootDebugWindow(Skin skin)
	{
		super(WINDOW_TITLE, skin);
		setTouchable(Touchable.enabled);		
		setWidth(DEFAULT_WIDTH_PX);
		setHeight(DEFAULT_HEIGHT_PX);
		setResizable(true);
		setMovable(true);
		align(Align.topLeft);
		padLeft(PAD_PX);
		padRight(PAD_PX);
		padBottom(PAD_PX);
		addActorInfoToWindow();		
	}
		
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		if(visible)
		{
			pack();
			setDefaultPos();
		}
	}
	
	public void setDefaultPos()
	{
		float width = getWidth();
		float height = getHeight();		
		float stageWidth = getStage().getWidth();
		float stageHeight = getStage().getHeight();
		setPosition(stageWidth - width, stageHeight - height);
	}

	public void setDebugActor(ZootActor actor)
	{
		this.actor = actor;		
		clearChildren();		
		addActorInfoToWindow();
	}
		
	private void addActorInfoToWindow()
	{
		if(actor == null)
		{
			Label nothingSelected = new Label("CLICK ON ACTOR TO SHOW DEBUG INFORMATION", getSkin());
			nothingSelected.setColor(Color.BLACK);
			nothingSelected.setWrap(true);			
			nothingSelected.setAlignment(Align.center);
			add(nothingSelected).width(DEFAULT_WIDTH_PX).fill(true);
			getTitleLabel().setText(WINDOW_TITLE);
		}
		else
		{		
			changeTitle();
			addBasicInfo();		
			addControllerInfo();
		}
		pack();
	}

	private void changeTitle()
	{
		getTitleLabel().setText(WINDOW_TITLE + " (" + actor.getName() + ")");
	}

	private void addControllerInfo()
	{
		for(Controller ctrl : actor.getControllers())
		{
			ZootPropertiesTree tree = new ZootPropertiesTree(ctrl.getClass().getSimpleName(), getSkin(), this);			
			for(Field field : ControllerAnnotations.getControllerDebugFields(ctrl))
			{								
				field.setAccessible(true);
				tree.addProperty(field.getName(), new ZootDynamicLabel(getSkin(), () -> getFieldValue(field, ctrl).toString()));
			}
			
			add(tree).align(Align.topLeft);
			row();
		}
	}

	private Object getFieldValue(Field field, Controller ctrl)
	{
		try
		{
			return field.get(ctrl);
		} 
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			return e.getMessage();
		}		
	}
	
	private void addBasicInfo()
	{
		ZootPropertiesTree tree = new ZootPropertiesTree("BASIC PROPERTIES", getSkin(), this);
		tree.addProperty("ID: ", String.valueOf(actor.getId()));
		tree.addProperty("Name: ", actor.getName());
		tree.addProperty("Pos: ", new ZootDynamicLabel(getSkin(), () -> getPoint(actor.getX(), actor.getY())));
		tree.addProperty("Size: ", new ZootDynamicLabel(getSkin(), () -> getPoint(actor.getWidth(), actor.getHeight())));
		tree.addProperty("Rotation: ", new ZootDynamicLabel(getSkin(), () -> formatFloat(actor.getRotation())));
		add(tree).align(Align.topLeft);
		row();
	}
	
	private String getPoint(float a, float b)
	{
		return String.format("("+formatFloat(a)+"; "+formatFloat(b)+")");
	}
	
	private String formatFloat(float value)
	{
		return String.format("%.2f", value);
	}
}
