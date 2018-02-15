package com.zootcat.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.zootcat.scene.ZootActor;

public class ZootEventTest
{
	@Test
	public void defaultCtorShouldSetDefaultValues()
	{
		assertEquals(ZootEventType.None, new ZootEvent().getType());		
	}
	
	@Test
	public void typeCtorShouldSetType()
	{
		assertEquals(ZootEventType.Attack, new ZootEvent(ZootEventType.Attack).getType());
		assertEquals(ZootEventType.Dead, new ZootEvent(ZootEventType.Dead).getType());
		assertEquals(ZootEventType.Update, new ZootEvent(ZootEventType.Update).getType());
	}
	
	@Test
	public void copyCtorShuoldCreateProperCopy()
	{
		//given
		ZootEventType type = ZootEventType.Dead;
		String userObj = "user object";
		
		ZootEvent e = new ZootEvent(type);
		e.setUserObject(userObj);
		
		//when
		ZootEvent copy = new ZootEvent(e);
		
		//then
		assertEquals(type, copy.getType());
		assertEquals(userObj, copy.getUserObject(String.class));
	}
	
	@Test
	public void shouldSetType()
	{
		ZootEvent event = new ZootEvent();
		
		event.setType(ZootEventType.WalkLeft);
		assertEquals(ZootEventType.WalkLeft, event.getType());
		
		event.setType(ZootEventType.RunRight);
		assertEquals(ZootEventType.RunRight, event.getType());
	}
	
	@Test
	public void toStringShouldProvideEventTypeName()
	{
		assertEquals(ZootEventType.Attack.toString(), new ZootEvent(ZootEventType.Attack).toString());
		assertEquals(ZootEventType.Dead.toString(), new ZootEvent(ZootEventType.Dead).toString());
		assertEquals(ZootEventType.Update.toString(), new ZootEvent(ZootEventType.Update).toString());
	}
	
	@Test
	public void shouldResetEvent()
	{
		ZootEvent event = new ZootEvent(ZootEventType.Attack);
		event.setUserObject("UserObj");
		
		event.reset();
		assertEquals(ZootEventType.None, event.getType());
		assertNull(event.getUserObject(String.class));
	}
	
	@Test
	public void shouldReturnTargetZootActor()
	{
		ZootEvent event = new ZootEvent();		
		assertEquals(null, event.getTargetZootActor());
		
		event.setTarget(new Actor());
		assertEquals("Should return null on Scene2D actor", null, event.getTargetZootActor());
		
		ZootActor zootActor = new ZootActor();
		event.setTarget(zootActor);
		assertEquals(zootActor, event.getTargetZootActor());
	}
	
	@Test
	public void shouldSetUserObject()
	{
		ZootEvent event = new ZootEvent();
		assertNull("Should return null if nothing was set", event.getUserObject(String.class));
		
		event.setUserObject("TestObj");
		assertEquals("TestObj", event.getUserObject(String.class));
		
		event.setUserObject(new Integer(128));
		assertEquals(new Integer(128), event.getUserObject(Integer.class));
		
		assertNull("Should return null on invalid cast", event.getUserObject(String.class));
		
		assertEquals("Should return default value on invalid cast", "default", event.getUserObject(String.class, "default"));
		assertEquals("Should return value on valid cast", new Integer(128), event.getUserObject(Integer.class, 128));
	}
}
