package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class StaticBodyControllerTest
{
	@Mock private ZootScene scene;
	@Mock private ZootPhysics physics;
	@Captor private ArgumentCaptor<List<FixtureDef>> fixtureDefCaptor;
	
	private StaticBodyController staticBodyCtrl;
	
	@BeforeClass
	public static void initialize()
	{
		Box2D.init();
	}
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(scene.getPhysics()).thenReturn(physics);
		when(physics.createBody(any())).thenReturn(mock(Body.class));
		
		staticBodyCtrl = new StaticBodyController();
		ControllerAnnotations.setControllerParameter(staticBodyCtrl, "scene", scene);
	}
	
	@Test
	public void shouldSetStaticBodyTypeForBodyDefinition()
	{
		//given
		ArgumentCaptor<BodyDef> captor = ArgumentCaptor.forClass(BodyDef.class);
		
		//when
		staticBodyCtrl.init(mock(ZootActor.class));
		
		//then
		verify(physics).createBody(captor.capture());		
		assertEquals(BodyType.StaticBody, captor.getValue().type);
	}
}
