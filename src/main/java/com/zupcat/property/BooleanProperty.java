package com.zupcat.property;

import com.zupcat.model.DatastoreEntity;
import com.zupcat.model.ObjectVar;
import com.zupcat.model.PropertyMeta;

import java.io.Serializable;

public class BooleanProperty extends PropertyMeta<Boolean> implements Serializable {

    private static final long serialVersionUID = 6181606486836703354L;


    public BooleanProperty(final DatastoreEntity owner, final Boolean initialValue, final boolean sentToClient, final boolean auditable, final boolean indexable) {
        super(owner, initialValue, sentToClient, auditable, indexable);
    }

    protected Boolean getValueImpl(final ObjectVar objectVar) {
        return objectVar.getBoolean(name);
    }

    protected void setValueImpl(final Boolean value, final ObjectVar objectVar) {
        objectVar.set(name, value);
    }
}
