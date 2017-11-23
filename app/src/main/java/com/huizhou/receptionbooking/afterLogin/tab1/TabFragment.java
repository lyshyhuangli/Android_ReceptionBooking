package com.huizhou.receptionbooking.afterLogin.tab3;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.tab1.RvAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */

public class TabFragment extends Fragment
{
    private String type;

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private List<String> title = new ArrayList<>();
    //RecyclerView自定义Adapter
    private RvAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;


    public static TabFragment newInstance(String str)
    {
        Bundle bundle = new Bundle();
        bundle.putString("type", str);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab1_fragment, container, false);
        type = getArguments().getString("type");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new RvAdapter(getActivity(), title);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);

        adapter.setOnItemClickListener(new RvAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view)
            {
                RvAdapter.NormalViewHolder vh = (RvAdapter.NormalViewHolder) view.getTag();
                Toast.makeText(getActivity(), vh.mTextView.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
//下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
//下拉刷新一些设置 详情参考文档
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
// default is false
        mPtrFrame.setPullToRefresh(false);
// default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
//进入Activity就进行自动下拉刷新
        mPtrFrame.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mPtrFrame.autoRefresh();
            }
        }, 100);
//下拉刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler()
        {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame)
            {
                title.clear();
//模拟数据
                for (int i = 0; i <= 5; i++)
                {
                    title.add(String.valueOf(i));
                }
//模拟联网 延迟更新列表
                new Handler().postDelayed(new Runnable()
                {
                    public void run()
                    {
                        mAdapter.notifyDataSetChanged();
                        mPtrFrame.refreshComplete();
                        mPtrFrame.setLoadMoreEnable(true);
                    }
                }, 1000);
            }
        });
//上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener()
        {
            @Override
            public void loadMore()
            {
//模拟联网延迟更新数据
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
//模拟数据
                        for (int i = 0; i <= 5; i++)
                        {
                            title.add(String.valueOf(i));
                        }
                        mAdapter.notifyDataSetChanged();
                        mPtrFrame.loadMoreComplete(true);
                        Toast.makeText(getActivity(), "load more complete", Toast.LENGTH_SHORT)
                                .show();
                    }
                }, 1000);
            }
        });
        return view;
    }
}
