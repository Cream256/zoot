package com.zootcat.controllers.gfx;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.zootcat.scene.ZootActor;

public class RenderControllerAdapterTest
{
	private RenderControllerAdapter ctrl;
	
	@Before
	public void setup()
	{
		ctrl = new RenderControllerAdapter();
	}
	
	@Test
	public void shouldDoNothingOnRender()
	{
		//given
		Batch batch = mock(Batch.class);
		ZootActor actor = mock(ZootActor.class);
		
		//when
		ctrl.onRender(batch, 1.0f, actor, 1.0f);
		
		//then
		verifyZeroInteractions(batch, actor);
	}
	
	@Test
	public void shouldGetZeroOffsetAfterCreation()
	{
		assertEquals(0.0f, ctrl.getOffsetX(), 0.0f);
		assertEquals(0.0f, ctrl.getOffsetY(), 0.0f);
	}
	
	@Test
	public void shouldSetOffset()
	{
		ctrl.setOffset(1.0f, -2.0f);
		assertEquals(1.0f, ctrl.getOffsetX(), 0.0f);
		assertEquals(-2.0f, ctrl.getOffsetY(), 0.0f);
	}
}
