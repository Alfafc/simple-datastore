package com.zupcat.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * It adds features to JSONObject
 */
public class DataObject extends JSONObject implements Serializable {

    private static final long serialVersionUID = 471847964351314234L;
    private static final String LIST_KEY = "_list_";

    public DataObject() {
    }

    public DataObject(final DataObject another) {
        super(another, getNames(another));
    }

    public DataObject(final Map map) {
        super(map);
    }

    public DataObject(final String source) throws JSONException {
        super(source);
    }

    public boolean isFullyEquals(final DataObject another) {
        if (another == null) {
            return false;
        }

        final JSONArray myKeys = this.names();
        final JSONArray otherKeys = another.names();

        if (myKeys == null) {
            return otherKeys == null;
        }

        if (otherKeys == null) {
            return false;
        }

        if (myKeys.length() != otherKeys.length()) {
            return false;
        }

        if (myKeys.length() == 0) {
            return true;
        }

        for (int i = 0; i < myKeys.length(); i++) {
            final Object myValue = this.get(myKeys.get(i).toString());
            final Object otherValue = another.get(otherKeys.get(i).toString());

            if (!Objects.equals(myValue, otherValue)) {
                return false;
            }
        }
        return true;
    }

    public void addItem(final DataObject item) {
        JSONArray array;

        if (has(LIST_KEY)) {
            array = getJSONArray(LIST_KEY);
        } else {
            array = new JSONArray();
            put(LIST_KEY, array);
        }
        array.put(item);
    }

    public void mergeWith(final DataObject another) {
        if (another == null) {
            return;
        }

        final String[] anotherNames = getNames(another);

        if (anotherNames != null && anotherNames.length > 0) {
            for (final String anotherName : anotherNames) {
                this.put(anotherName, another.get(anotherName));
            }
        }
    }

    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeUTF(this.toString());
    }

    private void readObject(final ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        // default deserialization
        objectInputStream.defaultReadObject();

        this.mergeWith(new DataObject(objectInputStream.readUTF()));
    }
}
