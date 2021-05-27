package com.example.newsfy_rework.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsfy_rework.NewsActivity
import com.example.newsfy_rework.R
import com.example.newsfy_rework.data.News
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso

class FeedFragment: Fragment() {
    var apiKey = "855fe594a6144026aff9c373b2d10182"
    var url = "http://newsapi.org/v2/everything?q=general&apiKey=$apiKey"
    var items: ArrayList<News> = ArrayList()
    private var preferences: ArrayList<String> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var mAdapter: FeedAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.feed_activity, container, false)

        swipeRefreshLayout = view.findViewById(R.id.feed_frame)
        recyclerView = view.findViewById(R.id.feed_recycler)
        recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        preferences = this.arguments?.getStringArrayList("preferences") as ArrayList<String>

        recyclerView.layoutManager = LinearLayoutManager(context)
        populateNewsList()
        mAdapter = FeedAdapter(items, context) {news ->
            val intent = Intent(context, NewsActivity::class.java)
            intent.putExtra("news", news)
            startActivity(intent)
        }
        recyclerView.adapter = mAdapter

        swipeRefreshLayout.setOnRefreshListener {
            items.clear()
            populateNewsList()
            swipeRefreshLayout.isRefreshing = false
        }

        return view
    }

    private fun populateNewsList(){
        for (item in preferences) {

            url = url.replace(url.substringAfter("q=").substringBefore("&"), item.toLowerCase())

            Ion.with(this).load("GET", url).setHeader("user-agent", "insomnia/2020.4.1")
                .asJsonObject().setCallback { _, result ->
                    val status = result.get("status").asString

                    if (status == "ok") {
                        val array = result.get("articles").asJsonArray

                        for (i in 0 until array.size()) {
                            val obj: JsonObject = array.get(i).asJsonObject
                            val source = obj.get("source").asJsonObject.get("name").toString()
                                .removeSurrounding("\"")
                            var title = obj.get("title").toString()
                            title = title.substring(1, title.length - 1)
                            var urlToImage = obj.get("urlToImage").toString()
                            urlToImage = urlToImage.substring(1, urlToImage.length - 1)
                            var date = obj.get("publishedAt").toString()
                            date = date.substringBefore('T').removePrefix("\"") + " " +
                                    date.substringAfter('T').substring(0, 5)

                            var content = obj.get("content").toString()
                            content = content.substring(1, content.length - 1)
                            items.add(News(title, source, content, urlToImage, date))
                        }

                        mAdapter.notifyDataSetChanged()
                    }
                }
        }

        items.sortedWith(compareBy { it.date })
    }

    class FeedAdapter(
            private val items: List<News>,
            private var context: Context? = null,
            private val listener: (News) -> Unit
    ) : RecyclerView.Adapter<FeedAdapter.MyViewHolder>(){

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
            var txtViewTitle: TextView = view.findViewById<View>(R.id.news_item_title) as TextView
            var txtPublished: TextView = view.findViewById<View>(R.id.news_item_date) as TextView
            var imgViewIcon: ImageView = view.findViewById<View>(R.id.news_item_thumbnail) as ImageView

        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val news: News = items[position]
            holder.txtViewTitle.text = news.title
            holder.txtPublished.text = news.author + " " + news.date
            Picasso.with(context).load(news.urlToImage).into(holder.imgViewIcon)
            holder.itemView.setOnClickListener {
                listener(news)
                holder.itemView.setBackgroundColor(Color.parseColor("#9F9F9F"))
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
            return MyViewHolder(itemView)
        }
    }
}