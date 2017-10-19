package com.tapc.platform.model;

import android.support.annotation.NonNull;

import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.library.controller.MachineOperateController;

/**
 * Created by Administrator on 2017/10/17.
 */

public class CalibrationModel {
    private MachineController mController;

    public CalibrationModel(@NonNull MachineController controller) {
        mController = controller;
    }

    public void start(@NonNull MachineOperateController.ResultListener resultListener) {
        mController.startLeftCal(resultListener);
    }

    public void stop() {
        mController.stopLeftCal();
    }
}
