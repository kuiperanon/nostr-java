package nostr.event.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nostr.base.GenericTagQuery;
import nostr.base.IEncoder;

import java.io.IOException;
import java.io.Serial;

/**
 * @author guilhermegps
 *
 */
public class CustomGenericTagQuerySerializer extends StdSerializer<GenericTagQuery> {

    @Serial
    private static final long serialVersionUID = 6803478463890319884L;

    public CustomGenericTagQuerySerializer() {
        super(GenericTagQuery.class);
    }

    @Override
    public void serialize(GenericTagQuery value, JsonGenerator gen, SerializerProvider serializers) {
        try {
            gen.writePOJO(toJson(value));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode toJson(GenericTagQuery gtq) {
        var mapper = IEncoder.MAPPER;
        try {
            JsonNode node = mapper.valueToTree(gtq);
            ObjectNode objNode = (ObjectNode) node;
            objNode.set("#" + node.get("tagName").textValue(), node.get("value"));
            objNode.remove("tagName");
            objNode.remove("value");

            return node;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

}
