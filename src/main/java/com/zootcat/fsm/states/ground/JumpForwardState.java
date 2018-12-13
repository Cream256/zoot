package com.zootcat.fsm.states.ground;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class JumpForwardState extends JumpState
{
	public static final String NAME = "JumpForward";
	public static final int ID = JumpForwardState.class.hashCode();
	 	
	private ZootDirection forwardJumpDirection = ZootDirection.None;
	
	public JumpForwardState()
	{
		super(NAME);
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{	
		//no super
		super.setAnimationBasedOnStateName(actor);
		actor.controllerAction(DirectionController.class, ctrl -> forwardJumpDirection = ctrl.getDirection());
		actor.controllerAction(WalkableController.class, ctrl -> ctrl.jumpForward(forwardJumpDirection, false));
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{	
		if(event.getType() == ZootEventType.Fall)
		{
			changeState(event, FallForwardState.ID);
			return true;
		}
		return super.handle(event);
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}
