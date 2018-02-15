package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Filter;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

public class CollisionFilterControllerTest
{
	private static final String CATEGORY = "category";
	private static final String MASK = "mask";
	private static final int DEFAULT_GROUP_MASK = 0;
	
	private CollisionFilterController ctrl;
	
	@Before
	public void setup()
	{
		ctrl = new CollisionFilterController();
		ControllerAnnotations.setControllerParameter(ctrl, "category", CATEGORY);
		ControllerAnnotations.setControllerParameter(ctrl, "mask", MASK);
		BitMaskConverter.Instance.clear();
	}
			
	@Test
	public void shouldReturnCollisionFilter()
	{
		//when
		ctrl.init(mock(ZootActor.class));
		
		//then
		Filter filter = ctrl.getCollisionFilter();
		assertNotNull(filter);
		assertEquals("Category does not match", BitMaskConverter.Instance.fromString(CATEGORY), filter.categoryBits);
		assertEquals("Mask does not match", BitMaskConverter.Instance.fromString(MASK), filter.maskBits);
		assertEquals("Group index does not match", DEFAULT_GROUP_MASK, filter.groupIndex);
	}
	
	@Test
	public void shouldSetFilterOnAllFixtures()
	{
		//given		
		ZootActor actor = new ZootActor();
		PhysicsBodyController physCtrl = mock(PhysicsBodyController.class);
		actor.addController(physCtrl);
		
		//when
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		//then
		verify(physCtrl, times(1)).setCollisionFilter(ctrl.getCollisionFilter());
	}
}
