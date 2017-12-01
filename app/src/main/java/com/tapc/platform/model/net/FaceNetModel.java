package com.tapc.platform.model.net;

import android.text.TextUtils;

import com.tapc.platform.okhttplibrary.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/1.
 */

public class FaceNetModel {
    private Map<String, String> mHeaders;
    private String mGroupId;
    private String mImage;
    private String mAccessToken;

    public FaceNetModel() {
        mGroupId = "FaceTestGroup";
        mHeaders = new HashMap<>();
        mHeaders.put("Content-Type", "application/x-www-form-urlencoded");
    }

    public FaceNetModel groupId(String groupId) {
        mGroupId = groupId;
        return this;
    }

    public void getToken(String client_id, String client_secret, StringCallback callback) {
//        FaceToken faceToken = new FaceToken();
//        faceToken.setClient_id(client_id);
//        faceToken.setClient_secret(client_secret);
        new NetModel().url(Urls.faceAccessToken).post("", callback);
    }


    public void addUser(String uid, HashMap<String, String> imageMap, StringCallback callback) {
        String image = null;
        Set<Map.Entry<String, String>> sets = imageMap.entrySet();
        for (Map.Entry<String, String> entry : sets) {
            image = entry.getValue();
        }
        if (TextUtils.isEmpty(image)) {
            return;
        }
//        FaceUserAdd addUser = new FaceUserAdd();
//        addUser.setAction_type("replace");
//        addUser.setGroup_id(mGroupId);
//        addUser.setUser_info("");
//        addUser.setUid(uid);
//        addUser.setImage(image);

        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("user_info", "");
        params.put("group_id", mGroupId);
        params.put("image", image);
        params.put("action_type", "replace");

        new NetModel().url(Urls.faceUserAdd + mAccessToken).headers(mHeaders).post(params, callback);
    }

    public void identify(HashMap<String, String> imageMap, StringCallback callback) {
        String image = null;
        Set<Map.Entry<String, String>> sets = imageMap.entrySet();
        for (Map.Entry<String, String> entry : sets) {
            image = entry.getValue();
        }
        if (TextUtils.isEmpty(image)) {
            return;
        }
//        FaceIdentify faceIdentify = new FaceIdentify();
//        faceIdentify.setImage(image);
//        faceIdentify.setGroup_id(mGroupId);
//        faceIdentify.setUser_top_num(1);

        Map<String, String> params = new HashMap<>();
        params.put("group_id", mGroupId);
        params.put("image", image);
        params.put("ext_fields", "");
        params.put("user_top_num", "1");
        new NetModel().url(Urls.identify + mAccessToken).headers(mHeaders).post(params, callback);
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String access_token) {
        this.mAccessToken = "?access_token=" + access_token + "&";
    }
}
