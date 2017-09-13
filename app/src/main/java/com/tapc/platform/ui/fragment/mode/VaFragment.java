package com.tapc.platform.ui.fragment.mode;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.application.TapcApplication;
import com.tapc.platform.model.vaplayer.PlayEntity;
import com.tapc.platform.model.vaplayer.ValUtil;
import com.tapc.platform.ui.activity.run.RunVaActivity;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.VaAdpater;
import com.tapc.platform.ui.fragment.BaseFragment;
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

public class VaFragment extends BaseFragment {
    @BindView(R.id.mode_recyclerview)
    RecyclerView mRecyclerview;

    private ArrayList<PlayEntity> mPlayList;
    private VaAdpater mVaAdpater;
    private static String[] VA_FILE_PATH = new String[]{"/mnt/external_sd/tapc/.va"};

    @Override
    protected int getContentView() {
        return R.layout.fragment_mode;
    }

    @Override
    protected void initView() {
        List<PlayEntity> list = new ArrayList<PlayEntity>();
        mVaAdpater = new VaAdpater(list);

        mVaAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<PlayEntity>() {
            @Override
            public void onItemClick(View view, PlayEntity playEntity) {
//                StartActivity.replaceFragment(mContext, ParameterSettingsFragment.class);
                TapcApplication.getInstance().getService().setStartMenuVisibility(false);
                RunVaActivity.launch(mContext, playEntity);
            }
        });

        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerview.setAdapter(mVaAdpater);

        mPlayList = new ArrayList<PlayEntity>();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                for (String path : VA_FILE_PATH) {
                    ArrayList<PlayEntity> playList = ValUtil.getValList(path);
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
                mVaAdpater.notifyDataSetChanged(mPlayList);
            }
        });
    }
}
