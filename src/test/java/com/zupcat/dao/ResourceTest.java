package com.zupcat.dao;

import com.zupcat.AbstractTest;
import com.zupcat.model.Resource;

public class ResourceTest extends AbstractTest {

    public void testSavingAndLoadingJavaObject() {
        Resource.buildJavaObject("testid", testClass).save(false);
        final TestClass result = (TestClass) Resource.load("testid").getJavaObject();

        assertEquals(result, testClass);
    }

    public void testSimpleSerialization() {
        final byte[] source = new byte[]{35, 17, 0x00, 33, 0x24};

        Resource.buildUnknownType("testidb", "ByteArray", source).save(false);
        final byte[] result = Resource.load("testidb").getRawValue();

        assertEquals(result.length, source.length);

        for (int i = 0; i < result.length; i++) {
            assertEquals(result[i], source[i]);
        }
    }
}
