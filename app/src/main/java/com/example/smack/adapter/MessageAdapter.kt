package com.example.smack.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smack.R
import com.example.smack.model.Message
import com.example.smack.services.UserDataService
import com.example.smack.utilities.DEBUG
import com.example.smack.utilities.PRE_FIX
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter(var context: Context, var messages: ArrayList<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    val LOG_TAG = if (DEBUG) PRE_FIX + javaClass.simpleName else javaClass.simpleName

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage = itemView.findViewById<ImageView>(R.id.messageUserImage)
        val timeStamp = itemView.findViewById<TextView>(R.id.timeStamp)
        val userName = itemView.findViewById<TextView>(R.id.messageUserName)
        val messageBody = itemView.findViewById<TextView>(R.id.messageBody)

        fun bindMessage(context: Context, message: Message) {
            val resourceId =
                context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            userImage.setImageResource(resourceId)
            userImage.setBackgroundColor(UserDataService.returnAvatarColor(message.userAtavarColor))
            userName.text = message.userName
            timeStamp.text = returnDateString(message.timeStamp)

            messageBody.text = message.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder?.bindMessage(context, messages[position])
    }

    fun returnDateString(isoString: String): String {
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var convertedDate = Date()
        try {
            convertedDate = isoFormatter.parse(isoString)

        } catch (exception: Exception) {
            println("convertTime $exception")
        }

        val outDateString = SimpleDateFormat("E, h:mm a", Locale.getDefault())
        return outDateString.format(convertedDate)
    }

    fun setNewData(newMessages: ArrayList<Message>){
        messages.clear()
        messages.addAll(newMessages)

    }
}