package nostr.event.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import nostr.event.list.GenericTagQueryList;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author guilhermegps
 *
 */
public class CustomGenericTagQueryListSerializer extends JsonSerializer<GenericTagQueryList> {

    @Override
    public void serialize(GenericTagQueryList value, JsonGenerator gen, SerializerProvider serializers) {
        try {
            var list = value.getList().parallelStream().map(gtq -> CustomGenericTagQuerySerializer.toJson(gtq))
                    .collect(Collectors.toList());

            gen.writePOJO(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

/*
    private JsonNode toJson(GenericTagQuery gtq) {
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
*/

}
