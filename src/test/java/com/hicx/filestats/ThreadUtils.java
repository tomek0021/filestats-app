package com.hicx.filestats;

public class ThreadUtils {

    public static Thread wrapInThread(ThrowingCallback callback) {
        Thread thread = new Thread(() ->
        {
            try {
                callback.execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        );
        return thread;
    }

    public interface ThrowingCallback {
        void execute() throws Exception;
    }
}
