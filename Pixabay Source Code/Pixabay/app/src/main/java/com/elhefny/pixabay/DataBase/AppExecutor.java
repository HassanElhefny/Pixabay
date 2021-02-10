package com.elhefny.pixabay.DataBase;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    private final Executor mDisk_IO;
    private final Executor mNetwork;
    private final Executor Main_Thread;

    private AppExecutor(Executor mDisk_IO, Executor mNetwork, Executor main_Thread) {
        this.mDisk_IO = mDisk_IO;
        this.mNetwork = mNetwork;
        Main_Thread = main_Thread;
    }

    public AppExecutor() {
        this(
                Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(3),
                new MainExecutor()
        );
    }

    public Executor getmDisk_IO() {
        return mDisk_IO;
    }

    public Executor getmNetwork() {
        return mNetwork;
    }

    public Executor getMain_Thread() {
        return Main_Thread;
    }

    private static class MainExecutor implements Executor {
        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
