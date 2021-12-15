package khaledekhtabbi.App

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
@SuppressLint("SimpleDateFormat")
class Timer(context: Context) :Thread()
{
    private val dt= SimpleDateFormat("EEE , dd/ MM / yyyy  hh:mm:ss  a")
    var mdate:String
    private val mListener:TimeListener
    init{

        mdate=dt.format(Calendar.getInstance().time)
        mListener=context as TimeListener
    }
    override fun run() {
        super.run()
        while (true){
            mdate=dt.format(Calendar.getInstance().time)
            mListener.setTime(mdate)
            sleep(1000)
        }
    }

}
interface TimeListener{
    fun setTime(time:String)
}