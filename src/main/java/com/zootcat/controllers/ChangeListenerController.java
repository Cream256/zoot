package com.zootcat.controllers;

import com.zootcat.scene.ZootActor;

public interface ChangeListenerController extends Controller 
{
	void onSizeChange(ZootActor actor);
	void onPositionChange(ZootActor actor);
	void onRotationChange(ZootActor actor);
}
