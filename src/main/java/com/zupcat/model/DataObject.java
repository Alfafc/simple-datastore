package com.zupcat.model;

import com.zupcat.property.ListProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.List;
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

    public void addItem(final DataObject item) {
        final JSONArray array = getJsonArray();

        array.put(item);
    }

    public List<DataObject> getItems() {
        final JSONArray array = getJsonArray();

        return ListProperty.getInternalListFromJSONArray(array);
    }

    private JSONArray getJsonArray() {
        JSONArray array;

        if (has(LIST_KEY)) {
            array = getJSONArray(LIST_KEY);
        } else {
            array = new JSONArray();
            put(LIST_KEY, array);
        }
        return array;
    }

    public void mergeWith(final JSONObject another) {
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

    public boolean isFullyEquals(final DataObject another) {
        return compareJSONS(this, another);
    }

    private boolean compareJSONS(final JSONObject object1, final JSONObject object2) {
        if (object1 == null) {
            return object2 == null;
        }

        if (object2 == null) {
            return false;
        }

        final JSONArray object1Keys = object1.names();
        final JSONArray object2Keys = object2.names();

        if (object1Keys == null) {
            return object2Keys == null;
        }

        if (object2Keys == null) {
            return false;
        }

        if (object1Keys.length() != object2Keys.length()) {
            return false;
        }

        if (object1Keys.length() == 0) {
            return true;
        }

        for (int i = 0; i < object1Keys.length(); i++) {
            final String key = object1Keys.get(i).toString();

            final Object object1ItemValue = object1.get(key);
            final Object object2ItemValue = object2.get(key);

            if (!compareObjects(object1ItemValue, object2ItemValue)) {
                return false;
            }
        }
        return true;
    }

    private boolean compareObjects(final Object object1, final Object object2) {
        if (object1 == null) {
            return object2 == null;
        }

        if (object2 == null) {
            return false;
        }

        if (object1 instanceof JSONObject) {
            if (!(object2 instanceof JSONObject)) {
                return false;
            }

            return compareJSONS((JSONObject) object1, (JSONObject) object2);
        } else if (object1 instanceof JSONArray) {
            if (!(object2 instanceof JSONArray)) {
                return false;
            }

            final JSONArray array1 = (JSONArray) object1;
            final JSONArray array2 = (JSONArray) object2;

            if (array1.length() != array2.length()) {
                return false;
            }

            if (array1.length() == 0) {
                return true;
            }

            for (int j = 0; j < array1.length(); j++) {
                if (!compareObjects(array1.get(j), array2.get(j))) {
                    return false;
                }
            }

            return true;

        } else {
            return Objects.equals(object1, object2);
        }
    }

    @Override
    public boolean equals(final Object other) {
        return other != null && other instanceof DataObject && compareJSONS(this, (JSONObject) other);
    }

    @Override
    public int hashCode() {
        final StringWriter stringWriter = new StringWriter(1024);
        write(stringWriter);
        return stringWriter.toString().hashCode();
    }
}
