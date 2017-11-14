package com.tapc.platform.ui.fragment.mode;

import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.entity.ParameterSet;
import com.tapc.platform.entity.RunType;
import com.tapc.platform.library.util.WorkoutEnum.ProgramType;
import com.tapc.platform.model.vaplayer.PlayEntity;
import com.tapc.platform.model.vaplayer.ValUtil;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.VaAdapter;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/25.
 */

public class VaFragment extends ModeBaseFragment {
    @BindView(R.id.mode_recyclerview)
    RecyclerView mRecyclerview;

    private ArrayList<PlayEntity> mPlayList;
    private VaAdapter mVaAdapter;
    private static List<String> VA_FILE_PATH;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mode;
    }

    @Override
    protected void initView() {
        List<PlayEntity> list = new ArrayList<PlayEntity>();
        mVaAdapter = new VaAdapter(list);

        mVaAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<PlayEntity>() {
            @Override
            public void onItemClick(View view, PlayEntity playEntity) {
                if (mListener != null) {
                    List<ParameterSet> list = new ArrayList<ParameterSet>();
                    List<Object> defValues = new ArrayList<Object>();
                    defValues.add("10");
                    defValues.add("20");
                    defValues.add("30");
                    list.add(new ParameterSet("时间", "30", "min", defValues));
                    list.add(new ParameterSet("体重", "6", "kg", defValues));
                    list.add(new ParameterSet("速度", "3.0", "km/h", defValues));
                    list.add(new ParameterSet("坡度", "6", "%", defValues));
                    mListener.switchParameterSettingsFragment(mContext, list, RunType.VA, ProgramType.TIME, playEntity);
                }
            }
        });

        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerview.setAdapter(mVaAdapter);

        mPlayList = new ArrayList<PlayEntity>();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                VA_FILE_PATH = new ArrayList<String>();
                VA_FILE_PATH.add(Environment.getExternalStorageDirectory().getPath());
                VA_FILE_PATH.add(System.getenv("SECONDARY_STORAGE"));
                for (String path : VA_FILE_PATH) {
                    ArrayList<PlayEntity> playList = ValUtil.getValList(path + "/tapc/.va");
                    if (playList != null) {
                        mPlayList.addAll(playList);
                    }
                }
                e.onNext("show");
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).compose(this.bindUntilEvent
                (FragmentEvent.DESTROY_VIEW)).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                mVaAdapter.notifyDataSetChanged(mPlayList);
            }
        });
    }
}
