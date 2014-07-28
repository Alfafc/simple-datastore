package com.zupcat.property;

import com.zupcat.model.PersistentObject;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;

import java.io.IOException;
import java.io.Serializable;

public final class ListLongProperty extends AbstractListAnyProperty<Long> implements Serializable {

    private static final long serialVersionUID = 6181606486836703354L;

    public ListLongProperty(final PersistentObject owner, final String name, final boolean sentToClient, final boolean auditable) {
        super(owner, name, sentToClient, auditable);
    }

    @Override
    protected void writeItem(final Encoder encoder, final Long item) throws IOException {
        encoder.writeLong(item);
    }

    @Override
    protected Long readItem(final Decoder decoder) throws IOException {
        return decoder.readLong();
    }
}
