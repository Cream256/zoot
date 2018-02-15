package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootDirection;

public class ZootStateUtilsTest
{
	@Test
	public void isMoveEventTest()
	{
		assertTrue(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.WalkLeft)));
		assertTrue(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.WalkRight)));
		assertTrue(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.RunLeft)));
		assertTrue(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.RunRight)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Attack)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Collide)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Dead)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Fall)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Jump)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Ground)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.None)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Stop)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Update)));
	}
	
	@Test
	public void getDirectionFromEventTest()
	{
		assertEquals(ZootDirection.Left, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.WalkLeft)));
		assertEquals(ZootDirection.Left, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.RunLeft)));
		assertEquals(ZootDirection.Right, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.WalkRight)));
		assertEquals(ZootDirection.Right, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.RunRight)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Attack)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Collide)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Dead)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Fall)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Jump)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Ground)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.None)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Stop)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Update)));	
	}
	
	@Test
	public void isRunEventTest()
	{
		assertTrue(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.RunLeft)));
		assertTrue(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.RunRight)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.WalkLeft)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.WalkRight)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.Attack)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.Collide)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.Stop)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.Update)));
	}
}
