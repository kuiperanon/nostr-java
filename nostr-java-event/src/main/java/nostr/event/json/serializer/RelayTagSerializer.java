package nostr.event.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import nostr.event.tag.RelayTag;

import java.io.IOException;

public class RelayTagSerializer  extends JsonSerializer<RelayTag>  {

    @Override
    public void serialize(RelayTag relayTag, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString("relay");
        jsonGenerator.writeString(relayTag.getRelay().getURI().toString());
        jsonGenerator.writeEndArray();
    }

}
