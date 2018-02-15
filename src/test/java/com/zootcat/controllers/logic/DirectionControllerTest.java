package com.zootcat.controllers.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zootcat.scene.ZootDirection;

public class DirectionControllerTest
{
	@Test
	public void ctorTest()
	{
		DirectionController ctrl = new DirectionController();
		assertEquals(ZootDirection.Right, ctrl.getDirection());
	}
	
	@Test
	public void setDirectionTest()
	{
		DirectionController ctrl = new DirectionController();
		
		ctrl.setDirection(ZootDirection.Left);
		assertEquals(ZootDirection.Left, ctrl.getDirection());
		
		ctrl.setDirection(ZootDirection.Right);
		assertEquals(ZootDirection.Right, ctrl.getDirection());
		
		ctrl.setDirection(ZootDirection.Up);
		assertEquals(ZootDirection.Up, ctrl.getDirection());
		
		ctrl.setDirection(ZootDirection.Down);
		assertEquals(ZootDirection.Down, ctrl.getDirection());
	}
}
