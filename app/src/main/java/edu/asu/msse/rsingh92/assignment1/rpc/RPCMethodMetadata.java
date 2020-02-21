package edu.asu.msse.rsingh92.assignment1.rpc;

import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;

/*
 * Copyright 2020 Rohit Kumar Singh,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Rohit Kumar Singh rsingh92@asu.edu
 *
 * @version February 2016
 */
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