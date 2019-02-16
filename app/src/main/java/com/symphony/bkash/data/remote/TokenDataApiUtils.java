package com.symphony.bkash.data.remote;

public class TokenDataApiUtils {
    public TokenDataApiUtils(){}
    public static final String TOKEN_DATA_BASE_URL_LOCAL = "https://bkash.gonona-lab.com/";
    public static final String TOKEN_DATA_BASE_URL = "http://symphony.bka.sh:8070/api/";
    public static TokenDataApiService getUserDataAPIServices(){
        return  TokenDataRetrofitClient.getTokenDataClient(TOKEN_DATA_BASE_URL).create(TokenDataApiService.class);
    }

    public static TokenDataApiService getUserDataAPIServicesLocal(){
        return  TokenDataRetrofitClient.getTokenDataClient(TOKEN_DATA_BASE_URL_LOCAL).create(TokenDataApiService.class);
    }
}
