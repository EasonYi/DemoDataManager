package com.emotionsense.demo.data.loggers;

import java.util.HashMap;

import android.content.Context;

import com.emotionsense.demo.data.DemoApplication;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.datahandler.loggertypes.AbstractImmediateTransferLogger;
import com.ubhave.sensormanager.ESException;

public class TransmitOnly extends AbstractImmediateTransferLogger
{
	private static TransmitOnly instance;

	public static AbstractDataLogger getInstance() throws ESException, DataHandlerException
	{
		if (instance == null)
		{
			instance = new TransmitOnly(DemoApplication.getContext());
		}
		return instance;
	}

	protected TransmitOnly(final Context context) throws DataHandlerException, ESException
	{
		super(context);
	}

	@Override
	protected String getPostKey()
	{
		// Note: the key that will be used when POST'ing your data
		return "data";
	}

	@Override
	protected String getDataPostURL()
	{
		// Note: add the URL (http://...) you are POST'ing data to.
		return RemoteServerDetails.JSON_POST_URL;
	}

	@Override
	protected String getSuccessfulPostResponse()
	{
		// Note: this is a string your server returns when data has been received
		return RemoteServerDetails.RESPONSE_ON_SUCCESS;
	}

	@Override
	protected HashMap<String, String> getPostParameters()
	{
		// Note: any additional parameters (e.g., API key-value) that your URL requires
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(RemoteServerDetails.API_KEY_KEY, RemoteServerDetails.API_KEY_VALUE);
		return params;
	}

	@Override
	protected String getUniqueUserId()
	{
		// Note: this should not be a static string
		return "test-user-id";
	}

	@Override
	protected String getDeviceId()
	{
		// Note: this should not be a static string
		return "test-device-id";
	}

	@Override
	protected boolean shouldPrintLogMessages()
	{
		// Note: return false to turn off Log.d messages
		return true;
	}
}
