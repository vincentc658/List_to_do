package com.library.todolist

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.library.todolist.databinding.ActivitySecondBinding
import com.library.todolist.fragment.FirstPage
import com.library.todolist.fragment.SecondPage
import com.library.todolist.fragment.ThirdPage
import com.library.todolist.realm.RealmControllerEvent
import java.util.*

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var bottomShareView: BottomSheetBehavior<ConstraintLayout>
    private val realmControllerEvent by lazy { RealmControllerEvent() }
    private val myPagerAdapter by lazy { MyPagerAdapter(supportFragmentManager) }
    private var state = 1
    private lateinit var date : String
    private var time : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.viewPager.adapter = myPagerAdapter
        binding.tb.setupWithViewPager(binding.viewPager)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "List To Do"
        bottomShareView = BottomSheetBehavior.from(binding.iClAddList.clAddList)
        bottomShareView.isHideable = true
        bottomShareView.peekHeight = 0
        binding.iClAddList.timePicker.setIs24HourView(true)
        binding.iClAddList.ivCLose.setOnClickListener {
            binding.backgroundBlack.visibility = View.GONE
            bottomShareView.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.iClAddList.timePicker.setOnTimeChangedListener { timePicker, i, i2 ->
            time ="$i:$i2:${TimeConverter.getDate(System.currentTimeMillis(), TimeConverter.SS)}"
        }
        binding.iClAddList.tvSubmit.setOnClickListener {
            if (binding.iClAddList.etEvent.text.isNotEmpty()) {
                if(time==null){
                    time =TimeConverter.getDate(System.currentTimeMillis(), TimeConverter.HH_MM)+":${TimeConverter.getDate(System.currentTimeMillis(), TimeConverter.SS)}"
                }
                val eventTime = TimeConverter.getTime("$date $time")
                realmControllerEvent.insertEvent(binding.iClAddList.etEvent.text.toString(), eventTime, null){
                    setAlarm(eventTime)
                    binding.backgroundBlack.visibility = View.GONE
                    bottomShareView.state = BottomSheetBehavior.STATE_HIDDEN
                    if(state==1){
                        ( myPagerAdapter.getItem(state-1) as FirstPage).refresh()
                    }else{
                        ( myPagerAdapter.getItem(state-1) as SecondPage).refresh(date)

                    }
                }

            }

        }
    }

    fun showPopUpAddList(date : String, state : Int) {
        this.date = date
        this.state = state
        binding.iClAddList.etEvent.text.clear()
        binding.backgroundBlack.visibility = View.VISIBLE
        bottomShareView.state = BottomSheetBehavior.STATE_EXPANDED
    }

    class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val pages = listOf(
            FirstPage(),
            SecondPage(),
            ThirdPage()
        )

        override fun getItem(position: Int): Fragment {
            return pages[position]
        }

        override fun getCount(): Int {
            return pages.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Today"
                1 -> "Calender"
                else -> "TimeLine"
            }
        }
    }
    private fun setAlarm(timeInMillis: Long) {

        val second =10 * 1000


        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show()
    }
     class MyAlarm : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            Log.d("Alarm Bell", "Alarm just fired")
            val intent = Intent(context, SecondActivity::class.java)
            pushNotification(context, "channel",1,intent)
        }
         private fun pushNotification(
             context : Context,
             channelId: String,
             idNotification: Int,
             intent: Intent,

         ) {
             val contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
             val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
             val contentTitle ="Check your activity"
             val mBuilder = NotificationCompat.Builder(context, channelId)
                 .setSmallIcon(R.drawable.ic_launcher_background)
                 .setContentTitle(contentTitle)
                 .setContentText("Alarm Bell")
                 .setContentIntent(contentIntent)
                 .setAutoCancel(true)
                 .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
             mNotificationManager.notify(idNotification, mBuilder.build())
         }
    }
}
