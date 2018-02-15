package com.zootcat.controllers.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;

public class OnTimerControllerTest
{
	private int executionCount;
	private OnTimerController ctrl;
	private ZootActor actor;
	
	@Before
	public void setup()
	{
		executionCount = 0;		
		actor = mock(ZootActor.class);
		ctrl = new OnTimerController()
		{
			@Override
			public void onTimer(float delta, ZootActor actor)
			{
				++executionCount;
			}
		};
		ctrl.init(actor);
	}
	
	@Test
	public void shouldExecuteOnlyOnceWhenRepeatIsSetToFalseTest()
	{
		ControllerAnnotations.setControllerParameter(ctrl, "interval", 1.0f);
		ControllerAnnotations.setControllerParameter(ctrl, "repeat", false);
		
		ctrl.onUpdate(0.0f, actor);
		assertEquals("OnTimer should not execute", 0, executionCount);
		
		ctrl.onUpdate(0.5f, actor);
		assertEquals("OnTimer should not execute", 0, executionCount);
		
		ctrl.onUpdate(0.5f, actor);
		assertEquals("OnTimer should execute", 1, executionCount);
		
		ctrl.onUpdate(1.0f, actor);
		assertEquals("OnTimer should not execute anymore", 1, executionCount);
	}
	
	@Test
	public void shouldExecutyMultiplyTimesWhenRepeatIsSetToTrueTest()
	{
		ControllerAnnotations.setControllerParameter(ctrl, "interval", 1.0f);
		ControllerAnnotations.setControllerParameter(ctrl, "repeat", true);
		for(int i = 1; i <= 10; i++) 
		{
			ctrl.onUpdate(1.0f, actor);
			assertEquals("OnTimer was not executed at " + i + "nth time", i, executionCount);
		}
	}
	
	
}
