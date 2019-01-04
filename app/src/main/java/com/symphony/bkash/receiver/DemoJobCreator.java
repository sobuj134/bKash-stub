package com.symphony.bkash.receiver;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by monir.sobuj on 12/3/2018.
 */

public class DemoJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag) {
            case UploaderJob.TAG:
                return new UploaderJob();
            default:
                return null;
        }
    }
}
