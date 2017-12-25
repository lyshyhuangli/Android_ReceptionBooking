package com.huizhou.receptionbooking.afterLogin.tab1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.tab3.ListViewAdapterTab3;
import com.huizhou.receptionbooking.database.vo.BookingMeetingRecord;
import com.huizhou.receptionbooking.database.vo.EveryMeeting;
import com.huizhou.receptionbooking.database.vo.MeetingConfirmRecord;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class ViewMeetingConfirmInfoAdapter extends BaseAdapter
{

    private List<MeetingConfirmRecord> list = new ArrayList<MeetingConfirmRecord>();

    private LayoutInflater inflater;

    public ViewMeetingConfirmInfoAdapter()
    {
    }

    public ViewMeetingConfirmInfoAdapter(
            Context context, List<MeetingConfirmRecord> lists
    )
    {
        this.inflater = LayoutInflater.from(context);
        this.list = lists;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public MeetingConfirmRecord getItem(int position)
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

        ViewMeetingConfirmInfoAdapter.ViewHolder holder = null;
        if (convertView == null)
        {
            view = inflater.inflate(R.layout.view_meeting_confirm_item, null);
            holder = new ViewMeetingConfirmInfoAdapter.ViewHolder();
            holder.attendUserName = (TextView) view.findViewById(R.id.attendUserName);
            holder.attendTypeResult = (TextView) view.findViewById(R.id.attendTypeResult);
            holder.signTypeResult = (TextView) view.findViewById(R.id.signTypeResult);
            holder.viewReason = (TextView) view.findViewById(R.id.viewReason);
            view.setTag(holder);
        }
        else
        {
            view = convertView;
            holder = (ViewMeetingConfirmInfoAdapter.ViewHolder) view.getTag();
        }

        MeetingConfirmRecord info = list.get(position);
        holder.attendUserName.setText(info.getUserName());

        int attendType = info.getAttendType();
        String attend = "参会情况：";
        if (1== attendType)
        {
            holder.attendTypeResult.setText(attend +"参加");
        }
        else if(2== attendType)
        {
            holder.attendTypeResult.setText(attend +"请假");
        }
        else if(3== attendType)
        {
            holder.attendTypeResult.setText(attend +"代参");
        }
        else
        {
            holder.attendTypeResult.setText(attend);
        }

        int isSign = info.getIsSign();
        if (1== isSign)
        {
            holder.signTypeResult.setText("签到：已签");
        }
        else
        {
            holder.signTypeResult.setText("签到：");
        }

        if(StringUtils.isNotBlank(info.getReason()))
        {
            holder.viewReason.setText("备注：" + info.getReason());
        }
        else
        {
            holder.viewReason.setText("备注：" );
        }

        return view;
    }


    static public class ViewHolder
    {
        TextView attendUserName;
        TextView attendTypeResult;
        TextView signTypeResult;
        TextView viewReason;

    }
}
