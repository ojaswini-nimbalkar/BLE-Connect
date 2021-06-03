//package com.tony.android.bluetoothlegatt.adapter;
//
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.tony.android.bluetoothlegatt.R;
//
//public class CharListAdapter extends BaseArrayListAdapter<BluetoothGattCharacteristic>{
//    protected Context mContext;
//
//    public CharListAdapter(Context context) {
//        super(context);
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        final ViewHolder viewHolder;
//        // General ListView optimization code.
//
//        final BluetoothGattDescriptor desc = (BluetoothGattDescriptor) getItem(position);
//        if (view == null) {
//            view = mInflater.inflate(R.layout.item_characteristic, null);
//            viewHolder = new ViewHolder();
//            viewHolder.descUuid = (TextView) view.findViewById(R.id.char_name);
//            viewHolder.descValue = (TextView) view.findViewById(R.id.char_id);
//            viewHolder.btnDownload = view.findViewById(R.id.btn_download);
//            viewHolder.btnUpload = view.findViewById(R.id.btn_upload);
//            viewHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String data = "";
//                    String uuid = desc.getUuid().toString();
//                    writeCharacteristic(uuid, data);
//                }
//            });
//            viewHolder.btnUpload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String data = "";
//                    String uuid = desc.getUuid().toString();
//                    readCharacteristic(uuid, data);
//                }
//            });
//
//            view.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) view.getTag();
//        }
//
//        viewHolder.descUuid.setText("uuid: "+desc.getUuid().toString().substring(4,8));
//        if (desc.getValue() != null && desc.getValue().length > 0){
//            viewHolder.descValue.setText("Name:");
//        }else{
//            viewHolder.descValue.setText("Name: Unknown");
//        }
//        return view;
//    }
//
//    private void readCharacteristic(String uuid, String data) {
//        //TODO
//    }
//
//    private void writeCharacteristic(String uuid, String data) {
//        //TODO
//    }
//
//    static class ViewHolder {
//        TextView descUuid;
//        TextView descValue;
//        ImageButton btnDownload;
//        ImageButton btnUpload;
//    }
//}
