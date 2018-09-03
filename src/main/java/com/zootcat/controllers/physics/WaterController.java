package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.leakedbits.codelabs.box2d.controllers.BuoyancyController;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class WaterController extends OnCollideController
{	
	@CtrlParam(global = true) private ZootScene scene;
    @CtrlParam(debug = true) public boolean isFluidFixed = true;
    @CtrlParam(debug = true) public float fluidDrag = 0.25f;
    @CtrlParam(debug = true) public float fluidLift = 0.25f;
    @CtrlParam(debug = true) public float linearDrag = 0;
    @CtrlParam(debug = true) public float maxFluidDrag = 2000;
    @CtrlParam(debug = true) public float maxFluidLift = 500;
	
	private BuoyancyController buoyancyCtrl;
		
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		
		Fixture actorFixture = actor.getController(PhysicsBodyController.class)
									.getFixtures()
									.stream()
									.findFirst()
									.orElseThrow(() -> new RuntimeZootException("Water Controller must have a fixture"));
		
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
