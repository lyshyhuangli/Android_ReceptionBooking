package com.huizhou.receptionbooking.afterLogin.bookDining;

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
 * Created by Administrator on 2017/12/17.
 */

public class BookDiningAdapter extends BaseAdapter
{
    private List<Integer> listIds = new ArrayList<Integer>();
    private Map<Integer, String> diningDate = new HashMap<Integer, String>();
    private Map<Integer, String> diningType = new HashMap<Integer, String>();
    private Map<Integer, String> content = new HashMap<Integer, String>();
    private Map<Integer, String> diningRoom = new HashMap<Integer, String>();

    private LayoutInflater inflater;

    public BookDiningAdapter()
    {
    }

    public BookDiningAdapter(
            Context context, Map<Integer, String> diningDate,
            Map<Integer, String> diningType, Map<Integer, String> content,
            Map<Integer, String> diningRoom, List<Integer> listIds

    )
    {
        this.diningDate = diningDate;
        this.diningType = diningType;
        this.content = content;
        this.diningRoom = diningRoom;
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
            Map<Integer, String> diningDate,
            Map<Integer, String> diningType, Map<Integer, String> content,
            Map<Integer, String> diningRoom, List<Integer> listIds
    )
    {
        try
        {
            this.diningDate = diningDate;
            this.diningType = diningType;
            this.content = content;
            this.diningRoom = diningRoom;
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
            BookDiningAdapter.ViewHolder holder = null;
            if (convertView == null)
            {
                view = inflater.inflate(R.layout.result_item_bookdining, null);
                holder = new BookDiningAdapter.ViewHolder();
                holder.diningDate = (TextView) view.findViewById(R.id.diningDate);
                holder.diningType = (TextView) view.findViewById(R.id.diningType);
                holder.diningRoom = (TextView) view.findViewById(R.id.diningRoom);
                holder.content = (TextView) view.findViewById(R.id.content);
                holder.idBookDining = (TextView) view.findViewById(R.id.idBookDining);
                view.setTag(holder);
            }
            else
            {
                view = convertView;
                holder = (BookDiningAdapter.ViewHolder) view.getTag();
            }

            int index = listIds.get(position);
            holder.idBookDining.setText(String.valueOf(index));

            holder.diningRoom.setText(diningRoom.get(index));
            holder.diningDate.setText(diningDate.get(index));
            holder.diningType.setText(diningType.get(index));
            holder.content.setText(content.get(index));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }


    static public class ViewHolder
    {
        TextView diningRoom;
        TextView diningDate;
        TextView diningType;
        TextView content;
        TextView idBookDining;
    }

}
