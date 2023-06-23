package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.socket_handlers.Event;
import be.howest.ti.mars.logic.socket_handlers.EventFactory;
import be.howest.ti.mars.logic.socket_handlers.ForumHandler;
import be.howest.ti.mars.logic.socket_handlers.OutgoingEvent;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
    import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The RTC bridge is one of the class taught topics.
 * If you do not choose the RTC topic you don't have to do anything with this class.
 * Otherwise, you will need to expand this bridge with the websockets topics shown in the other modules.
 *
 * The client-side starter project does not contain any teacher code about the RTC topic.
 * The rtc bridge is already initialized and configured in the WebServer.java.
 * No need to change the WebServer.java
 *
 * The job of the "bridge" is to bridge between websockets events and Java (the controller).
 * Just like in the openapi bridge, keep business logic isolated in the package logic.
 * <p>
 */
public class MarsRtcBridge {
    private static final String EB_EVENT_TO_MARTIANS = "events.to.martians";
    private SockJSHandler sockJSHandler;
    private EventBus eventBus;
    private static final String CHANNEL_TO_USER = "events.to.users.%s";
    private static final String CHANNEL_TO_SERVER = "events.to.server";

    /**
     * Example function to put a message on the event bus every 10 seconds.
     * The timer logic is only there to simulate a repetitive stream of data as an example.
     * Please remove this timer logic or move it to an appropriate place.
     * Please call the controller to get some business logic data.
     * Afterwords publish the result to the client.
     */
    public void sendEventToClients() {
            final Timer timer = new Timer();

            TimerTask task = new TimerTask() {
                public void run() {
                    eventBus.publish(EB_EVENT_TO_MARTIANS, new JsonObject(Map.of("MyJsonProp", "some value")));
                }
            };

            timer.schedule(task, 0, 30000);
    }

    private void createSockJSHandler() {
        final PermittedOptions permittedOptions = new PermittedOptions().setAddressRegex("events\\..+");
        final SockJSBridgeOptions options = new SockJSBridgeOptions()
                .addInboundPermitted(permittedOptions)
                .addOutboundPermitted(permittedOptions);
        sockJSHandler.bridge(options);
    }

    public SockJSHandler getSockJSHandler(Vertx vertx) {
        sockJSHandler = SockJSHandler.create(vertx);
        eventBus = vertx.eventBus();
        createSockJSHandler();
        createServerConsumer();

        // This is for demo purposes only.
        // Do not send messages in this getSockJSHandler function.
        sendEventToClients();

        return sockJSHandler;
    }

    private void createServerConsumer() {
        eventBus.consumer(CHANNEL_TO_SERVER, this::handleMsg);
    }

    private void handleMsg(Message<JsonObject> msg) {
        Event event = EventFactory.getInstance().createEvent(msg.body());
        OutgoingEvent outGoingEvent = null;
        try {
            outGoingEvent = ForumHandler.getInstance().handleIncomingEvent(event);
        } catch (Exception ex) {
            return;
        }
        eventBus.publish(String.format(CHANNEL_TO_USER, outGoingEvent.getReceiverId()), outGoingEvent.getBody());
    }
}
