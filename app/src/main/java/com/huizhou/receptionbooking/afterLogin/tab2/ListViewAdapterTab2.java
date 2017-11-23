package com.huizhou.receptionbooking.afterLogin.tab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huizhou.receptionbooking.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/10.
 */

public class ListViewAdapterTab2 extends BaseAdapter
{

    private List<Integer> listIds = new ArrayList<Integer>();
    private Map<Integer, String> mapPersonNames = new HashMap<Integer, String>();

    private LayoutInflater inflater;

    public ListViewAdapterTab2()
    {
    }

    public ListViewAdapterTab2(
            Context context, Map<Integer, String> mapPersonNames, List<Integer> listIds

    )
    {
        this.mapPersonNames = mapPersonNames;
        this.inflater = LayoutInflater.from(context);
        this.listIds = listIds;
    }

    @Override
    public int getCount()
    {
        return listIds.size();
    }

    @Override
    public Integer getItem(int position)
    {
        return listIds.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public void updateView(
            Map<Integer, String> mapPersonNames, List<Integer> listIds
    )
    {
        try
        {
            this.mapPersonNames = mapPersonNames;
            this.listIds = listIds;
            this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;
        try
        {
            ViewHolder holder = null;
            if (convertView == null)
            {
                view = inflater.inflate(R.layout.result_item_group, null);
                holder = new ViewHolder();
                holder.id = (TextView) view.findViewById(R.id.idData);
                holder.personName = (TextView) view.findViewById(R.id.personName);
                view.setTag(holder);
            }
            else
            {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            int index = listIds.get(position);
            holder.id.setText(String.valueOf(index));

            String name = mapPersonNames.get(index);
            holder.personName.setText(name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }


    static public class ViewHolder
    {
        TextView personName;
        TextView id;
    }
}
