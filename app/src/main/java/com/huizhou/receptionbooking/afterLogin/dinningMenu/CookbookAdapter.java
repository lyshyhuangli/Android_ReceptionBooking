package com.huizhou.receptionbooking.afterLogin.dinningMenu;

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
 * Created by Administrator on 2017/12/16.
 */

public class CookbookAdapter extends BaseAdapter
{

    private List<Integer> listIds = new ArrayList<Integer>();
    private Map<Integer, String> cookbookDate = new HashMap<Integer, String>();
    private Map<Integer, String> menuType = new HashMap<Integer, String>();
    private Map<Integer, String> menuContent = new HashMap<Integer, String>();

    private LayoutInflater inflater;

    public CookbookAdapter()
    {
    }

    public CookbookAdapter(
            Context context, Map<Integer, String> cookbookDate,
            Map<Integer, String> menuType, Map<Integer, String> menuContent, List<Integer> listIds

    )
    {
        this.cookbookDate = cookbookDate;
        this.menuType = menuType;
        this.menuContent = menuContent;
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
            Map<Integer, String> cookbookDate,
            Map<Integer, String> menuType, Map<Integer, String> menuContent, List<Integer> listIds
    )
    {
        try
        {
            this.cookbookDate = cookbookDate;
            this.menuType = menuType;
            this.menuContent = menuContent;
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
            CookbookAdapter.ViewHolder holder = null;
            if (convertView == null)
            {
                view = inflater.inflate(R.layout.result_item_cookbook, null);
                holder = new CookbookAdapter.ViewHolder();
                holder.idCookbook = (TextView) view.findViewById(R.id.idCookbook);
                holder.menuType = (TextView) view.findViewById(R.id.menuType);
                holder.menuContent = (TextView) view.findViewById(R.id.menuContent);
                holder.cookbookDate = (TextView) view.findViewById(R.id.cookbookDate);
                view.setTag(holder);
            }
            else
            {
                view = convertView;
                holder = (CookbookAdapter.ViewHolder) view.getTag();
            }

            int index = listIds.get(position);
            holder.idCookbook.setText(String.valueOf(index));

            holder.cookbookDate.setText(cookbookDate.get(index));
            holder.menuContent.setText(menuContent.get(index));
            holder.menuType.setText(menuType.get(index));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }


    static public class ViewHolder
    {
        TextView cookbookDate;
        TextView menuType;
        TextView menuContent;
        TextView idCookbook;
    }
}
