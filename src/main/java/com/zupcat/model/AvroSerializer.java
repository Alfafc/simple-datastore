package com.zupcat.model;

import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public final class AvroSerializer<T extends SpecificRecordBase> implements Serializable {

    private static final long serialVersionUID = 471847964351314234L;
    private static final Object LOCK_OBJECT = new Object();

//    private static final byte[] EB = {
//            0x22 - 0x17,
//            0x50 + 0x21,
//            0x29 + 0x24,
//            0x66 - 0x21,
//            0x36 + 0x24,
//            0x18 - 0x11,
//            0x37 - 0x21,
//            0x32 - 0x21,
//            0x64 + 0x03,
//            0x91 - 0x24,
//            0x21
//    };

    public static final String ID_KEY = "_i";


    private transient BinaryEncoder reusableBinaryEncoder;
    private transient BinaryDecoder reusableBinaryDecoder;


    private BinaryEncoder getReusableBinaryEncoder() {
        if (reusableBinaryEncoder == null) {
            reusableBinaryEncoder = EncoderFactory.get().binaryEncoder(new ByteArrayOutputStream(1024), null);
        }
        return reusableBinaryEncoder;
    }

    private BinaryDecoder getReusableBinaryDecoder() {
        if (reusableBinaryDecoder == null) {
            reusableBinaryDecoder = DecoderFactory.get().binaryDecoder(new byte[1], null);
        }
        return reusableBinaryDecoder;
    }


    public void serializeTo(final T record, final Class<T> recordClass, final boolean compressing, final OutputStream outputStream) {
        try {
            serializeImpl(record, recordClass, compressing, outputStream);
        } catch (final Exception _exception) {
            throw new RuntimeException("Problems serializing record [" + record.getClass().getName() + "|" + record + "]: " + _exception.getMessage(), _exception);
        }
    }

    public byte[] serialize(final T record, final Class<T> recordClass, final boolean compressing) {
        ByteArrayOutputStream byteOutputStream = null;

        try {
            byteOutputStream = new ByteArrayOutputStream(1024);

            serializeImpl(record, recordClass, compressing, byteOutputStream);

            byte[] result = byteOutputStream.toByteArray();
            byteOutputStream.close();
            byteOutputStream = null;

//            enc(result);

            return result;

        } catch (final Exception _exception) {
            throw new RuntimeException("Problems serializing record [" + record.getClass().getName() + "|" + record + "]: " + _exception.getMessage(), _exception);
        } finally {
            if (byteOutputStream != null) {
                try {
                    byteOutputStream.close();
                } catch (final IOException _ioException) {
                    // ok to ignore
                }
            }
        }
    }

    private void serializeImpl(final T record, final Class<T> recordClass, final boolean compressing, final OutputStream targetOutputStream) throws Exception {
        final OutputStream outputStream = compressing ? new DeflaterOutputStream(targetOutputStream) : targetOutputStream;

        synchronized (LOCK_OBJECT) {
            final Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, getReusableBinaryEncoder());
            final SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(recordClass);

            writer.write(record, encoder);
            encoder.flush();
            outputStream.close();
        }
    }

    public T deserialize(final byte[] bytes, final Class<T> recordClass, final boolean compressed) {
        ByteArrayInputStream inputStream = null;

        try {
            inputStream = new ByteArrayInputStream(bytes);
            return deserialize(inputStream, recordClass, compressed);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    // ok to ignore
                }
            }
        }
    }

    public T deserialize(final InputStream inputStream, final Class<T> recordClass, final boolean compressed) {
        try {
//            enc(bytes);

            final SpecificDatumReader<T> reader = new SpecificDatumReader<>(recordClass);
            synchronized (LOCK_OBJECT) {
                final Decoder decoder = DecoderFactory.get().binaryDecoder(compressed ? new InflaterInputStream(inputStream) : inputStream, getReusableBinaryDecoder());

                return reader.read(null, decoder);
            }

        } catch (final IOException _ioException) {
            throw new RuntimeException("Problems when deserializing record [" + recordClass.getName() + "]: " + _ioException.getMessage(), _ioException);
        }
    }


//    private void enc(final byte[] input) {
//        final int size = input.length;
//        final int tSize = EB.length;
//
//        for (int i = 0; i < size; i++) {
//            final byte tb = i < tSize ? EB[i] : 0x53;
//
//            input[i] = (byte) (input[i] ^ (tb));
//        }
//    }
}
