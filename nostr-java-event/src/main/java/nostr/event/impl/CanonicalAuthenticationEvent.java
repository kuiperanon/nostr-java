package nostr.event.impl;

import lombok.NonNull;
import nostr.base.PublicKey;
import nostr.base.Relay;
import nostr.base.annotation.Event;
import nostr.event.tag.RelayTag;

import java.util.ArrayList;

@Event(name = "Canonical Authentication Event", nip = 42)
public class CanonicalAuthenticationEvent extends EphemeralEvent {

    public CanonicalAuthenticationEvent(@NonNull PublicKey pubKey, @NonNull Relay relay, @NonNull String challenge) {
        super(pubKey, 22_242, new ArrayList<>(), null);
        this.addTag(RelayTag.builder().relay(relay).build());
        this.addTag(GenericTag.create("challenge", 42, challenge));
    }

}
