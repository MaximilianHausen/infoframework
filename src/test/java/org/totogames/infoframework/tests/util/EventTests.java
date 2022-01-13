package org.totogames.infoframework.tests.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.totogames.infoframework.tests.CamelCaseGenerator;
import org.totogames.infoframework.util.Action;
import org.totogames.infoframework.util.Event;
import org.totogames.infoframework.util.logging.LogSeverity;
import org.totogames.infoframework.util.logging.Logger;

@DisplayNameGeneration(CamelCaseGenerator.class)
public class EventTests {
    private int counter = 0;

    @Test
    public void logs() {
        Event event = new Event("TestEvent", true);
        Logger.setLogLevel(LogSeverity.Debug);
        Logger.setLogTarget(s -> counter++);
        event.run();
        Assertions.assertEquals(2, counter);
    }

    @Test
    public void subscribeAndRun() {
        Event event = new Event();
        event.subscribe(() -> counter++);
        event.run();
        Assertions.assertEquals(1, counter);
    }

    @Test
    public void unsubscribe() {
        Event event = new Event();
        Action action = () -> counter++;
        event.subscribe(action);
        event.unsubscribe(action);
        event.run();
        Assertions.assertEquals(0, counter);
    }
}
