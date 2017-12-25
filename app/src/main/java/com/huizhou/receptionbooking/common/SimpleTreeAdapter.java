package com.huizhou.receptionbooking.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhou.receptionbooking.R;

import java.util.List;


public class SimpleTreeAdapter<T> extends TreeListAdapter<T>
{
    private Context context;

    private SubClickListener subClickListener;

    public void setSubClickListener(SubClickListener topicClickListener)
    {
        this.subClickListener = topicClickListener;
    }

    public interface SubClickListener
    {
        void onClick(Node node, int position);
    }

    public SimpleTreeAdapter(
            ListView mTree, Context context, List<T> datas,
            int defaultExpandLevel
    ) throws IllegalArgumentException,
            IllegalAccessException
    {
        super(mTree, context, datas, defaultExpandLevel);
        this.context = context;
    }

    @Override
    public View getConvertView(final Node node, final int position, View convertView, ViewGroup parent)
    {

        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.adapter_tree_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView
                    .findViewById(R.id.id_treenode_icon);
            viewHolder.label = (TextView) convertView
                    .findViewById(R.id.id_treenode_label);
            convertView.setTag(viewHolder);
            viewHolder.next = (ImageView) convertView
                    .findViewById(R.id.id_treenode_next);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (node.getIcon() == -1)
        {
            viewHolder.icon.setVisibility(View.INVISIBLE);

//            if (node.getLevel() == 3)
//            {
//                viewHolder.next.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                viewHolder.next.setVisibility(View.INVISIBLE);
//            }
        }
        else
        {
            //viewHolder.next.setVisibility(View.INVISIBLE);
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }

        if("1".equals(node.getType()))
        {
            viewHolder.next.setImageResource(R.mipmap.department);
        }
        else if("2".equals(node.getType()))
        {
            viewHolder.next.setImageResource(R.mipmap.meetingmanager);
        }
        else
        {
            viewHolder.next.setImageResource(R.mipmap.user2);
        }

        viewHolder.label.setText(node.getName());

        viewHolder.icon
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        // TODO Auto-generated method stub
                        expandOrCollapse(position);
                    }
                });
        viewHolder.label
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        // TODO Auto-generated method stub

                        if (subClickListener != null)
                        {
                            subClickListener.onClick(
                                    mNodes.get(position),
                                    position
                            );
                        }
                    }
                });

        return convertView;
    }

    private final class ViewHolder
    {
        ImageView icon;
        ImageView next;
        TextView label;
    }

}
