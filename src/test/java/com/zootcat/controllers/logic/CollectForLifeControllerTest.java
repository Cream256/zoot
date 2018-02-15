package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;

public class CollectForLifeControllerTest
{
	private ZootActor collector;
	private ZootActor collectible;
	private CollectForLifeController ctrl;
	
	@Before
	public void setup()
	{
		collector = new ZootActor();
		collectible = new ZootActor();
		ctrl = new CollectForLifeController();
	}
	
	@Test
	public void onCollectActorWithNoLifeCtrlShouldNotThrowTest()
	{					
		ctrl.onCollect(collectible, collector);	
		//ok
	}
	
	@Test
	public void onCollectShouldAddMaxLifeTest()
	{
		//given
		IntValueController lifeCtrl = new LifeController();
		lifeCtrl.init(collector);
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "life", 0);
		ControllerAnnotations.setControllerParameter(ctrl, "maxLife", 1);		
		collector.addController(lifeCtrl);
		ctrl.onCollect(collectible, collector);
		
		//then
		assertEquals(LifeController.DEFAULT_LIFE, lifeCtrl.getValue());
		assertEquals(LifeController.DEFAULT_LIFE + 1, lifeCtrl.getMaxValue());
	}
	
	@Test
	public void onCollectShouldAddLifeTest()
	{
		//given
		final int life = 1;
		final int maxLife = 100;
		final int lifeToAdd = 10;
		
		IntValueController lifeCtrl = new LifeController();
		lifeCtrl.setMaxValue(maxLife);
		lifeCtrl.setValue(life);
						
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "life", lifeToAdd);
		ControllerAnnotations.setControllerParameter(ctrl, "maxLife", 0);
		collector.addController(lifeCtrl);
		ctrl.onCollect(collectible, collector);
		
		//then
		assertEquals(life + lifeToAdd, lifeCtrl.getValue());
		assertEquals(maxLife, lifeCtrl.getMaxValue());
	}
	
	@Test
	public void onCollectShouldAddLifeAndMaxLifeTest()
	{
		//given
		final int life = 5;
		final int maxLife = 5;
		final int lifeToAdd = 10;
		final int maxLifeToAdd = 10;
		
		IntValueController lifeCtrl = new LifeController();
		lifeCtrl.setMaxValue(maxLife);
		lifeCtrl.setValue(life);
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "life", lifeToAdd);
		ControllerAnnotations.setControllerParameter(ctrl, "maxLife", maxLifeToAdd);
		collector.addController(lifeCtrl);
		ctrl.onCollect(collectible, collector);
		
		//then
		assertEquals(life + lifeToAdd, lifeCtrl.getValue());
		assertEquals(maxLife + maxLifeToAdd, lifeCtrl.getMaxValue());		
	}
}
