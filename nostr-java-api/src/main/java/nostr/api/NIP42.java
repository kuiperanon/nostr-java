/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.api;

import lombok.NonNull;
import nostr.api.factory.TagFactory;
import nostr.base.Relay;
import nostr.event.impl.GenericEvent;
import nostr.event.impl.GenericTag;
import nostr.event.tag.RelayTag;

/**
 *
 * @author eric
 */
public class NIP42<T extends GenericEvent> extends EventNostr<T> {

    /**
     *
     * @param relay
     */
    public static RelayTag createRelayTag(@NonNull Relay relay) {
        return new RelayTag(relay);
    }

    /**
     *
     * @param challenge
     */
    public static GenericTag createChallengeTag(@NonNull String challenge) {
        return new TagFactory("challenge", 42, challenge).create();
    }

}