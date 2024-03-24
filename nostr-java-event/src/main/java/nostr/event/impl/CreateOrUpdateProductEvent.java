package nostr.event.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import nostr.base.PublicKey;
import nostr.base.annotation.Event;
import nostr.event.BaseTag;

import java.util.List;

/**
 *
 * @author eric
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Event(name = "", nip = 15)
public class CreateOrUpdateProductEvent extends NostrMarketplaceEvent {

    public CreateOrUpdateProductEvent(PublicKey sender, List<BaseTag> tags, @NonNull Product product) {
        super(sender, 30_018, tags, product);
    }
}
