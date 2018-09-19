package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

/**
 * Most typical collision controller for enemies. When player jumps on the enemy
 * from the top, the enemy will get damage. Otherwise, if player is not on top
 * of the enemy, he will be hurt instead.
 * 
 * Additionally, an Attack {@link ZootEvent} will be send to enemy if the 
 * player gets damage. By default this should trigger the attack state, which should
 * play attack animation. 
 * 
 * @author Cream
 *
 */
public class EnemyCollisionController extends OnCollideFromAboveOrBelowController
{
	@CtrlParam private int damageToEnemy = 1;
	@CtrlParam private int damageToPlayer = 1;
	
	private HurtOnCollideController hurtEnemyCtrl;
	private HurtOnCollideController hurtPlayerCtrl;
	
	@Override
	public void init(ZootActor actor)
	{
		super.init(actor);
		
		hurtEnemyCtrl = new HurtOnCollideController();
		hurtEnemyCtrl.setDamage(damageToEnemy);
		hurtEnemyCtrl.setHurtOwner(true);		
		hurtEnemyCtrl.init(actor);
		
		hurtPlayerCtrl = new HurtOnCollideController();
		hurtPlayerCtrl.setDamage(damageToPlayer);
		hurtPlayerCtrl.setHurtOwner(false);
		hurtPlayerCtrl.init(actor);
	}
		
	@Override
	public void onCollidedFromAbove(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		hurtEnemyCtrl.hurt(actorA, actorB);		
	}

	@Override
	public void onCollidedFromBelow(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		hurtPlayerCtrl.hurt(actorA, actorB);
		ZootEvents.fireAndFree(getControllerActor(), ZootEventType.Attack);
	}
	
	public int getDamageToEnemy()
	{
		return damageToEnemy;
	}
	
	public int getDamageToPlayer()
	{
		return damageToPlayer;
	}
}
