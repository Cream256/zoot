package com.zootcat.actions;

import java.util.function.Consumer;
import java.util.function.Function;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.zootcat.camera.ZootCamera;
import com.zootcat.controllers.Controller;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.game.ZootGame;
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
	
	public static ZootEnableInputAction enableInput(ZootGame game, boolean enabled)
	{
		ZootEnableInputAction enableInputAction = zootAction(ZootEnableInputAction.class);
		enableInputAction.setGame(game);
		enableInputAction.setInputEnabled(enabled);		
		return enableInputAction;
	}
	
	public static ZootCameraFocusAction cameraFocus(ZootCamera camera, ZootActor target)
	{
		ZootCameraFocusAction cameraFocusAction = zootAction(ZootCameraFocusAction.class);
		cameraFocusAction.setTarget(target);
		cameraFocusAction.setCamera(camera);
		return cameraFocusAction;		
	}
	
	public static ZootFireEventAction fireEvent(ZootActor target, ZootEvent event)
	{
		ZootFireEventAction fireEventAction = zootAction(ZootFireEventAction.class);
		fireEventAction.setTarget(target);
		fireEventAction.setEvent(event);
		return fireEventAction;
	}
	
	public static ZootLambdaAction lambda(Function<Float, Boolean> lambda)
	{
		ZootLambdaAction lambdaAction = new ZootLambdaAction(lambda);
		return lambdaAction;
	}
	
	public static ZootLoadLevelAction loadLevel(ZootGame game, String levelPath)
	{
		ZootLoadLevelAction loadLevelAction = zootAction(ZootLoadLevelAction.class);
		loadLevelAction.setZootGame(game);
		loadLevelAction.setLevelPath(levelPath);
		return loadLevelAction;
	}
	
	public static ZootShowDialogScreenAction showDialog(String dialogPath, String dialogToken, ZootGame game, ZootActor target, Consumer<Game> onShowAction, Consumer<Game> onHideAction)
	{
		ZootShowDialogScreenAction showDialogAction = zootAction(ZootShowDialogScreenAction.class);
		showDialogAction.setTarget(target);
		showDialogAction.setDialogPath(dialogPath);
		showDialogAction.setDialogToken(dialogToken);
		showDialogAction.setZootGame(game);		
		showDialogAction.setOnShowAction(onShowAction);
		showDialogAction.setOnHideAction(onHideAction);
		return showDialogAction;
	}
	
	public static ZootKnockbackAction knockback(float knockbackX, float knockbackY, boolean varyHorizontal, ZootActor target, ZootActor owner)
	{
		ZootKnockbackAction knockbackAction = zootAction(ZootKnockbackAction.class);
		knockbackAction.setKnockback(knockbackX, knockbackY);
		knockbackAction.setVaryHorizontal(varyHorizontal);
		knockbackAction.setKnockbackActor(target);		
		knockbackAction.setAttackActor(owner);
		return knockbackAction;
	}
}
