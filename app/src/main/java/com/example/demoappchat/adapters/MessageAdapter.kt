package com.example.demoappchat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demoappchat.models.Message
import com.example.demoappchat.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

class MessageAdapter(
    private val originalMessages: List<Message>,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messages: List<Message> = originalMessages

    private val VIEW_TYPE_SENT = 0
    private val VIEW_TYPE_RECEIVED = 1

    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showMessage: TextView = itemView.findViewById(R.id.show_message)
        val txttime: TextView = itemView.findViewById(R.id.txt_time)
    }

    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showMessage: TextView = itemView.findViewById(R.id.show_message)
        val txttime: TextView = itemView.findViewById(R.id.txt_time)
        val txtDateLeft: TextView = itemView.findViewById(R.id.txtDateLeft)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item_right, parent, false)
            SentMessageViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item_left, parent, false)
            ReceivedMessageViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = messages[position]

        val originalFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        val time = originalFormat.parse(currentItem.time)

        // time chat
        val gmtFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        gmtFormat.timeZone = TimeZone.getTimeZone("GMT")
        val formattedTime = time?.let { gmtFormat.format(it) }

        // date chat
        val gmtFormatDate = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        gmtFormatDate.timeZone = TimeZone.getTimeZone("GMT")
        val formattedDate = time?.let { gmtFormatDate.format(it) }

        val dateReceiver: Date = gmtFormatDate.parse(formattedDate)

        // get current time
        val currentTime = Date()

        if (holder.itemViewType == VIEW_TYPE_SENT) {
            val sentHolder = holder as SentMessageViewHolder
            sentHolder.showMessage.text = currentItem.data
            sentHolder.txttime.text = formattedTime
        } else {
            val receivedHolder = holder as ReceivedMessageViewHolder
            receivedHolder.showMessage.text = currentItem.data
            receivedHolder.txttime.text = formattedTime
            if(is24Hours(dateReceiver,currentTime)){
                receivedHolder.txtDateLeft.text = formattedDate
            }else{
                receivedHolder.txtDateLeft.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = messages[position]
        return if (currentItem.direc == 0) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }
    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(messages.size - 1)
    }

    // Kiem tra xem tin nhan gui/nhan la qua 24 tieng chua?
    private fun is24Hours(date1: Date, date2: Date): Boolean {
        val dateTime1 = LocalDateTime.ofInstant(date1.toInstant(), ZoneId.systemDefault())
        val dateTime2 = LocalDateTime.ofInstant(date2.toInstant(), ZoneId.systemDefault())

        val hoursDifference = abs(ChronoUnit.HOURS.between(dateTime1, dateTime2))

        return hoursDifference >= 24
    }
}
