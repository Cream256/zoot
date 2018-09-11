package com.zootcat.screen;

import java.util.LinkedList;
import java.util.function.Consumer;

import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.game.ZootGame;

public class ZootLoadingScreen extends ZootScreenAdapter
{
	private int allCount;
	private int finishedCount;
	private Consumer<AssetManager> task;
	
	private ZootGame game;
	private AssetManager assetManager;
	private LinkedList<Consumer<AssetManager>> loadTasks = new LinkedList<Consumer<AssetManager>>();
	
	private Consumer<ZootGame> onFinishLoading;
	private Consumer<Float> onRenderWhileLoading;
	private Consumer<Float> onRenderAfterLoading;
	
	public ZootLoadingScreen(ZootGame game)
	{
		this.game = game;
		this.assetManager = game.getAssetManager();
	}
	
	@Override
	public void show()
	{
		finishedCount = 0;
		allCount = loadTasks.size();		
		if(allCount > 0)
		{
			task = loadTasks.remove();
			task.accept(assetManager);
		}
	}

	@Override
	public void render(float delta)
	{				
		if(task == null && loadTasks.isEmpty())
		{
			doRenderAfterLoading(delta);
			return;
		}
		
		doRenderWhileLoading(delta);
		if(task != null)
		{
			boolean taskFinished = assetManager.update();
			if(taskFinished)
			{
				++finishedCount;
				if(loadTasks.isEmpty())
				{
					doFinishLoading();
					task = null;
					return;
				}
				task = loadTasks.remove();
				task.accept(assetManager);
			}
		}
	}
	
	public AssetManager getAssetManager()
	{
		return assetManager;
	}
	
	public void addTask(Consumer<AssetManager> loadTask)
	{
		loadTasks.add(loadTask);
	}
	
	public float getProgress()
	{		
		float progress = finishedCount / (float)allCount;		
		return Float.isNaN(progress) ? 0.0f : progress;
	}
		
	public void onFinishLoading(Consumer<ZootGame> consumer)
	{
		onFinishLoading = consumer;
	}
	
	public void onRenderAfterLoading(Consumer<Float> consumer)
	{
		onRenderAfterLoading = consumer;
	}
	
	public void onRenderWhileLoading(Consumer<Float> consumer)
	{
		onRenderWhileLoading = consumer;
	}
		
	private void doFinishLoading()
	{
		if(onFinishLoading != null)
		{
			onFinishLoading.accept(game);
		}
	}
	
	private void doRenderAfterLoading(float delta)
	{
		if(onRenderAfterLoading != null)
		{
			onRenderAfterLoading.accept(delta);
		}
	}
	
	private void doRenderWhileLoading(float delta)
	{
		if(onRenderWhileLoading != null)
		{
			onRenderWhileLoading.accept(delta);
		}
	}
}
