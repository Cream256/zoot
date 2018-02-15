package com.zootcat.screen;

import java.util.LinkedList;
import java.util.function.Consumer;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.game.ZootGame;

public class ZootLoadingScreen implements Screen
{
	private int allCount;
	private int finishedCount;
	private Consumer<AssetManager> task;
	
	private ZootGame game;
	private AssetManager assetManager;
	private LinkedList<Consumer<AssetManager>> loadTasks = new LinkedList<Consumer<AssetManager>>();
	
	//fields are public so could be used like events
	public Consumer<ZootGame> onFinishLoading;
	public Consumer<Float> onRenderWhileLoading;
	public Consumer<Float> onRenderAfterLoading;
	
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
	
	public void addTask(Consumer<Void> loadTask)
	{
		loadTasks.add((assetManager) -> loadTask.accept(null));
	}
	
	public float getProgress()
	{		
		float progress = finishedCount / (float)allCount;		
		return Float.isNaN(progress) ? 0.0f : progress;
	}
	
	@Override
	public void resize(int width, int height)
	{
		//noop		
	}

	@Override
	public void pause()
	{
		//noop		
	}

	@Override
	public void resume()
	{
		//noop		
	}

	@Override
	public void hide()
	{
		//noop
	}

	@Override
	public void dispose()
	{
		//noop
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
