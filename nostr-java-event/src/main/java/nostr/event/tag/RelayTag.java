package nostr.event.tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nostr.base.Relay;
import nostr.base.annotation.Key;
import nostr.base.annotation.Tag;
import nostr.event.BaseTag;
import nostr.event.json.serializer.RelayTagSerializer;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@Tag(code = "relay", nip = 42)
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = RelayTagSerializer.class)
public class RelayTag extends BaseTag {

    @Key
    @JsonProperty
    private Relay relay;
}
