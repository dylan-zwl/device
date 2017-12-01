package com.tapc.platform.model.net;

/**
 * Created by Administrator on 2017/8/17.
 */

public class Urls {


    //face
    public static final String faceUrl = "https://aip.baidubce.com/rest/2.0/face/v2/";

    // 1. grant_type为固定参数
    // 2. 官网获取的 API Key
    // 3. 官网获取的 Secret Key
    public static final String faceAccessToken = "https://aip.baidubce" +
            ".com/oauth/2.0/token?grant_type=client_credentials" + "&client_id=" +
            "9t92jz3UxVcI3QVlIUXGEMpN" + "&client_secret=" + "NNABdnS4A9hT9ugX1bWcEa41VqizKz82";

    public static final String faceUserAdd = faceUrl + "faceset/user/add";
    public static final String identify = faceUrl + "identify";
}
