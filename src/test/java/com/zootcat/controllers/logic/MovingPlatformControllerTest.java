package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;
import com.zootcat.testing.ZootActorStub;

public class MovingPlatformControllerTest
{		
	private static final float SPEED = 2.5f;
	private static final float RANGE = 100.0f;
	
	private ZootActor ctrlActor;	
	private MovingPlatformController ctrl;	
	
	@Mock private Body actor1Body;
	@Mock private Body actor2Body;
	@Mock private ZootScene sceneMock;	
	@Mock private ZootActor actorOnPlatform1;
	@Mock private ZootActor actorOnPlatform2;	
	@Mock private PhysicsBodyController ctrlActorBodyMock;
	@Mock private PhysicsBodyController actorOnPlatform1BodyMock;
	@Mock private PhysicsBodyController actorOnPlatform2BodyMock;
	
	@Before
	public void setup()
	{
		//mocks
		MockitoAnnotations.initMocks(this);
		when(sceneMock.getUnitScale()).thenReturn(1.0f);
		when(actorOnPlatform1.getSingleController(PhysicsBodyController.class)).thenReturn(actorOnPlatform1BodyMock);
		when(actorOnPlatform2.getSingleController(PhysicsBodyController.class)).thenReturn(actorOnPlatform2BodyMock);
		when(actorOnPlatform1BodyMock.getVelocity()).thenReturn(new Vector2());
		when(actorOnPlatform2BodyMock.getVelocity()).thenReturn(new Vector2());
	
		//controller actor
		ctrlActor = new ZootActorStub();
		ctrlActor.addController(ctrlActorBodyMock);
		reset(ctrlActorBodyMock);
		when(ctrlActorBodyMock.getVelocity()).thenReturn(new Vector2());
		
		//controller
		ctrl = new MovingPlatformController();
		ControllerAnnotations.setControllerParameter(ctrl, "scene", sceneMock);
		ControllerAnnotations.setControllerParameter(ctrl, "speed", SPEED);
		ControllerAnnotations.setControllerParameter(ctrl, "range", RANGE);
		ctrl.init(ctrlActor);
	}
		
	@Test
	public void shouldEndPlatformMovementIfRangeIsExceededAndComebackIsSetToFalseTest()
	{
		//given
		ctrlActor.setPosition(RANGE + 1.0f, 0.0f);
		
		//when
		ctrl.setComeback(false);
		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then
		assertFalse(ctrl.isMoving());
	}
	
	@Test
	public void shouldInvertDirectionIfComebackIsSetToTrueAndRangeIsExceededTest()
	{
		//given
		ctrlActor.setPosition(RANGE + 1.0f, 0.0f);
		assertEquals(ZootDirection.Right, ctrl.getDirection());
		
		//when
		ctrl.setComeback(true);
		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then
		assertTrue(ctrl.isMoving());
		assertEquals(ZootDirection.Left, ctrl.getDirection());
	}

	@Test
	public void shouldZeroPlatformActorVelocityIfNotMoving()
	{
		ctrl.setMoving(false);
		ctrl.onUpdate(1.0f, ctrlActor);
		verify(ctrlActorBodyMock, times(1)).setVelocity(0.0f, 0.0f);
		verifyNoMoreInteractions(ctrlActorBodyMock);
	}
		
	@Test
	public void shouldSetVelocityToGoRightTest()
	{		
		ControllerAnnotations.setControllerParameter(ctrl, "direction", ZootDirection.Right);		
		ctrl.onUpdate(1.0f, ctrlActor);
		
		verify(ctrlActorBodyMock, times(1)).setVelocity(SPEED, 0.0f);
	}
	
	@Test
	public void shouldSetVelocityToGoLeftTest()
	{
		ControllerAnnotations.setControllerParameter(ctrl, "direction", ZootDirection.Left);
		ctrl.onUpdate(1.0f, ctrlActor);
		
		verify(ctrlActorBodyMock, times(1)).setVelocity(-SPEED, 0.0f);
	}
	
	@Test
	public void shouldSetVelocityToGoUpTest()
	{
		ControllerAnnotations.setControllerParameter(ctrl, "direction", ZootDirection.Up);
		ctrl.onUpdate(1.0f, ctrlActor);
		
		verify(ctrlActorBodyMock, times(1)).setVelocity(0.0f, SPEED);
	}
	
	@Test
	public void shouldSetVelocityToGoDownTest()
	{
		ControllerAnnotations.setControllerParameter(ctrl, "direction", ZootDirection.Down);
		ctrl.onUpdate(1.0f, ctrlActor);
		
		verify(ctrlActorBodyMock, times(1)).setVelocity(0.0f, -SPEED);
	}
	
	@Test
	public void shouldSetVelocityToUpLeftTest()
	{
		ControllerAnnotations.setControllerParameter(ctrl, "direction", ZootDirection.UpLeft);
		ctrl.onUpdate(1.0f, ctrlActor);
		
		verify(ctrlActorBodyMock, times(1)).setVelocity(-SPEED, SPEED);
	}
	
	@Test
	public void shouldSetVelocityToDownRightTest()
	{
		ControllerAnnotations.setControllerParameter(ctrl, "direction", ZootDirection.DownRight);
		ctrl.onUpdate(1.0f, ctrlActor);
		
		verify(ctrlActorBodyMock, times(1)).setVelocity(SPEED, -SPEED);
	}
	
	@Test
	public void shouldSetMoving()
	{
		ctrl.setMoving(false);
		assertFalse(ctrl.isMoving());
		
		ctrl.setMoving(true);
		assertTrue(ctrl.isMoving());
	}
	
	@Test
	public void setComebackTest()
	{
		ctrl.setComeback(false);
		assertFalse(ctrl.getComeback());
		
		ctrl.setComeback(true);
		assertTrue(ctrl.getComeback());
	}
}
