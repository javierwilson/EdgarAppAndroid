package org.lwr.edgar.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.youtube.player.YouTubeStandalonePlayer
import org.lwr.edgar.Constants
import org.lwr.edgar.R
import org.lwr.edgar.models.videoTutorialM


class AdapterVideo(private val mContext: Activity, private val listVideo: List<videoTutorialM>) : RecyclerView.Adapter<AdapterVideo.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txTitulo: TextView? = null
        var cont:RelativeLayout? = null
        init {
            txTitulo = view.findViewById(R.id.txTitulo) as TextView
            cont = view.findViewById(R.id.cont) as RelativeLayout

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val videoItem = listVideo[position]

        holder.txTitulo!!.setText(videoItem.name)

        holder.cont!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                var intent = YouTubeStandalonePlayer.createVideoIntent(mContext, Constants.DEVELOPER_KEY, videoItem.video_link,0,true,false)
                mContext.startActivity(intent)

                //var intent = YouTubeStandalonePlayer.createVideoIntent(mContext, DeveloperKey.DEVELOPER_KEY, VIDEO_ID, startTimeMillis, autoplay, lightboxMode)

            }
        })
    }

    override fun getItemCount(): Int {
        return listVideo.size
    }
}
