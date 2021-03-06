package com.emotionsense.demo.data;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.emotionsense.demo.data.loggers.StoreOnlyUnencryptedFiles;
import com.ubhave.datahandler.ESDataManager;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class MainActivity extends Activity
{
	private final static String LOG_TAG = "MainActivity";

	private AbstractDataLogger logger;
	private ESSensorManager sensorManager;
	private SubscribeThread[] subscribeThreads;
	private SenseOnceThread[] pullThreads;

	private final int[] pushSensors = {};
	private final int[] pullSensors = {};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try
		{
			// TODO: change this line of code to change the type of data logger
			logger = StoreOnlyUnencryptedFiles.getInstance();
			sensorManager = ESSensorManager.getSensorManager(this);

			// Use this thread to collect a single sample of pull sensor data
			pullThreads = new SenseOnceThread[pullSensors.length];
			for (int i = 0; i < pullSensors.length; i++)
			{
				pullThreads[i] = new SenseOnceThread(this, sensorManager, logger, pullSensors[i]);
				pullThreads[i].start();
			}

			subscribeThreads = new SubscribeThread[pushSensors.length];
			for (int i = 0; i < pushSensors.length; i++)
			{
				subscribeThreads[i] = new SubscribeThread(this, sensorManager, logger, pushSensors[i]);
				subscribeThreads[i].start();
			}
		}
		catch (Exception e)
		{
			Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			Log.d(LOG_TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void onSearchClicked(final View view)
	{
		try
		{
			long startTime = System.currentTimeMillis() - (1000L * 10);
			ESDataManager dataManager = logger.getDataManager();
			List<SensorData> recentData = dataManager.getRecentSensorData(SensorUtils.SENSOR_TYPE_PROXIMITY, startTime);
			Toast.makeText(this, "Recent events: " + recentData.size(), Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Error retrieving sensor data", Toast.LENGTH_LONG).show();
			Log.d(LOG_TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void onFlushClicked(final View view)
	{
		try
		{
			ESDataManager dataManager = logger.getDataManager();
			dataManager.postAllStoredData();
			Toast.makeText(this, "Data transferred.", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Error transferring data", Toast.LENGTH_LONG).show();
			Log.d(LOG_TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}
