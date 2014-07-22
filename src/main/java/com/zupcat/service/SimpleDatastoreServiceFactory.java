package com.zupcat.service;

public final class SimpleDatastoreServiceFactory {

    private static SimpleDatastoreService defaultImpl = new SimpleDatastoreServiceDefaultImpl();


    public static SimpleDatastoreService getSimpleDatastoreService() {
        return defaultImpl;
    }
}
