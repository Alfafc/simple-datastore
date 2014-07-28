package com.zupcat.property;

import com.zupcat.model.PersistentObject;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public final class MapStringMapStringIntegerProperty extends AbstractMapStringAnyProperty<String, Map<String, Integer>> implements Serializable {

    private static final long serialVersionUID = 6181606486836703354L;

    public MapStringMapStringIntegerProperty(final PersistentObject owner, final String name, final boolean sentToClient, final boolean auditable) {
        super(owner, name, sentToClient, auditable);
    }

    @Override
    protected void writeKeyValue(final Encoder encoder, final Entry<String, Map<String, Integer>> entry) throws IOException {
        // Key
        encoder.writeString(entry.getKey());

        // Value
        final Map<String, Integer> itemsMap = entry.getValue();

        if (!itemsMap.isEmpty()) {
            encoder.writeMapStart();
            encoder.setItemCount(itemsMap.size());

            for (final Entry<String, Integer> itemEntry : itemsMap.entrySet()) {
                encoder.startItem();
                encoder.writeString(itemEntry.getKey());
                encoder.writeInt(itemEntry.getValue());
            }

            encoder.writeMapEnd();
        }
    }

    @Override
    protected Entry<String, Map<String, Integer>> readKeyValue(final Decoder decoder) throws IOException {
        final Map<String, Integer> items = new HashMap<>();

        final Entry<String, Map<String, Integer>> result = new AbstractMap.SimpleEntry<>(decoder.readString(), items);

        for (long i = decoder.readMapStart(); i != 0; i = decoder.mapNext()) {
            for (long j = 0; j < i; j++) {
                final String key = decoder.readString();
                items.put(key, decoder.readInt());
            }
        }
        return result;
    }
}
