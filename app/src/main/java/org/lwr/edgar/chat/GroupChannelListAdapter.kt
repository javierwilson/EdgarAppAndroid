package org.lwr.edgar.chat

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sendbird.android.AdminMessage
import com.sendbird.android.BaseChannel
import com.sendbird.android.FileMessage
import com.sendbird.android.GroupChannel
import com.sendbird.android.SendBird
import com.sendbird.android.UserMessage
import com.stfalcon.multiimageview.MultiImageView

import org.lwr.edgar.R
import org.lwr.edgar.utils.DateUtils
import org.lwr.edgar.utils.FileUtils
import org.lwr.edgar.utils.TextUtils
import org.lwr.edgar.utils.TypingIndicator

import java.io.File
import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.ConcurrentHashMap

/**
 * Displays a list of Group Channels within a SendBird application.
 */
internal class GroupChannelListAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mChannelList: MutableList<GroupChannel>? = null
    private val mSimpleTargetIndexMap: ConcurrentHashMap<SimpleTarget<Bitmap>, Int>
    private val mSimpleTargetGroupChannelMap: ConcurrentHashMap<SimpleTarget<Bitmap>, GroupChannel>
    private val mChannelImageNumMap: ConcurrentHashMap<String, Int>
    private val mChannelImageViewMap: ConcurrentHashMap<String, ImageView>
    private val mChannelBitmapMap: ConcurrentHashMap<String, SparseArray<Bitmap>>

    private var mItemClickListener: OnItemClickListener? = null
    private var mItemLongClickListener: OnItemLongClickListener? = null

    internal interface OnItemClickListener {
        fun onItemClick(channel: GroupChannel)
    }

    internal interface OnItemLongClickListener {
        fun onItemLongClick(channel: GroupChannel)
    }

    init {

        mSimpleTargetIndexMap = ConcurrentHashMap()
        mSimpleTargetGroupChannelMap = ConcurrentHashMap()
        mChannelImageNumMap = ConcurrentHashMap()
        mChannelImageViewMap = ConcurrentHashMap()
        mChannelBitmapMap = ConcurrentHashMap()

        mChannelList = ArrayList()
    }

    fun clearMap() {
        mSimpleTargetIndexMap.clear()
        mSimpleTargetGroupChannelMap.clear()
        mChannelImageNumMap.clear()
        mChannelImageViewMap.clear()
        mChannelBitmapMap.clear()
    }

    fun load() {
        try {
            val appDir = File(mContext.cacheDir, SendBird.getApplicationId())
            appDir.mkdirs()

            val dataFile = File(appDir, TextUtils.generateMD5(SendBird.getCurrentUser().userId + "channel_list") + ".data")

            val content = FileUtils.loadFromFile(dataFile)
            val dataArray = content.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            // Reset channel list, then add cached data.
            mChannelList!!.clear()
            for (i in dataArray.indices) {
                mChannelList!!.add(BaseChannel.buildFromSerializedData(Base64.decode(dataArray[i], Base64.DEFAULT or Base64.NO_WRAP)) as GroupChannel)
            }

            notifyDataSetChanged()
        } catch (e: Exception) {
            // Nothing to load.
        }

    }

    fun save() {
        try {
            val sb = StringBuilder()

            // Save the data into file.
            val appDir = File(mContext.cacheDir, SendBird.getApplicationId())
            appDir.mkdirs()

            val hashFile = File(appDir, TextUtils.generateMD5(SendBird.getCurrentUser().userId + "channel_list") + ".hash")
            val dataFile = File(appDir, TextUtils.generateMD5(SendBird.getCurrentUser().userId + "channel_list") + ".data")

            if (mChannelList != null && mChannelList!!.size > 0) {
                // Convert current data into string.
                var channel: GroupChannel? = null
                for (i in 0 until Math.min(mChannelList!!.size, 100)) {
                    channel = mChannelList!![i]
                    sb.append("\n")
                    sb.append(Base64.encodeToString(channel.serialize(), Base64.DEFAULT or Base64.NO_WRAP))
                }
                // Remove first newline.
                sb.delete(0, 1)

                val data = sb.toString()
                val md5 = TextUtils.generateMD5(data)

                try {
                    val content = FileUtils.loadFromFile(hashFile)
                    // If data has not been changed, do not save.
                    if (md5 == content) {
                        return
                    }
                } catch (e: IOException) {
                    // File not found. Save the data.
                }

                FileUtils.saveToFile(dataFile, data)
                FileUtils.saveToFile(hashFile, md5)
            } else {
                FileUtils.deleteFile(dataFile)
                FileUtils.deleteFile(hashFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_group_channel, parent, false)
        return ChannelHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChannelHolder).bind(mContext, mChannelList!![position], mItemClickListener, mItemLongClickListener)
    }

    override fun getItemCount(): Int {
        return mChannelList!!.size
    }

    fun setGroupChannelList(channelList: MutableList<GroupChannel>) {
        mChannelList = channelList
        notifyDataSetChanged()
    }

    fun addLast(channel: GroupChannel) {
        mChannelList!!.add(channel)
        notifyDataSetChanged()
    }

    // If the channel is not in the list yet, adds it.
    // If it is, finds the channel in current list, and replaces it.
    // Moves the updated channel to the front of the list.
    fun updateOrInsert(channel: BaseChannel) {
        if (channel !is GroupChannel) {
            return
        }

        for (i in mChannelList!!.indices) {
            if (mChannelList!![i].url == channel.url) {
                mChannelList!!.remove(mChannelList!![i])
                mChannelList!!.add(0, channel)
                notifyDataSetChanged()
                Log.v(GroupChannelListAdapter::class.java.simpleName, "Channel replaced.")
                return
            }
        }

        mChannelList!!.add(0, channel)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mItemLongClickListener = listener
    }

    /**
     * A ViewHolder that contains UI to display information about a GroupChannel.
     */
    private inner class ChannelHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var topicText: TextView
        internal var lastMessageText: TextView
        internal var unreadCountText: TextView
        internal var dateText: TextView
        internal var memberCountText: TextView
        internal var coverImage: MultiImageView
        internal var typingIndicatorContainer: LinearLayout

        init {

            topicText = itemView.findViewById<View>(R.id.text_group_channel_list_topic) as TextView
            lastMessageText = itemView.findViewById<View>(R.id.text_group_channel_list_message) as TextView
            unreadCountText = itemView.findViewById<View>(R.id.text_group_channel_list_unread_count) as TextView
            dateText = itemView.findViewById<View>(R.id.text_group_channel_list_date) as TextView
            memberCountText = itemView.findViewById<View>(R.id.text_group_channel_list_member_count) as TextView
            coverImage = itemView.findViewById<View>(R.id.image_group_channel_list_cover) as MultiImageView
            coverImage.shape = MultiImageView.Shape.CIRCLE

            typingIndicatorContainer = itemView.findViewById<View>(R.id.container_group_channel_list_typing_indicator) as LinearLayout
        }

        /**
         * Binds views in the ViewHolder to information contained within the Group Channel.
         * @param context
         * @param channel
         * @param clickListener A listener that handles simple clicks.
         * @param longClickListener A listener that handles long clicks.
         */
        internal fun bind(context: Context, channel: GroupChannel,
                          clickListener: OnItemClickListener?,
                          longClickListener: OnItemLongClickListener?) {
            topicText.text = TextUtils.getGroupChannelTitle(channel)
            memberCountText.text = channel.memberCount.toString()

            setChannelImage(context, channel, coverImage)

            val unreadCount = channel.unreadMessageCount
            // If there are no unread messages, hide the unread count badge.
            if (unreadCount == 0) {
                unreadCountText.visibility = View.INVISIBLE
            } else {
                unreadCountText.visibility = View.VISIBLE
                unreadCountText.text = channel.unreadMessageCount.toString()
            }

            val lastMessage = channel.lastMessage
            if (lastMessage != null) {
                dateText.visibility = View.VISIBLE
                lastMessageText.visibility = View.VISIBLE

                // Display information about the most recently sent message in the channel.
                dateText.setText(DateUtils.formatDateTime(lastMessage.createdAt))
                // Bind last message text according to the type of message. Specifically, if
                // the last message is a File Message, there must be special formatting.
                if (lastMessage is UserMessage) {
                    lastMessageText.text = lastMessage.message
                } else if (lastMessage is AdminMessage) {
                    lastMessageText.text = lastMessage.message
                } else {
                    val lastMessageString = String.format(
                            context.getString(R.string.group_channel_list_file_message_text),
                            (lastMessage as FileMessage).sender.nickname)
                    lastMessageText.text = lastMessageString
                }
            } else {
                dateText.visibility = View.INVISIBLE
                lastMessageText.visibility = View.INVISIBLE
            }

            /*
             * Set up the typing indicator.
             * A typing indicator is basically just three dots contained within the layout
             * that animates. The animation is implemented in the {@link TypingIndicator#animate() class}
             */
            val indicatorImages = ArrayList<ImageView>()
            indicatorImages.add(typingIndicatorContainer.findViewById<View>(R.id.typing_indicator_dot_1) as ImageView)
            indicatorImages.add(typingIndicatorContainer.findViewById<View>(R.id.typing_indicator_dot_2) as ImageView)
            indicatorImages.add(typingIndicatorContainer.findViewById<View>(R.id.typing_indicator_dot_3) as ImageView)

            val indicator = TypingIndicator(indicatorImages, 600)
            indicator.animate()

            // debug
            //            typingIndicatorContainer.setVisibility(View.VISIBLE);
            //            lastMessageText.setText(("Someone is typing"));

            // If someone in the channel is typing, display the typing indicator.
            if (channel.isTyping) {
                typingIndicatorContainer.visibility = View.VISIBLE
                lastMessageText.text = "Someone is typing"
            } else {
                // Display typing indicator only when someone is typing
                typingIndicatorContainer.visibility = View.GONE
            }

            // Set an OnClickListener to this item.
            if (clickListener != null) {
                itemView.setOnClickListener { clickListener.onItemClick(channel) }
            }

            // Set an OnLongClickListener to this item.
            if (longClickListener != null) {
                itemView.setOnLongClickListener {
                    longClickListener.onItemLongClick(channel)

                    // return true if the callback consumed the long click
                    true
                }
            }
        }

        private fun setChannelImage(context: Context, channel: GroupChannel, multiImageView: MultiImageView) {
            val members = channel.members
            if (members != null) {
                val size = members.size
                if (size >= 1) {
                    var imageNum = size
                    if (size >= 4) {
                        imageNum = 4
                    }

                    if (!mChannelImageNumMap.containsKey(channel.url)) {
                        mChannelImageNumMap[channel.url] = imageNum
                        mChannelImageViewMap[channel.url] = multiImageView

                        multiImageView.clear()

                        for (index in 0 until imageNum) {
                            val simpleTarget = object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, glideAnimation: Transition<in Bitmap>) {
                                    val index = mSimpleTargetIndexMap[this]
                                    if (index != null) {
                                        val channel = mSimpleTargetGroupChannelMap[this]

                                        var array = mChannelBitmapMap[channel!!.url]
                                        if (array == null) {
                                            array = SparseArray()
                                            mChannelBitmapMap[channel.url] = array
                                        }
                                        array.put(index, resource)

                                        val num = mChannelImageNumMap[channel.url]
                                        if (num != null) {
                                            if (array.size() == num) {
                                                val multiImageView = mChannelImageViewMap[channel.url] as MultiImageView?

                                                for (i in 0 until array.size()) {
                                                    multiImageView!!.addImage(array.get(i))
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            mSimpleTargetIndexMap[simpleTarget] = index
                            mSimpleTargetGroupChannelMap[simpleTarget] = channel

                            val myOptions = RequestOptions()
                                    .dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                            Glide.with(context)
                                    .asBitmap()
                                    .load(members[index].profileUrl)
                                    .apply(myOptions)
                                    .into<SimpleTarget<Bitmap>>(simpleTarget)
                        }
                    }
                }
            }
        }
    }
}
