package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.physics.ZootBodyShape;
import com.zootcat.physics.ZootFixtureDefBuilder;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.tools.physicsbodyeditor.PhysicsBodyEditorModel;

public class PhysicsBodyEditorFixtureController extends ControllerAdapter
{
	@CtrlParam(required = true) protected String fileName;
	@CtrlParam(required = true) protected String fixtureName;
	@CtrlParam(required = true) protected float fixtureScaleX;
	@CtrlParam(required = true) protected float fixtureScaleY;
	@CtrlParam protected float density = 1.0f;
	@CtrlParam protected float friction = 0.2f;
	@CtrlParam protected float restitution = 0.0f;
	@CtrlParam protected float offsetX = 0.0f;
	@CtrlParam protected float offsetY = 0.0f;
	@CtrlParam protected float width = 0.0f;
	@CtrlParam protected float height = 0.0f;
	@CtrlParam protected boolean sensor = false;
	@CtrlParam protected String category = "";
	@CtrlParam protected String mask = "";
	@CtrlParam(global = true) protected ZootScene scene;
	@CtrlParam(global = true) protected ZootAssetManager assetManager;
		
	protected PhysicsBodyEditorModel model;
	
	@Override
	public void init(ZootActor actor)
	{		
		model = assetManager.get(fileName, PhysicsBodyEditorModel.class);
	}
		
	@Override
	public void onAdd(ZootActor actor)
	{
		model.attachFixture(actor, fixtureName, createFixtureDef(actor), fixtureScaleX, fixtureScaleY);
	}
	
	private FixtureDef createFixtureDef(ZootActor actor)
	{
		FixtureDef fixtureDef = new ZootFixtureDefBuilder(scene)
				.setDensity(density)
				.setFriction(friction)
				.setRestitution(restitution)
				.setOffsetX(offsetX)
				.setOffsetY(offsetY)
				.setWidth(width)
				.setHeight(height)
				.setSensor(sensor)
				.setShape(ZootBodyShape.NONE)
				.setCategory(category)
				.setMask(mask)
				.build(actor);
		return fixtureDef;
	}	
}
