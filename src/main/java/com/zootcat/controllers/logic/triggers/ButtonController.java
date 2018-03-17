package com.zootcat.controllers.logic.triggers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.scene.ZootActor;

/**
 * Button Controller - used for actors that are buttons and can be stepped on. 
 * When button is stepped on it emits SwitchOn event. If there is no other
 * actors on button, it emits SwitchOff event.
 * @author Cream
 * @see TriggerController
 */
public class ButtonController extends TriggerController
{
	private int count;
	
	@Override
	public void init(ZootActor actor)
	{
		super.init(actor);
		count = 0;
	}
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(++count == 1)
		{
			setActive(true);
		}
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(--count == 0)
		{		
			setActive(false);
		}
	}
}
