package com.example.newsfy_rework

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newsfy_rework.data.News
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso


class NewsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_activity)

        val news: News = intent.getSerializableExtra("news") as News

        var imgViewIcon: ImageView = findViewById(R.id.news_image)
        var txtViewTitle: TextView = findViewById(R.id.news_title)
        var txtPublished: TextView = findViewById(R.id.news_date_publisher)
        var txtContent: TextView = findViewById(R.id.news_content)
        var share: FloatingActionButton = findViewById(R.id.share_button)

        Picasso.with(this).load(news.urlToImage).into(imgViewIcon)
        txtViewTitle.text = news.title
        txtPublished.text = news.author + " " + news.date
        txtContent.text = news.description

        share.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, txtPublished.text.toString())
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Newsfy")
            shareIntent.type = "text/plain"
            startActivity(shareIntent)
        }
    }
}