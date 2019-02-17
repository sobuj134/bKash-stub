package com.symphony.bkash.data.remote;

public class TokenDataApiUtils {
    public TokenDataApiUtils(){}
    public static final String TOKEN_DATA_BASE_URL = "http://symphony.bka.sh:8070/api/";
    public static TokenDataApiService getUserDataAPIServices(){
        return  TokenDataRetrofitClient.getTokenDataClient(TOKEN_DATA_BASE_URL).create(TokenDataApiService.class);
    }
}
