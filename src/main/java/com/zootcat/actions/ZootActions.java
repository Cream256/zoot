package com.zootcat.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.zootcat.camera.ZootCamera;
import com.zootcat.controllers.Controller;
import com.zootcat.scene.ZootActor;

public class ZootActions
{
	private static final int DEFAULT_POOL_SIZE = 20;
	
	static public <T extends ZootAction> T zootAction(Class<T> type) {
		Pool<T> pool = Pools.get(type, DEFAULT_POOL_SIZE);
		T action = pool.obtain();
		action.setPool(pool);
		return action;
	}

	static public ZootKillActorAction killActor(ZootActor actor)
	{
		ZootKillActorAction killAction = zootAction(ZootKillActorAction.class);
		killAction.setTarget(actor);
		return killAction;
	}

	public static ZootMoveActorAction moveActor(ZootActor actor, float mx, float my)
	{
		ZootMoveActorAction moveAction = zootAction(ZootMoveActorAction.class);
		moveAction.setMovementX(mx);
		moveAction.setMovementY(my);
		moveAction.setTarget(actor);
		return moveAction;
	}
	
	public static ZootPositionActorAction positionActor(ZootActor actor, float x, float y)
	{
		ZootPositionActorAction positionAction = zootAction(ZootPositionActorAction.class);
		positionAction.setPosition(x, y);
		positionAction.setTarget(actor);
		return positionAction;
	}
	
	public static ZootAddControllerAction addController(ZootActor actor, Controller ctrl)
	{
		ZootAddControllerAction addCtrlAction = zootAction(ZootAddControllerAction.class);
		addCtrlAction.setController(ctrl);
		addCtrlAction.setTarget(actor);
		return addCtrlAction;	
	}
	
	public static ZootRemoveControllerAction removeController(ZootActor actor, Controller ctrl)
	{
		ZootRemoveControllerAction removeCtrlAction = zootAction(ZootRemoveControllerAction.class);
		removeCtrlAction.setController(ctrl);
		removeCtrlAction.setTarget(actor);
		return removeCtrlAction;
	}
	
	public static ZootParabolicMovementAction parabolicMovement(ZootActor actor, Vector2 peak, Vector2 pointOnParabole)
	{
		ZootParabolicMovementAction parabolicAction = zootAction(ZootParabolicMovementAction.class);
		parabolicAction.setParaboleParams(peak, pointOnParabole);
		parabolicAction.setTarget(actor);
		return parabolicAction;
	}

	public static ZootEnableInputProcessorControllerAction enableInputProcessorController(ZootActor target, boolean enabled)
	{		
		ZootEnableInputProcessorControllerAction enableInputProcessorAction = zootAction(ZootEnableInputProcessorControllerAction.class);
		enableInputProcessorAction.setTarget(target);
		enableInputProcessorAction.setControllerEnabled(enabled);
		return enableInputProcessorAction;
	}
	
	public static ZootCameraFocusAction cameraFocus(ZootCamera camera, ZootActor target)
	{
		ZootCameraFocusAction cameraFocusAction = new ZootCameraFocusAction();
		cameraFocusAction.setTarget(target);
		cameraFocusAction.setCamera(camera);
		return cameraFocusAction;		
	}
}
