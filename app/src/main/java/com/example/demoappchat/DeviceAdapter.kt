package com.example.demoappchat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceAdapter(
    var deviceList: List<String>,
    var messagesByDevice: Map<String, List<Message>> // Thêm biến messagesByDevice
) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val deviceName = deviceList[position]
        holder.bind(deviceName)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val deviceNameTextView: TextView = itemView.findViewById(R.id.deviceName)
        init {

            // Thêm lắng nghe sự kiện khi itemView được nhấn
            itemView.setOnClickListener {
                // Lấy vị trí của item được nhấn
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Lấy tên thiết bị và tin nhắn tương ứng
                    val deviceName = deviceList[position]
                    val deviceMessages = messagesByDevice[deviceName] ?: emptyList()

                    // Tạo Intent để chuyển sang MessageActivity và truyền dữ liệu cần thiết
                    val intent = Intent(itemView.context, MessageActivity::class.java)
                    intent.putExtra("devicename", deviceName)
                    intent.putExtra("deviceMessages", ArrayList(deviceMessages))
                    // Chuyển sang MessageActivity
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun bind(deviceName: String) {
            deviceNameTextView.text = deviceName
            // Bind avatar to ImageView here if you have avatar data
        }
    }

}
