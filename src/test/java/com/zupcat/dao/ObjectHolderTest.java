package com.zupcat.dao;

import com.zupcat.AbstractTest;
import com.zupcat.model.ObjectHolder;
import com.zupcat.model.ObjectVar;
import com.zupcat.util.RandomUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class ObjectHolderTest extends AbstractTest {

    @Parameterized.Parameters
    public static java.util.List<Object[]> data() {
        return Arrays.asList(new Object[500][0]);
    }

    public void testSimpleCompressSerialization() {
        trySimpleSerialization(true);
    }

    public void testSimpleSerialization() {
        trySimpleSerialization(false);
    }

    private void trySimpleSerialization(final boolean compress) {
        for (int i = 0; i < 10; i++) {
            final ObjectHolder source = build();

            final ObjectHolder target = ObjectHolder.deserialize(ObjectHolder.deserialize(source.serialize(compress), compress).serialize(compress), compress);

            assertTrue(source.isFullyEquals(target));
            assertTrue(target.isFullyEquals(source));
            assertTrue(source.isFullyEquals(source));
            assertTrue(target.isFullyEquals(target));
            assertEquals(source.toString(), target.toString());

//            final int size = source.serialize(compress).length;
//            System.err.println("");
        }
    }

    private ObjectHolder build() {
        final ObjectHolder objectHolder = new ObjectHolder();

        fillObjectVar(objectHolder.getObjectVar());

        for (int i = 0; i < 1000; i++) {
            final ObjectVar item = new ObjectVar();
            fillObjectVar(item);

            objectHolder.addItem(item);
        }
        return objectHolder;
    }

    private void fillObjectVar(final ObjectVar objectVar) {
        final RandomUtils randomUtils = RandomUtils.getInstance();

        objectVar.set("string", randomUtils.getRandomSafeAlphaNumberString(20));
        objectVar.set("long", randomUtils.getRandomLong());
        objectVar.set("bool", randomUtils.getRandomBoolean());
        objectVar.set("int", randomUtils.getRandomInt(Integer.MAX_VALUE));
    }
}
