package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.external.codelabs.BuoyancyController;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class WaterController extends OnCollideController
{	
	@CtrlParam(global = true) private ZootScene scene;
    @CtrlParam public boolean isFluidFixed = true;
    @CtrlParam public float fluidDrag = 0.25f;
    @CtrlParam public float fluidLift = 0.25f;
    @CtrlParam public float linearDrag = 0;
    @CtrlParam public float maxFluidDrag = 5000;
    @CtrlParam public float maxFluidLift = 2000;
	
	private BuoyancyController buoyancyCtrl;
		
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		
		Fixture actorFixture = actor.getSingleController(PhysicsBodyController.class)
									.getFixtures()
									.first();
		
		buoyancyCtrl = new BuoyancyController(scene.getPhysics().getWorld(), actorFixture);
		buoyancyCtrl.isFluidFixed = isFluidFixed;
		buoyancyCtrl.fluidDrag = fluidDrag;
		buoyancyCtrl.fluidLift = fluidLift;
		buoyancyCtrl.linearDrag = linearDrag;
		buoyancyCtrl.maxFluidDrag = maxFluidDrag;
		buoyancyCtrl.maxFluidLift = maxFluidLift;
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		buoyancyCtrl.step();
	}
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		buoyancyCtrl.addBody(getOtherFixture(actorA, actorB, contact));		
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		buoyancyCtrl.removeBody(getOtherFixture(actorA, actorB, contact));
	}
}
