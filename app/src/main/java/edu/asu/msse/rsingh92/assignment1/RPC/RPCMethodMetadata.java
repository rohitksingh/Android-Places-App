package edu.asu.msse.rsingh92.assignment1.RPC;

import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;

public class RPCMethodMetadata {

    public String method;
    public Object[] params;
    public RPCCallback callback;
    public String urlString;
    public String resultAsJson;

    public RPCMethodMetadata(RPCCallback callback, String urlString, String method, Object[] params){
        this.method = method;
        this.callback = callback;
        this.urlString = urlString;
        this.params = params;
        this.resultAsJson = "{}";
    }
}