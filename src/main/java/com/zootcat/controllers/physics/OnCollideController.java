package com.zootcat.controllers.physics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.physics.ZootDefaultContactFilter;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

/**
 * OnCollide Controller - abstract class used to do some action when 
 * collision begins and ends.<br/> 
 * <br/>
 * OnEnter and OnLeave will be executed by default per fixture. It can be 
 * changed by setting the 'collidePerActor' parameter, then the collision
 * will count once per body, even if body has several fixtures.<br/>
 *  * 
 * @override onEnter - will be executed when collision begins<br/>
 * @override onLeave - will be executed when collision ends<br/>
 * 
 * @ctrlParam category - category name for collision detection,
 * if nothing is given a default value will be used
 * 
 * @ctrlParam mask - categories for which the collision will take place, separated with "|". 
 * If nothing is given, mask that collides with everything will be used.
 * 
 * @ctrlParam collideWithSensors - if sensors should count to collision, default true 
 * 
 * @ctrlParam collidePerActor - if collision per actor rather than fixture should be counted
 * 
 * @author Cream
 */
public abstract class OnCollideController extends PhysicsCollisionController
{
	@CtrlParam protected String category = null;
	@CtrlParam protected String mask = null;
	@CtrlParam protected boolean collideWithSensors = true;
	@CtrlParam protected boolean collidePerActor = false;
	
	protected Filter filter;
	protected Map<ZootActor, Integer> collidingActors = new HashMap<ZootActor, Integer>();
		
	public OnCollideController()
	{
		//noop
	}
		
	public void setCategoryParameter(String category)
	{
		this.category = category;
	}
	
	public String getCategoryParameter()
	{
		return category;
	}
	
	public void setMaskParameter(String mask)
	{
		this.mask = mask;
	}
	
	public String getMaskParameter()
	{
		return mask;
	}
	
	public void setFilter(Filter filter)
	{
		this.filter = filter;
	}
	
	public Filter getFilter()
	{
		return filter;
	}
	
	public void setCollideWithSensors(boolean value)
	{
		collideWithSensors = value;
	}
	
	public boolean getCollideWithSensors()
	{
		return collideWithSensors;
	}
	
	public void setCollidePerActor(boolean value)
	{
		collidePerActor = value;
	}
	
	public boolean getCollidePerActor()
	{
		return collidePerActor;
	}
	
	@Override
	public void init(ZootActor actor)
	{		
		super.init(actor);
		
		collidingActors.clear();		
		createCollisionFilter(actor);
	}

	private void createCollisionFilter(ZootActor actor)
	{
		filter = new Filter();
		
		if(mask != null || category != null)
		{
			filter.maskBits = BitMaskConverter.Instance.fromString(mask);
			if(category != null && !category.isEmpty())
			{
				filter.categoryBits = BitMaskConverter.Instance.fromString(category);
			}
		}
	}
		
	@Override
	public void onBeginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{						
		ZootActor otherActor = getOtherActor(actorA, actorB);
		if(collides(actorA, actorB, contact))
		{
			int collisionCount = collidingActors.getOrDefault(otherActor, 0) + 1;
			collidingActors.put(otherActor, collisionCount);
						
			if(collidePerActor && collisionCount > 1) return;
			
			onEnter(actorA, actorB, contact);
		}
	}
		
	@Override
	public void onEndContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		ZootActor otherActor = getOtherActor(actorA, actorB);
		if(collides(actorA, actorB, contact))
		{			
			int collisionCount = Math.max(0, collidingActors.getOrDefault(otherActor, 0) - 1);
			collidingActors.put(otherActor, collisionCount);
						
			if(collidePerActor && collisionCount != 0) return;
						
			onLeave(actorA, actorB, contact);
		}
	}
	
	protected boolean collides(ZootActor actorA, ZootActor actorB, Contact contact)
	{				
		Fixture otherFixture = getOtherFixture(actorA, actorB, contact);
		
		boolean collisionDetected = ZootDefaultContactFilter.shouldCollide(filter, otherFixture.getFilterData());
		boolean sensorOk = collideWithSensors || !otherFixture.isSensor();
		return collisionDetected && sensorOk;
	}
		
	@Override
	public void onPreSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{
		//noop
	}
	
	@Override
	public void onPostSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
	{
		//noop
	}
				
	public abstract void onEnter(ZootActor actorA, ZootActor actorB, Contact contact);
	
	public abstract void onLeave(ZootActor actorA, ZootActor actorB, Contact contact);
}