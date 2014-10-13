package de.androidbienchen.activities;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import de.androidbienchen.R;
import de.androidbienchen.camhelper.ImageFetcher;
import de.androidbienchen.data.NetworkAvailability;
import de.androidbienchen.listener.ImageFetcherListener;
import de.androidbienchen.listener.UpdateStatusListener;

public class CamActivity extends Fragment implements ImageFetcherListener {

	private static final int UPDATE_TIMER_TIME = 1000 * 60 * 15;

	private ImageFetcher imageFetcher;
	private boolean firstFetch;
	private UpdateStatusListener listener;

	public CamActivity(UpdateStatusListener listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_cam, container,
				false);
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initFirstFetch();
		initFetcher();
		fetchingData();
		initTimer();
	}

	void initFirstFetch() {
		firstFetch = true;
	}

	// Sets up a runnable that is called every 15 minutes
	void initTimer() {
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				imageFetcher.startFetchingData();
				handler.postDelayed(this, UPDATE_TIMER_TIME);
			}
		};

		handler.postDelayed(runnable, UPDATE_TIMER_TIME);
	}

	void initFetcher() {
		imageFetcher = new ImageFetcher(getActivity(), this);
	}

	void fetchingData() {
		if (NetworkAvailability.networkStatus(getActivity())) {
			imageFetcher.startFetchingData();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		imageFetcher.stopFetchingData();
	}

	@Override
	public void onImageFetched(Bitmap bm) {
		setImageContent(bm);
		informListener();
	}

	void setImageContent(Bitmap bm) {
		if (bm != null) {
			try {
				ImageView v = (ImageView) getActivity().findViewById(
						R.id.cam_image);
				v.setImageBitmap(bm);
			} catch (Exception e) {
			}
		} else {
			listener.onLocalDataError("");
		}
	}

	// Informs the MainActivity of cam update finished
	void informListener() {
		if (firstFetch) {
			firstFetch = false;
			listener.onUpdateFinished();
		}
	}
}
