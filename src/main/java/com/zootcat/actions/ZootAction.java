package com.zootcat.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.scene.ZootActor;

public abstract class ZootAction extends Action
{
	private ZootActor actionActor;
	private ZootActor targetActor;
	
	@Override
	public void setActor (Actor actor) 
	{
		super.setActor(actor);
		if(ClassReflection.isInstance(ZootActor.class, actor))
		{
			actionActor = (ZootActor) actor;
		}
	}

	@Override
	public void setTarget (Actor target) 
	{
		super.setTarget(target);
		if(ClassReflection.isInstance(ZootActor.class, target))
		{
			targetActor = (ZootActor) target;
		}
	}
	
	@Override
	public void reset() 
	{
		actionActor = null;
		targetActor = null;
		super.reset();
	}
	
	public ZootActor getActionZootActor()
	{
		return actionActor;
	}
	
	public ZootActor getTargetZootActor()
	{
		return targetActor;
	}
}
