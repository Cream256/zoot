package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Filter;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

/**
 * CollisionFilter controller - used to set collision filter on all fixtures.
 * @ctrlParam category - box2d filter category (e.g. "myCategory") 
 * @ctrlParam mask - box2d filter mask (e.g. "myCategory | otherCategory")
 * @author Cream
 *
 */
public class CollisionFilterController extends ControllerAdapter
{
	@CtrlParam private String category;
	@CtrlParam private String mask;
	
	private Filter collisionFilter;
	
	@Override
	public void init(ZootActor actor)
	{
		collisionFilter = new Filter();
		collisionFilter.categoryBits = BitMaskConverter.Instance.fromString(category);
		collisionFilter.maskBits = BitMaskConverter.Instance.fromString(mask);
	}
	
	@Override
	public void onAdd(ZootActor actor)
	{		
		actor.controllersAction(PhysicsBodyController.class, ctrl -> ctrl.setCollisionFilter(collisionFilter));
	}
	
	@Override
	public ControllerPriority getPriority()
	{
		return ControllerPriority.Low;
	}
	
	public Filter getCollisionFilter()
	{
		return collisionFilter;
	}
	
	public String getCategory()
	{
		return category;
	}
	
	public String getMask()
	{
		return mask;
	}	
}