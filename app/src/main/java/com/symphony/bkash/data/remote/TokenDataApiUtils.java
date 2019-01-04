package com.symphony.bkash.data.remote;

public class TokenDataApiUtils {
    public TokenDataApiUtils(){}
    public static final String TOKEN_DATA_BASE_URL = "http://bdmobile.net/bkash/";
    public static TokenDataApiService getUserDataAPIServices(){
        return  TokenDataRetrofitClient.getTokenDataClient(TOKEN_DATA_BASE_URL).create(TokenDataApiService.class);
    }
}
