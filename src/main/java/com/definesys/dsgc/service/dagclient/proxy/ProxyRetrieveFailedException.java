package com.definesys.dsgc.service.dagclient.proxy;

public class ProxyRetrieveFailedException  extends Exception{

    public ProxyRetrieveFailedException(String id){
        super("The object with id "+ id + " which not found in dag!");
    }

}
