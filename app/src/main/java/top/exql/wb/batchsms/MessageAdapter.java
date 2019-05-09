package top.exql.wb.batchsms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageEntity> {

    private int resourceId;

    public MessageAdapter(Context context, int resourceId, List<MessageEntity> objects){
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageEntity message = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView contentView = (TextView) view.findViewById(R.id.textView2);
        TextView statusView = (TextView) view.findViewById(R.id.textView3);
        contentView.setText(message.getContent());
        statusView.setText("等待发送");
        return super.getView(position, convertView, parent);
    }
}
