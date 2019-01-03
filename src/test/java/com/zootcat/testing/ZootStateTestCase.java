package com.zootcat.testing;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.controllers.ai.DefaultStateMachineController;
import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.logic.LifeController;
import com.zootcat.controllers.physics.FlyableController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class ZootStateTestCase
{
	protected ZootActor actor;	
	@Mock protected PhysicsBodyController physicsBodyCtrlMock;
	@Mock protected AnimatedSpriteController animatedSpriteCtrlMock;
	@Mock protected DirectionController directionCtrlMock;
	@Mock protected WalkableController walkableCtrlMock;
	@Mock protected FlyableController flyableCtrlMock;
	@Mock protected LifeController lifeCtrlMock;
	
	@Before
	public void setup()
	{
		//init mocks
		MockitoAnnotations.initMocks(this);
		
		//setup actor
		actor = new ZootActor();
		actor.addController(animatedSpriteCtrlMock);
		actor.addController(physicsBodyCtrlMock);
		actor.addController(directionCtrlMock);
		actor.addController(walkableCtrlMock);
		actor.addController(flyableCtrlMock);
		actor.addController(lifeCtrlMock);
		
		//setup state machine
		DefaultStateMachineController smCtrl = new DefaultStateMachineController();
		smCtrl.init(actor);
		actor.addController(smCtrl);
				
		//state machine init can have side effects, reset them
		reset(animatedSpriteCtrlMock);
		reset(walkableCtrlMock);
		reset(flyableCtrlMock);
		
		//setup mocks, has to be done after reset
		when(walkableCtrlMock.canJump()).thenReturn(true);
		when(walkableCtrlMock.canRun()).thenReturn(true);
	}
	
	public ZootEvent createEvent(ZootEventType type, Object userObject)
	{
		ZootEvent event = new ZootEvent(type);
		event.setTarget(actor);
		event.setUserObject(userObject);
		return event;		
	}
	
	public ZootEvent createEvent(ZootEventType type)
	{
		return createEvent(type, null);
	}
}
