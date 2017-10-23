package net.ericsson.emovs.download;

import com.ebs.android.exposure.interfaces.IPlayable;
import com.ebs.android.exposure.models.EmpOfflineAsset;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Joao Coelho on 2017-10-21.
 */

public class DownloadInfo implements Serializable {
    int state;
    double progress;
    String downloadPath;
    UUID uuid;
    IPlayable onlinePlayable;
    EmpOfflineAsset offlinePlayable;
}