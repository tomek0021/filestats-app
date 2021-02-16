package com.hicx.filestats;

import org.awaitility.core.ThrowingRunnable;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public class WaitUtils {

    public static void wait10Seconds(ThrowingRunnable callback) {
        await()
                .atMost(Duration.ofSeconds(10))
                .with()
                .pollInterval(Duration.ofMillis(50))
                .untilAsserted(() -> callback.run());
    }
}
