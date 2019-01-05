package com.zootcat.testing;

import com.zootcat.controllers.recognizers.ZootMockitoControllerRecognizer;
import com.zootcat.scene.ZootActor;

/**
 * Stub ZootActor class used in unit tests. It sets a test-specific controller comparator,
 * so that controllers mocked by Mockito could be recognized.
 * @author Cream
 *
 */
public class ZootActorStub extends ZootActor
{
	public ZootActorStub()
	{
		setControllerRecognizer(ZootMockitoControllerRecognizer.Instance);
	}
}
