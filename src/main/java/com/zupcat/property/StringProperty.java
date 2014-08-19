package com.zupcat.property;

import com.zupcat.model.DatastoreEntity;
import com.zupcat.model.ObjectVar;
import com.zupcat.model.PropertyMeta;

import java.io.Serializable;

public class StringProperty extends PropertyMeta<String> implements Serializable {

    private static final long serialVersionUID = 6181606486836703354L;


    public StringProperty(final DatastoreEntity owner, final String initialValue, final boolean sentToClient, final boolean auditable, final boolean indexable) {
        super(owner, initialValue, sentToClient, auditable, indexable);
    }

    protected String getValueImpl(final ObjectVar objectVar) {
        return objectVar.getString(name);
    }

    protected void setValueImpl(final String value, final ObjectVar objectVar) {
        objectVar.set(name, value);
    }
}
