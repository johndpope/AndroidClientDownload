package net.ericsson.emovs.download;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.ebs.android.exposure.interfaces.IPlayable;
import com.ebs.android.utilities.ServiceUtils;

import net.ericsson.emovs.download.interfaces.IDownload;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 2017-10-05.
 */

public class EMPDownloadProvider {
    private static final String TAG = EMPDownloadProvider.class.toString();

    Context app;
    Intent downloadServiceIntent;

    private static class EMPDownloadProviderHolder {
        private final static EMPDownloadProvider sInstance = new EMPDownloadProvider();
    }

    public static EMPDownloadProvider getInstance(Context app) {
        EMPDownloadProviderHolder.sInstance.bind(app);
        return EMPDownloadProviderHolder.sInstance;
    }

    protected EMPDownloadProvider() {
    }

    public void bind(Context app) {
        try {
            if (this.app == app) {
                return;
            }
            DownloadItemManager.bind(app);
            this.app = app;
            startService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(IPlayable playable) throws Exception {
        boolean ret = DownloadItemManager.getInstance().createItem(playable);
        if (ret) {
            startService();
        }
    }

    public void retry(IPlayable playable) {
        DownloadItemManager.getInstance().retry(playable);
    }

    public void delete(IPlayable playable) {
        // TODO: delete asset (downloaded, queued, downloading, failed)
    }

    public void pause(IPlayable playable) {
        DownloadItemManager.getInstance().pause(playable);
    }

    public void resume(IPlayable playable) {
        DownloadItemManager.getInstance().resume(playable);
    }

    public ArrayList<IDownload> getDownloads() {
        return DownloadItemManager.getInstance().getDownloads();
    }

    public void syncWithStorage() {
        DownloadItemManager.getInstance().syncWithStorage();
    }

    public void startService() throws Exception {
        if (this.app == null) {
            throw  new Exception("APP_NOT_BOUND_TO_DOWNLOADER_PROVIDER");
        }
        if (isDownloadServiceRunning() == false) {
            this.downloadServiceIntent = new Intent(app, EMPDownloadService.class);
            this.downloadServiceIntent.setAction(EMPDownloadService.class.getName());
            app.startService(downloadServiceIntent);
        }
    }

    public void stopService() {
        if (isDownloadServiceRunning() == true) {
            app.stopService(this.downloadServiceIntent);
        }
    }

    public boolean isDownloadServiceRunning() {
        if (this.app == null) {
            return false;
        }
        return ServiceUtils.isServiceRunning(this.app, EMPDownloadService.class);
    }

}