package com.example.guanzhuli.icart.utils;

import com.example.guanzhuli.icart.utils.*;
import com.example.guanzhuli.icart.service.ClientService;

public class API {
    public static final String URL = "http://192.168.80.1:8080/";

    public static ClientService getClientService(){
        return Client.getClient(URL).create(ClientService.class);
    }
}
