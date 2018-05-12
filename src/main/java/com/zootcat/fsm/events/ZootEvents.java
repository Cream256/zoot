package com.zootcat.fsm.events;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.zootcat.scene.ZootActor;

public class ZootEvents
{
	private static final Pool<ZootEvent> pool = Pools.get(ZootEvent.class);
	
	public static ZootEvent get(ZootEventTypeEnum type)
	{
		return get(type, null);
	}
	
	public static ZootEvent get(ZootEventTypeEnum type, Object userObj)
	{
		ZootEvent event = pool.obtain();
		event.setType(type);
		event.setUserObject(userObj);		
		return event;
	}
	
	public static void fireAndFree(ZootActor actor, ZootEventTypeEnum type)
	{
		fireAndFree(actor, type, null);
	}
	
	public static void fireAndFree(ZootActor actor, ZootEventTypeEnum type, Object userObj)
	{
		ZootEvent event = get(type, userObj);
		actor.fire(event);
		free(event);
	}
	
	public static void free(ZootEvent event)
	{
		pool.free(event);
	}
}
