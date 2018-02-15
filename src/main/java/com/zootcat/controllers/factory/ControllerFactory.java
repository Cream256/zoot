package com.zootcat.controllers.factory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.Controller;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.utils.ArgumentParser;
import com.zootcat.utils.ClassFinder;
import com.zootcat.utils.ZootUtils;

public class ControllerFactory
{
	private static final String CONTROLLER_SUFFIX = "controller";
	
	Map<String, Class<? extends Controller>> classes = new HashMap<String, Class<? extends Controller>>();
    Map<String, Object> globalParameters = new HashMap<String, Object>();
    
    public ControllerFactory()
    {
    	this(true);
    }
    
    public ControllerFactory(boolean addDefaultControllers)
    {
    	if(addDefaultControllers)
    	{
    		addFromPackage("com.zootcat.controllers", true);
    	}
    }
    
    public void add(Class<? extends Controller> clazz)
    {
        if(!ClassReflection.isInterface(clazz) && !ClassReflection.isAbstract(clazz))
        {
        	classes.put(normalizeName(clazz.getSimpleName()), clazz);
        }
    }
        
    public Class<? extends Controller> get(String ctrlName)
    {
        return classes.get(normalizeName(ctrlName));
    }

	public boolean contains(Class<? extends Controller> clazz)
	{
		return classes.containsKey(clazz.getSimpleName().toLowerCase());
	}
    
	public boolean contains(String ctrlName)
	{
		return classes.containsKey(normalizeName(ctrlName));
	}
    
	@SuppressWarnings("unchecked")
	public void addFromPackage(String packageName, boolean includeSubDirs)
	{
		List<Class<?>> found = ClassFinder.find(packageName, includeSubDirs);		
		Predicate<Class<?>> filterAssignable = cls -> ClassReflection.isAssignableFrom(Controller.class, cls) && !ClassReflection.isInterface(cls);				
		List<Class<? extends Controller>> filtered = found.stream()
									   .filter(filterAssignable)
									   .map(clazz -> (Class<? extends Controller>)clazz)
									   .collect(Collectors.toList());		
		filtered.forEach(cls -> add(cls));
	}
	
    public int getSize()
    {
        return classes.size();
    }
    
    public void addGlobalParameter(String name, Object param)
    {
        globalParameters.put(name, param);
    }
    
    public void removeGlobalParameter(String paramName)
    {
    	globalParameters.remove(paramName);
    }
    
    public Map<String, Object> getGlobalParameters()
    {
    	return globalParameters;
    }
            
	public Controller create(String ctrlName, String ctrlParams)
	{
		return create(ctrlName, ArgumentParser.parse(ctrlParams.split(",")));
	}
    
    public Controller create(String ctrlName, Map<String, Object> params)
    {
    	if(!contains(ctrlName))
        {
            throw new RuntimeZootException("No controller for given name: " + ctrlName + ".");
        }
    	
        Class<?> classToCreate = get(ctrlName);
        
        try 
        {
			Controller createdController = (Controller) classToCreate.newInstance();
			assignParamsToClassFields(createdController, params);
			return createdController;
		}
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) 
        {
        	throw new RuntimeZootException(e.getMessage(), e);
		}
    }
    
    private void assignParamsToClassFields(Controller controller, Map<String, Object> params) throws IllegalArgumentException, IllegalAccessException 
    {
    	List<Field> annotatedFields = ControllerAnnotations.getControllerParameterFields(controller);     	
    	for(Field field : annotatedFields)
    	{
			String fieldName = field.getName();
			CtrlParam ctrlParam = field.getAnnotation(CtrlParam.class);								
			Object param = ctrlParam.global() ? globalParameters.get(fieldName) : params.get(fieldName);
			
			if(param == null)
			{
				if(ctrlParam.required() || ctrlParam.global())
				{				
					throw new IllegalArgumentException("Parameter " + field.getName() + " is required for " + controller.getClass().getSimpleName());
				}
				continue;
			}
			field.setAccessible(true);
							
			if(field.getType().isEnum())
			{
				@SuppressWarnings("unchecked")
				Enum<?> enumeration = ZootUtils.searchEnum((Class<? extends Enum<?>>)field.getType(), param.toString());					
				field.set(controller, enumeration);
			}
			else
			{				
				field.set(controller, param);
			}		
    	}
		
	}
    
	private String normalizeName(String name)
	{
		//to lowercase
		String lowerCase = name.toLowerCase();
		
		//remove number suffix
		String noNumber = lowerCase.split("_")[0];
		
		//controller suffix
		String withSuffix = noNumber.endsWith(CONTROLLER_SUFFIX) ? noNumber : noNumber + CONTROLLER_SUFFIX; 
		
		//result
		return withSuffix;
	}
}
