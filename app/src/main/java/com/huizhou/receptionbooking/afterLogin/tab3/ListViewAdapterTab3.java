package com.huizhou.receptionbooking.afterLogin.tab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.database.vo.BookingMeetingRecord;
import com.huizhou.receptionbooking.database.vo.EveryMeeting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class ListViewAdapterTab3 extends BaseAdapter
{

    private List<BookingMeetingRecord> list = new ArrayList<BookingMeetingRecord>();

    private LayoutInflater inflater;

    public ListViewAdapterTab3()
    {
    }

    public ListViewAdapterTab3(
            Context context, List<BookingMeetingRecord> lists

    )
    {
        this.inflater = LayoutInflater.from(context);
        this.list = lists;
    }

    /**
     * TextView的监听接口
     */
    public interface onItemTextViewListener
    {
        void onTextViewClick(View v, int i,String type);
    }

    private onItemTextViewListener mOnItemTextViewListener;

    public void setOnItemTextViewClickListener(onItemTextViewListener mOnItemTextViewListener)
    {
        this.mOnItemTextViewListener = mOnItemTextViewListener;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public BookingMeetingRecord getItem(int position)
    {
        return list.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = null;
        try
        {
            ListViewAdapterTab3.ViewHolder holder = null;
            if (convertView == null)
            {
                view = inflater.inflate(R.layout.list_item_tab3, null);
                holder = new ListViewAdapterTab3.ViewHolder();
                holder.meetingPlace = (TextView) view.findViewById(R.id.meetingPlace);
                holder.am = (TextView) view.findViewById(R.id.am);
                holder.pm = (TextView) view.findViewById(R.id.pm);
                holder.bookUserIdAm = (TextView) view.findViewById(R.id.bookUserIdAm);
                holder.bookUserIdPm = (TextView) view.findViewById(R.id.bookUserIdPm);
                holder.meetingRoomId = (TextView) view.findViewById(R.id.meetingRoomId);
                holder.publishRoomIdAm = (TextView) view.findViewById(R.id.publishRoomIdAm);
                holder.publishRoomIdPm = (TextView) view.findViewById(R.id.publishRoomIdPm);
                view.setTag(holder);
            }
            else
            {
                view = convertView;
                holder = (ListViewAdapterTab3.ViewHolder) view.getTag();
            }

            BookingMeetingRecord info = list.get(position);
            holder.meetingPlace.setText(info.getName());
            holder.meetingRoomId.setText(String.valueOf(info.getId()));

            for (EveryMeeting e : info.getMeetingList())
            {
                if ("am".equals(e.getAmOrPm()))
                {
                    holder.am.setText(e.getStartTime() + "-" + e.getEndTime() + " " + e.getThreaf());
                    holder.publishRoomIdAm.setText(String.valueOf(e.getMid()));
                    holder.bookUserIdAm.setText(e.getBookUser());
                }
                else if("pm".equals(e.getAmOrPm()))
                {
                    holder.pm.setText(e.getStartTime() + "-" + e.getEndTime() + " " + e.getThreaf());
                    holder.publishRoomIdPm.setText(String.valueOf(e.getMid()));
                    holder.bookUserIdPm.setText(e.getBookUser());
                }
                else
                {

                }
            }

            holder.am.setTag(holder);
            holder.am.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemTextViewListener.onTextViewClick(v, position,"am");
                }
            });


//            holder.pm.setTag(holder);
//            holder.pm.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    mOnItemTextViewListener.onTextViewClick(v, position,"pm");
//                }
//            });


        }
        catch (Exception e)
        {
            System.out.println("4444444444444444444444444");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return view;
    }


    static public class ViewHolder
    {
        TextView meetingPlace;
        TextView am;
        TextView pm;

        //会议室id
        TextView meetingRoomId;

        //发布会议Id
        TextView publishRoomIdAm;
        TextView publishRoomIdPm;

        TextView bookUserIdAm;
        TextView bookUserIdPm;
    }

}
