package vaxsoft.com.vaxphone.MainTab.ExtensionTab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vaxsoft.com.vaxphone.R;

public class ExtensionAdapter extends RecyclerView.Adapter<ExtensionAdapter.ViewHolder> {

    private static List<SipStatus> mData;

    private LayoutInflater mInflater;
    static CustomItemClickListener listener;


    ExtensionAdapter(Context context, List<SipStatus> data, CustomItemClickListener listenerParam)
    {
        listener = listenerParam;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.extension_row, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SipStatus status = mData.get(position);
        holder.mTextView.setText(status.DisplayName);
        holder.mExtensionText.setText(status.Extension);
        holder.mStatus.setText(status.Status);
        switch (status.Status)
        {
            case "Busy":
                holder.mStatusImage.setImageResource(R.drawable.ic_contact_busy);
                break;
            case "Idle":
                holder.mStatusImage.setImageResource(R.drawable.ic_contact_online);
                break;
            case "None":
                holder.mStatusImage.setImageResource(R.drawable.ic_contact_unknown);
                break;
        }
        holder.Id = status.Id;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mExtensionText;
        public TextView mStatus;
        public ImageView mStatusImage;
        public int Id;

        public ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.extensionDisplayName);
            mExtensionText = v.findViewById(R.id.extensionName);
            mStatus = v.findViewById(R.id.extensionStatus);
            mStatusImage = v.findViewById(R.id.statusImage);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNo = mData.get(getAdapterPosition()).Extension;
                    String name = mData.get(getAdapterPosition()).DisplayName;
                    listener.onItemClick(v, phoneNo,name);
                }
            });
        }
    }

}
