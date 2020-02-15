package edu.asu.msse.rsingh92.assignment1.RPC;

import edu.asu.msse.rsingh92.assignment1.activities.PlaceListActivity;

public class MethodInformation {
    public String method;
    public Object[] params;
    public PlaceListActivity parent;
    public String urlString;
    public String resultAsJson;

    public MethodInformation(PlaceListActivity parent, String urlString, String method, Object[] params){
        this.method = method;
        this.parent = parent;
        this.urlString = urlString;
        this.params = params;
        this.resultAsJson = "{}";
    }
}