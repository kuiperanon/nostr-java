
package nostr.event.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import nostr.base.Command;
import nostr.event.impl.CanonicalAuthenticationEvent;

/**
 *
 * @author eric
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ClientAuthenticationMessage extends BaseAuthMessage {

    @JsonProperty
    private final CanonicalAuthenticationEvent event;
    
    public ClientAuthenticationMessage(@NonNull CanonicalAuthenticationEvent event) {
        super(Command.AUTH.name());
        this.event = event;
    }
    
}