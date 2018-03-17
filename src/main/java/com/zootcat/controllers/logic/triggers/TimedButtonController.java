package com.zootcat.controllers.logic.triggers;

import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

//TODO add test
public class TimedButtonController extends ButtonController
{
	@CtrlParam(debug = true, required = true) private float revertAfter;
	@CtrlDebug private float time = 0.0f;
	@CtrlDebug private boolean trackingTime = false; 
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		super.onUpdate(delta, actor);
		
		if(trackingTime)
		{
			time = Math.max(0.0f, time - delta);
			if(time == 0.0f)
			{
				trackingTime = false;
				super.unpress();
			}
		}
	}
	
	@Override
	protected void press()
	{
		time = 0.0f;
		super.press();
	}
	
	@Override
	protected void unpress()
	{
		time = revertAfter;
		trackingTime = true;
	}
}
