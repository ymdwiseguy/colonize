package com.ymdwiseguy.col.crosscutting.logging;

import com.codahale.metrics.Timer;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.function.Supplier;

import static org.slf4j.LoggerFactory.getLogger;

public class LogUtil {
    private static final Logger LOG = getLogger(LogUtil.class);

    /**
     * Wraps a timer around {@code toBeTimes} and logs the runtime with the MDC key 'runtime'
     *
     * @param timerName The name of the timer
     * @param toBeTimed The code block to be executed and timed.
     * @param <T> The return type of the code block
     *
     * @return The result of the given code block {@code toBeTimed}
     */
    public static <T> T timed(final String timerName, final Supplier<T> toBeTimed) {
        Timer.Context c = new Timer().time();
        T result = toBeTimed.get();
        long runtime = c.stop();

        MDC.put("runtime", Long.toString(runtime / 1000));
        LOG.info(timerName);
        MDC.remove("runtime");

        return result;
    }

    public static void timed(final String timerName, final Runnable toBeTimed) {
        timed(timerName, () -> {
            toBeTimed.run();
            return "";
        });
    }

    /**
     * Extract the exception class name and put it next to the {@code logStatement}
     * into the MDC variable 'exceptionType'.
     *
     * @param e The exception
     * @param logStatement The log statement. e.g.:
     *  <code>
     *   () -> LOGGER.error("There was an exception", myException)
     *  </code>
     */
    public static void error(final Throwable e, final Runnable logStatement) {
        MDC.put("exceptionType", e.getClass().getSimpleName());
        logStatement.run();
        MDC.remove("exceptionType");
    }
}
