package com.highersun.sign.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.highersun.sign.R;
import com.highersun.sign.bean.IbeaconBean;

import java.util.List;

/**
 * Created by admin on 2016/8/23.
 */

public class ScanResultAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<IbeaconBean> mIbeanList;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public ScanResultAdapter(Context mContext, List<IbeaconBean> mIbeanList) {
        this.mContext = mContext;
        this.mIbeanList = mIbeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScanViewHolder(View.inflate(mContext, R.layout.item_scan_result, null));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ScanViewHolder) {
            if (mIbeanList.get(position).getName() != null) {
                ((ScanViewHolder) holder).companyName.setText(mIbeanList.get(position).getName());
            } else {
                ((ScanViewHolder) holder).companyName.setText("未知设备");
            }


//            ((ScanViewHolder) holder).companyDistance.setText(mIbeanList.get(position).getDistance());
            if (mIbeanList.get(position).getRssi() < -50) {
                ((ScanViewHolder) holder).ivRssi.setImageResource(R.drawable.ic_rssi_high);
            } else if (mIbeanList.get(position).getRssi() < -30) {
                ((ScanViewHolder) holder).ivRssi.setImageResource(R.drawable.ic_rssi_mid);
            } else {
                ((ScanViewHolder) holder).ivRssi.setImageResource(R.drawable.ic_rssi_low);
            }
            if (mListener == null) return;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position, mIbeanList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mIbeanList != null) {
            return mIbeanList.size();
        } else {
            return 1;
        }
    }


    class ScanViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        TextView companyDistance;
        ImageView ivRssi;


        public ScanViewHolder(View itemView) {
            super(itemView);
            companyName = (TextView) itemView.findViewById(R.id.item_tv_company_name);
            companyDistance = (TextView) itemView.findViewById(R.id.item_tv_company_distance);
            ivRssi = (ImageView) itemView.findViewById(R.id.item_iv_rssi);
        }
    }


    public synchronized void addDevice(IbeaconBean device) {
        if (device == null)
            return;

        for (int i = 0; i < mIbeanList.size(); i++) {
            String btAddress = mIbeanList.get(i).bluetoothAddress;
            if (btAddress.equals(device.bluetoothAddress)) {
                device.setAddTime(System.currentTimeMillis());
                mIbeanList.add(i + 1, device);
                mIbeanList.remove(i);
                return;
            }

        }
        device.setAddTime(System.currentTimeMillis());
        mIbeanList.add(device);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, IbeaconBean data);
    }
}
