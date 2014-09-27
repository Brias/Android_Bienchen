package de.androidbienchen.listener;

public interface UpdateStatusListener {

	public void onUpdateFinished();
	public void onLocalDataError(String description);
}
