package com.zootcat.controllers.logic;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.zootcat.actions.ZootActions;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.ZootUtils;

public class BlinkOnHurtController extends OnZootEventController
{
	@CtrlParam private float duration = 1.0f;
	@CtrlDebug private boolean blinking = false;
	
	@Override
	public boolean onZootEvent(ZootActor actor, ZootEvent event)
	{
		if(event.getType() != ZootEventType.Hurt)
		{
			return false;
		}
		
		if(!blinking)
		{
			blinking = true;
			actor.addAction(createBlinkSequence(actor));	
		}
			
		return true;
	}
	
	public boolean isBlinking()
	{
		return blinking;
	}
	
	private SequenceAction createBlinkSequence(ZootActor actor)
	{
		Action blinkAction = Actions.sequence(Actions.fadeOut(0.25f), Actions.fadeIn(0.25f));
		
		int repeatCount = Math.max(1, ZootUtils.trunc(duration / 0.5f));
		Action repeatedBlinkAction = Actions.repeat(repeatCount, blinkAction);
		
		return Actions.sequence(
				ZootActions.lambda(() -> setActorInvulnerable(actor, true)), 
				repeatedBlinkAction, 
				ZootActions.lambda(() -> setActorInvulnerable(actor, false)),
				ZootActions.lambda(() -> blinking = false));
	}
	
	private boolean setActorInvulnerable(ZootActor actor, boolean value)
	{
		actor.controllersAction(LifeController.class, ctrl -> ctrl.setFrozen(value));
		return true;
	}
}
