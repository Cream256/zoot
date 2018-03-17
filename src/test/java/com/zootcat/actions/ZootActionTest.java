package com.zootcat.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.zootcat.scene.ZootActor;

public class ZootActionTest
{
	private ZootAction action;
	
	@Before
	public void setup()
	{
		action = new ZootAction(){
			@Override
			public boolean act(float delta)
			{
				return false;
			}};
	}
	
	@Test
	public void shouldReturnNullActionZootActorByDefault()
	{
		assertNull(action.getActionZootActor());
	}
	
	@Test
	public void shouldReturnNullActionTargetActorByDefault()
	{
		assertNull(action.getTargetZootActor());
	}
	
	@Test
	public void shouldSetTargetZootActor()
	{
		//given
		ZootActor actor = new ZootActor();
		
		//when
		action.setTarget(actor);
		
		//then
		assertEquals(actor, action.getTargetZootActor());
	}
	
	@Test
	public void shouldNotSetTargetZootActor()
	{
		//given
		Actor actor = new Actor();
		
		//when
		action.setTarget(actor);
		
		//then
		assertNull(action.getTargetZootActor());
	}
	
	@Test
	public void shouldSetActionZootActor()
	{
		//given
		ZootActor actor = new ZootActor();
		
		//when
		action.setActor(actor);
		
		//then
		assertEquals(actor, action.getActionZootActor());
	}
	
	@Test
	public void shouldNotSetActionZootActor()
	{
		//given
		Actor actor = new Actor();
		
		//when
		action.setActor(actor);
		
		//then
		assertNull(action.getActionZootActor());
	}
	
	@Test
	public void shouldResetActionAndTargetActor()
	{
		//given
		ZootActor actor = new ZootActor();
		
		//when
		action.setActor(actor);
		action.setTarget(actor);
		action.reset();
		
		//then
		assertNull(action.getActionZootActor());
		assertNull(action.getTargetZootActor());
	}
	
}
