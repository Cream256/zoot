package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class HurtOnCollideFromAboveOrBelowController extends OnCollideFromAboveOrBelowController
{
	@CtrlParam private int damage = 1;
	@CtrlParam private boolean above = true;
	@CtrlParam private boolean hurtOwner = false;
		
	private HurtOnCollideController hurtOnCollideCtrl;
	
	@Override
	public void init(ZootActor actor)
	{		
		super.init(actor);
		
		hurtOnCollideCtrl = new HurtOnCollideController();
		hurtOnCollideCtrl.init(actor);
	}
	
	@Override
	public void onAdd(ZootActor actor)
	{
		hurtOnCollideCtrl.setHurtOwner(hurtOwner);
		hurtOnCollideCtrl.setDamage(damage);		
			
		super.onAdd(actor);
	}
		
	@Override
	public void onCollidedFromAbove(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(!above) return;
		hurtOnCollideCtrl.onEnter(actorA, actorB, contact);	
	}
	
	@Override
	public void onCollidedFromBelow(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(above) return;
		hurtOnCollideCtrl.onEnter(actorA, actorB, contact);	
	}
	
	public void setDamage(int value)
	{
		damage = value;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public void setHurtOwner(boolean value)
	{
		hurtOwner = value;
	}
	
	public boolean getHurtOwner()
	{
		return hurtOwner;
	}

	public boolean collideFromAbove()
	{
		return above;
	}
	
	public void setCollideFromAbove(boolean value)
	{
		above = value;
	}
}
