package com.zootcat.testing;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.controllers.ai.DefaultStateMachineController;
import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.logic.LifeController;
import com.zootcat.controllers.physics.MoveableController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class ZootStateTestCase
{
	protected ZootActor actor;	
	@Mock protected PhysicsBodyController physicsBodyCtrlMock;
	@Mock protected AnimatedSpriteController animatedSpriteCtrlMock;
	@Mock protected DirectionController directionCtrlMock;
	@Mock protected MoveableController moveableCtrlMock;
	@Mock protected LifeController lifeCtrlMock;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		actor = new ZootActor();
		actor.addController(animatedSpriteCtrlMock);
		actor.addController(physicsBodyCtrlMock);
		actor.addController(directionCtrlMock);
		actor.addController(moveableCtrlMock);
		actor.addController(lifeCtrlMock);
		
		DefaultStateMachineController smCtrl = new DefaultStateMachineController();
		smCtrl.init(actor);
		actor.addController(smCtrl);
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
