package com.example.newsfy_rework

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.newsfy_rework.fragment.AccountFragment
import com.example.newsfy_rework.fragment.ExploreFragment
import com.example.newsfy_rework.fragment.FeedFragment
import com.example.newsfy_rework.notification.ShowNotification
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class Main : AppCompatActivity(){

    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var auth: FirebaseAuth? = null
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private var preferences: ArrayList<String> = ArrayList()
    private val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navigation: BottomNavigationView = findViewById(R.id.bottom_nav)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        toolbar.title = "Your Feed"

        auth = FirebaseAuth.getInstance()
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@Main, LoginActivity::class.java))
                finish()
            } else {
                fetchPreferences()
                bundle.putStringArrayList("preferences", preferences)
                checkNotification()
                loadFragment(FeedFragment(), bundle)
            }
        }
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            when(it.itemId){
                R.id.navigation_feed -> {
                    toolbar.title = "Your Feed"
                    loadFragment(FeedFragment(), bundle)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_explore -> {
                    toolbar.title = "Explore"
                    loadFragment(ExploreFragment(), bundle)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_account -> {
                    toolbar.title = "Account"
                    loadFragment(AccountFragment(), bundle)
                    return@OnNavigationItemSelectedListener true
                }
        }
            false
        }

    private fun fetchPreferences(){
        database.child(auth?.uid.toString()).addValueEventListener (object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot){
                preferences.clear()
                for(data: DataSnapshot in dataSnapshot.children){
                    preferences.add(data.value.toString())
                }
            }
        })
    }

    private fun loadFragment(fragment: Fragment, bundle: Bundle){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.replace(R.id.main_frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun checkNotification(){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 1)
        calendar.set(Calendar.MINUTE, 27)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(applicationContext, ShowNotification::class.java)
        intent.putExtra("preferences", preferences)
        val pendingIntent =
            PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent)
    }

    override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }
}