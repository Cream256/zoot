package com.zootcat.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.ChangeListenerController;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.ControllerComparator;
import com.zootcat.controllers.gfx.RenderController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.fsm.ZootStateMachine;

/**
 * ZootScene actor. Extends from LibGdx {@link Actor}. It exends 
 * the base actor class so that it could use {@link Controller}'s. 
 * @author Cream
 * @see ZootScene
 *
 */
public class ZootActor extends Actor
{
	public static final String DEFAULT_NAME = "Unnamed Actor";
	
	private List<Controller> controllers = new ArrayList<Controller>();
	private Set<String> types = new HashSet<String>();	
	private float opacity = 1.0f;
	private int id = 0;
	private int gid = -1;
	private ZootScene scene;
	private ZootStateMachine stateMachine = new ZootStateMachine();

	public ZootActor()
	{
		setName(DEFAULT_NAME);
		stateMachine.setOwner(this);
		addListener(stateMachine);
	}
	
	@Override
	public void act(float delta)
	{				
		controllers.forEach(ctrl -> ctrl.onUpdate(delta, this));
		stateMachine.update(delta);
		super.act(delta);
	}
	
	@Override
	public boolean remove() 
	{
		removeAllControllers();
		return super.remove();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		float delta = Gdx.graphics.getDeltaTime();
		controllers.stream().filter(ctrl -> ClassReflection.isInstance(RenderController.class, ctrl))
							.map(ctrl -> (RenderController)ctrl)
							.forEach(ctrl -> ctrl.onRender(batch, parentAlpha, this, delta));
	}
	
	@Override
	protected void positionChanged() 
	{
		controllers.stream().filter(ctrl -> ClassReflection.isInstance(ChangeListenerController.class, ctrl))
							.map(ctrl -> (ChangeListenerController)ctrl)
							.forEach(ctrl -> ctrl.onPositionChange(this));
	}

	@Override
	protected void sizeChanged() 
	{
		controllers.stream().filter(ctrl -> ClassReflection.isInstance(ChangeListenerController.class, ctrl))
							.map(ctrl -> (ChangeListenerController)ctrl)
							.forEach(ctrl -> ctrl.onSizeChange(this));
	}
	
	@Override
	protected void rotationChanged() 
	{
		controllers.stream().filter(ctrl -> ClassReflection.isInstance(ChangeListenerController.class, ctrl))
							.map(ctrl -> (ChangeListenerController)ctrl)
							.forEach(ctrl -> ctrl.onRotationChange(this));
	}

	@SuppressWarnings("unchecked")
	public <T extends Controller> void controllerAction(Class<T> clazz, Consumer<T> action)
	{		
		Controller ctrl = tryGetController(clazz);
		if(ctrl != null)
		{
			action.accept((T) ctrl);
		}
	}
	
	public <T extends Controller> void controllersOfTypeAction(Class<T> clazz, Consumer<T> action)
	{
		getControllersOfType(clazz).forEach(action);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Controller> boolean controllerCondition(Class<T> clazz, Function<T, Boolean> func)
	{		
		Controller ctrl = tryGetController(clazz);
		if(ctrl != null)
		{
			return func.apply((T) ctrl);
		}
		return false;
	}
	
	public void addControllers(Collection<Controller> newControllers)
	{
		//firstly all controllers must be added to actor
		newControllers.forEach((ctrl) -> controllers.add(ctrl));
		
		//secondly, they must be invoked in proper order
		newControllers.stream().sorted(ControllerComparator.Instance)
							   .forEach((ctrl) -> ctrl.onAdd(this));
	}
	
	public void addController(Controller controller)
	{
		controllers.add(controller);
		controller.onAdd(this);		
	}
	
	public void removeController(Controller controller)
	{
		controller.onRemove(this);
		controllers.removeAll(Arrays.asList(controller));
	}
	
	public void removeAllControllers()
	{
		controllers.stream().sorted(ControllerComparator.Instance.reversed())
							.forEach(ctrl -> ctrl.onRemove(this));
		controllers.clear();
	}
	
	public List<Controller> getAllControllers()
	{
		return new ArrayList<Controller>(controllers);
	}
	
	public <T extends Controller> T tryGetController(Class<T> controllerClass)
	{
		try
		{
			return (T) getController(controllerClass);
		}
		catch(RuntimeZootException e)
		{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Controller> T getController(Class<T> controllerClass)
	{
		Controller result = controllers.stream()
				  .filter(ctrl -> ClassReflection.isInstance(controllerClass, ctrl))
				  .reduce((u, v) -> { throw new RuntimeZootException("More than one controllers found for " + controllerClass);})
				  .orElseThrow(() -> new RuntimeZootException("Controller " + controllerClass + " not found for " + this));
		return (T)result;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Controller> List<T> getControllersOfType(Class<T> controllerClass)
	{
		List<Controller> result = controllers.stream()
						 .filter(ctrl -> ClassReflection.isInstance(controllerClass, ctrl))
						 .collect(Collectors.toList());		
		return (List<T>)result;
	}
	
	public float getOpacity() 
	{
		return opacity;
	}
	
	public void setOpacity(float value)
	{
		this.opacity = MathUtils.clamp(value, 0.0f, 1.0f);
	}
	    
    public void addType(String newType)
    {
        types.add(newType.toLowerCase().trim());
    }
    
    public void removeType(String typeToRemove)
    {
        types.remove(typeToRemove.toLowerCase());
    }
    
    public Set<String> getTypes()
    {
        return new HashSet<String>(types);
    }
    
    public boolean isType(String type)
    {
        return types.contains(type.toLowerCase());
    }	
    
    public int getId()
    {
    	return id;
    }
    
    public void setId(int id)
    {
    	this.id = id;
    }
    
	public void setGid(int gid)
	{
		this.gid = gid;
	}
	
	public int getGid()
	{
		return gid;
	}
    
	public ZootStateMachine getStateMachine()
	{
		return stateMachine;
	}
	
    @Override
    public String toString()
    {
    	return getName();
    }

	public void setScene(ZootScene scene)
	{
		this.scene = scene;
	}
	
	public ZootScene getScene()
	{
		return scene;
	}
}
