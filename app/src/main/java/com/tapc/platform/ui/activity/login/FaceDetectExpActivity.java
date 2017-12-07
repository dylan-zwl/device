package com.tapc.platform.ui.activity.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Switch;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.ui.FaceDetectActivity;
import com.google.gson.Gson;
import com.tapc.platform.R;
import com.tapc.platform.application.Config;
import com.tapc.platform.model.net.FaceNetModel;
import com.tapc.platform.model.net.dao.FaceIdentifyAck;
import com.tapc.platform.model.net.dao.FaceUserAddAck;
import com.tapc.platform.okhttplibrary.callback.StringCallback;
import com.tapc.platform.ui.widget.DefaultDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class FaceDetectExpActivity extends FaceDetectActivity {
    private DefaultDialog mDefaultDialog;
    private FaceNetModel mFaceNetModel;
    private Switch mSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        mFaceNetModel = new FaceNetModel();
        mSwitch = (Switch) findViewById(R.id.detect_switch);
        mFaceNetModel.getToken("9t92jz3UxVcI3QVlIUXGEMpN", "NNABdnS4A9hT9ugX1bWcEa41VqizKz82", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showMessageDialog("人脸识别", "获取Token失败，无法继续使用！");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String access_token = jsonObject.getString("access_token");
                    if (!TextUtils.isEmpty(access_token)) {
                        mFaceNetModel.setAccessToken(access_token);
                    } else {
                        showMessageDialog("人脸识别", "获取Token失败，无法继续使用！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showMessageDialog("人脸识别", "获取Token失败，无法继续使用！");
                }
            }
        });
    }

    @Override
    public void onDetectCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onDetectCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
//            showMessageDialog("人脸图像采集", "采集成功");
            if (mSwitch.isChecked()) {
                login(base64ImageMap);
            } else {
                addUser(base64ImageMap);
            }
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            showMessageDialog("人脸图像采集", "采集超时");
        }
    }

    private void showMessageDialog(String title, String message) {
        if (mDefaultDialog == null) {
            DefaultDialog.Builder builder = new DefaultDialog.Builder(this);
            builder.setTitle(title).
                    setMessage(message).
                    setNegativeButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDefaultDialog.dismiss();
                                    finish();
                                }
                            });
            mDefaultDialog = builder.create();
            mDefaultDialog.setCancelable(true);
        }
        mDefaultDialog.dismiss();
        mDefaultDialog.show();
    }

    /**
     * 初始化SDK
     */
    public List<LivenessTypeEnum> mLivenessList = new ArrayList<LivenessTypeEnum>();
    public boolean isLivenessRandom = false;

    private void init() {
        // 根据需求添加活体动作
        mLivenessList.clear();
        mLivenessList.add(LivenessTypeEnum.Eye);
        mLivenessList.add(LivenessTypeEnum.Mouth);
        mLivenessList.add(LivenessTypeEnum.HeadUp);
        mLivenessList.add(LivenessTypeEnum.HeadDown);
        mLivenessList.add(LivenessTypeEnum.HeadLeft);
        mLivenessList.add(LivenessTypeEnum.HeadRight);
        mLivenessList.add(LivenessTypeEnum.HeadLeftOrRight);

        initLib();
    }

    private void initLib() {
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(this, Config.Face.appName, Config.Face.licenseFileName);
        // setFaceConfig();
    }

    private void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        config.setLivenessTypeList(mLivenessList);
        config.setLivenessRandom(isLivenessRandom);
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        config.setCheckFaceQuality(true);
        config.setFaceDecodeNumberOfThreads(2);

        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    private void login(HashMap<String, String> base64ImageMap) {
        mFaceNetModel.identify(base64ImageMap, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showMessageDialog("人脸识别", "网络错误");
            }

            @Override
            public void onResponse(String response, int id) {
                FaceIdentifyAck faceIdentifyAck = new Gson().fromJson(response, FaceIdentifyAck.class);
                if (faceIdentifyAck != null) {
                    String uid = faceIdentifyAck.getResult()[0].getUid();
                    String groupId = faceIdentifyAck.getResult()[0].getGroup_id();
                    if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(groupId)) {
                        showMessageDialog("人脸识别", " 用户组：" + groupId + "用户id： " + uid + " 登录成功 ");
                    }
                }
            }
        });
    }

    private void addUser(HashMap<String, String> base64ImageMap) {
        String uid = "" + SystemClock.currentThreadTimeMillis();
        mFaceNetModel.addUser(uid, base64ImageMap, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showMessageDialog("人脸识别", "网络错误");
            }

            @Override
            public void onResponse(String response, int id) {
                FaceUserAddAck faceUserAddAck = new Gson().fromJson(response, FaceUserAddAck.class);
                if (faceUserAddAck != null && faceUserAddAck.getError_code() == 0) {
                    showMessageDialog("人脸识别", "注册成功");
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        mFaceNetModel.cancelTag();
    }
}
