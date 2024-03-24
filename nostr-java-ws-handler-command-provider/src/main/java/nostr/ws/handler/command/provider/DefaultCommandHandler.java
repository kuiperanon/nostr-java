/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.ws.handler.command.provider;

import lombok.NonNull;
import lombok.extern.java.Log;
import nostr.base.Command;
import nostr.base.Relay;
import nostr.base.annotation.DefaultHandler;
import nostr.client.Client;
import nostr.id.Identity;
import nostr.ws.handler.command.spi.ICommandHandler;

import java.util.logging.Level;

/**
 * @author squirrel
 */
@Log
@DefaultHandler
public class DefaultCommandHandler implements ICommandHandler {

    @Override
    public void onEose(String subscriptionId, Relay relay) {
        log.log(Level.FINE, "Command: {0} - Subscription ID: {1} - Relay {3}", new Object[]{Command.EOSE, subscriptionId, relay});
    }

    @Override
    public void onOk(String eventId, String reasonMessage, Reason reason, boolean result, Relay relay) {
        log.log(Level.FINE, "Command: {0} - Event ID: {1} - Reason: {2} ({3}) - Result: {4} - Relay {5}", new Object[]{Command.OK, eventId, reason, reasonMessage, result, relay});
        if (reason == Reason.AUTH_REQUIRED) {
            log.log(Level.WARNING, "Authentication required on relay {0}", relay);
        }
    }

    @Override
    public void onNotice(String message) {
        log.log(Level.WARNING, "Command: {0} - Message: {1}", new Object[]{Command.NOTICE, message});
    }

    @Override
    public void onEvent(String jsonEvent, String subId, Relay relay) {
        log.log(Level.FINE, "Command: {0} - Event: {1} - Subscription ID: {2} - Relay {3}", new Object[]{Command.EVENT, jsonEvent, subId, relay});
    }

    @Override
    public void onAuth(@NonNull String challenge, @NonNull Relay relay) {
        log.log(Level.FINE, "Command: {0} - Relay {1}", new Object[]{Command.AUTH, relay});
        auth(challenge, relay);
    }

    @Override
    public void onClosed(@NonNull String subId, @NonNull Reason reason, @NonNull String message, @NonNull Relay relay) {
        log.log(Level.WARNING, "Command: {0} - Subscription ID: {1} - Reason: {2} - Message: {3} - Relay {4}", new Object[]{Command.CLOSED, subId, reason, message, relay});
    }

    public static void auth(@NonNull String challenge, @NonNull Relay relay) {
        log.log(Level.INFO, "Authenticating with challenge: {0} on relay {1}", new Object[]{"<hidden>", relay});
        var client = Client.getInstance();
        var identity = Identity.getInstance();

        client.auth(identity, challenge, relay);
    }
}
