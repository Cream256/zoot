package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class SizeControllerTest
{
	private static final float WIDTH = 2.560f;
	private static final float HEIGHT = 1.280f;
	
	private ZootActor actor;
	private SizeController ctrl;
	private ZootScene scene;
		
	@Before
	public void setup()
	{
		actor = new ZootActor();
		ctrl = new SizeController();
		scene = mock(ZootScene.class);
		when(scene.getUnitScale()).thenReturn(1.0f);
		
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
		ControllerAnnotations.setControllerParameter(ctrl, "width", WIDTH);
		ControllerAnnotations.setControllerParameter(ctrl, "height", HEIGHT);
	}
	
	@Test
	public void initShouldSetActorSizeTest()
	{
		//when
		ctrl.init(actor);
		
		//then
		assertEquals("Wrong width", WIDTH, actor.getWidth(), 0.0f);
		assertEquals("Wrong height", HEIGHT, actor.getHeight(), 0.0f);
	}
	
	@Test
	public void initShouldSetActorSizeScaledBySceneScaleTest()
	{
		//given
		final float scale = 0.5f;		
		when(scene.getUnitScale()).thenReturn(scale);
		
		//when
		ctrl.init(actor);
		
		//then
		assertEquals("Wrong width", WIDTH * scale, actor.getWidth(), 0.0f);
		assertEquals("Wrong height", HEIGHT * scale, actor.getHeight(), 0.0f);
	}
}
