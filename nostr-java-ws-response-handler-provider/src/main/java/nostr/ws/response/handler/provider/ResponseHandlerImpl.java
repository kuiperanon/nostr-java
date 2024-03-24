/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package nostr.ws.response.handler.provider;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;
import nostr.base.Relay;
import nostr.base.annotation.DefaultHandler;
import nostr.event.BaseEvent;
import nostr.event.BaseMessage;
import nostr.event.json.codec.BaseEventEncoder;
import nostr.event.json.codec.BaseMessageDecoder;
import nostr.event.message.ClosedMessage;
import nostr.event.message.EoseMessage;
import nostr.event.message.EventMessage;
import nostr.event.message.NoticeMessage;
import nostr.event.message.OkMessage;
import nostr.event.message.RelayAuthenticationMessage;
import nostr.util.NostrException;
import nostr.ws.handler.command.spi.ICommandHandler;
import nostr.ws.handler.command.spi.ICommandHandler.Reason;
import nostr.ws.handler.spi.IResponseHandler;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.logging.Level;

/**
 *
 * @author eric
 */
@Data
@DefaultHandler
@Log
public class ResponseHandlerImpl implements IResponseHandler {

    private ICommandHandler commandHandler;
    @NonNull
    private List<BaseMessage> responses;

    public ResponseHandlerImpl(List<BaseMessage> responses) {
    	setResponses(responses);
    	init();
    }
    
    private void init() {
        try {
            this.commandHandler = ServiceLoader
                    .load(ICommandHandler.class)
                    .stream()
                    .map(p -> p.get())
                    .filter(ch -> !ch.getClass().isAnnotationPresent(DefaultHandler.class))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException ex) {
            log.log(Level.WARNING, "No custom command handler provided. Using default command handler instead...");
            try {
                this.commandHandler = ServiceLoader
                        .load(ICommandHandler.class)
                        .stream()
                        .map(p -> p.get())
                        .filter(ch -> ch.getClass().isAnnotationPresent(DefaultHandler.class))
                        .findFirst()
                        .get();
            } catch (NoSuchElementException e) {
                throw new AssertionError("Could not load the default handler", e);
            }
        }
    }

    @Override
    public void process(String message, Relay relay) {
        log.log(Level.FINE, "Processing message: {0} from relay: {1}", new Object[]{message, relay});

        var oMsg = new BaseMessageDecoder(message).decode();
        getResponses().add(oMsg);
        final String command = oMsg.getCommand();

        switch (command) {
            case "EOSE" -> {
                if (oMsg instanceof EoseMessage msg) {
                    commandHandler.onEose(msg.getSubscriptionId(), relay);
                } else {
                    throw new AssertionError("EOSE");
                }
            }
            case "OK" -> {
                if (oMsg instanceof OkMessage msg) {
                    String eventId = msg.getEventId();
                    boolean result = msg.getFlag();
                    String strMsg = msg.getMessage();
                    Reason reason = getReason(strMsg);
                    String okMessage = getMessage(strMsg);

                    commandHandler.onOk(eventId, okMessage, reason, result, relay);
                } else {
                    throw new AssertionError("OK");
                }
            }
            case "NOTICE" -> {
                if (oMsg instanceof NoticeMessage msg) {
                    commandHandler.onNotice(msg.getMessage());
                } else {
                    throw new AssertionError("NOTICE");
                }
            }
            case "EVENT" -> {
                if (oMsg instanceof EventMessage msg) {
                    var subId = msg.getSubscriptionId();
                    var jsonEvent = new BaseEventEncoder((BaseEvent) msg.getEvent()).encode();
                    commandHandler.onEvent(jsonEvent, subId, relay);
                } else {
                    throw new AssertionError("EVENT");
                }
            }

            case "AUTH" -> {
                if (oMsg instanceof RelayAuthenticationMessage msg) {
                    var challenge = msg.getChallenge();
                    commandHandler.onAuth(challenge, relay);
                } else {
                    throw new AssertionError("AUTH");
                }
            }

            case "CLOSED" -> {
                if (oMsg instanceof ClosedMessage msg) {
                    String strMsg = msg.getMessage();
                    Reason reason = getReason(strMsg);
                    String closedMessage = getMessage(strMsg);
                    commandHandler.onClosed(msg.getSubscriptionId(), reason, closedMessage, relay);
                } else {
                    throw new AssertionError("CLOSED");
                }
            }
            default -> throw new AssertionError("Unknown command " + command);
        }
    }

    private Reason getReason(String input) {
        final var msgSplit = input.split(":", 2);
        if (msgSplit.length < 2) {
            return Reason.UNDEFINED;
        } else {
            return Reason.fromCode(msgSplit[0]).orElseThrow(RuntimeException::new);
        }
    }

    private String getMessage(@NonNull String input) {
        final var msgSplit = input.split(":", 2);
        if (msgSplit.length < 2) {
            return input;
        } else {
            return msgSplit[1];
        }
    }
}
