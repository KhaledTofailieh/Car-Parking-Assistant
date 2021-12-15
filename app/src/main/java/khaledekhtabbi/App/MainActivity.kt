package khaledekhtabbi.App

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.custom_dialog.view.*
import java.io.*
import java.lang.Exception
import kotlin.collections.ArrayList
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.support.v4.content.ContextCompat
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.PendingIntent.getActivity
import android.content.pm.PackageManager
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), TimeListener , ListenerInterface{
    lateinit var list_view:RecyclerView
    lateinit var cars_count:TextView
    lateinit var sum_total:TextView
    lateinit var mobject:MyData
    lateinit var  madapter:item_adapter
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val AppDir=File(FilePath.path )
            AppDir.mkdir()
            val In = FileInputStream(File(FilePath.path + "/data"))
            ObjectInputStream(In).use { obj ->
                mobject = obj.readObject() as MyData
            }
        }catch (e:Exception){
            Toast.makeText(this,"Hello Khaled This is two default items ",Toast.LENGTH_LONG).show()
            mobject=MyData()
            mobject.items_List.add(item_data(150,0,0))
            mobject.items_List.add(item_data(200,0,0))
        }

        /*if(CheckAndroidVersion()){
            CheckPermissions()
        }*/
        list_view=findViewById(R.id.List_items_view)
        cars_count=findViewById(R.id.cars_total)
        sum_total=findViewById(R.id.s_total)
        cars_count.text=mobject.cars_total.toString()
        sum_total.text=mobject.sum_total.toString()
        /*mTime=findViewById(R.id.Date_txt)
        val mtimer=Timer(this)
        mtimer.start()*/

        val layoutmanager=LinearLayoutManager(this)
        layoutmanager.orientation=LinearLayoutManager.VERTICAL
         madapter=item_adapter(this,mobject.items_List)
        list_view.layoutManager=layoutmanager
        list_view.adapter=madapter



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
           R.id.Add_new_item->{
               val mdialog=layoutInflater.inflate(R.layout.custom_dialog,null)

          val dialog= AlertDialog.Builder(this).setView(mdialog).create()

               dialog.show()
               mdialog.ok_button.setOnClickListener {
                   try{
                       val mval=mdialog.enter_value.text.toString().toInt()
                       mobject.items_List.add(item_data(mval,0,0))

                   }catch (e:Exception){
                       Toast.makeText(this,"Cannot Add Empty Value",Toast.LENGTH_SHORT).show()
                   }
                   madapter.notifyDataSetChanged()
                   dialog.dismiss()
               }
              mdialog.cancel_btn.setOnClickListener {
                  dialog.dismiss()
              }

           }
            R.id.Reset_all_items ->{
               for(mitem in mobject.items_List){
                  mitem.count=0
                   mitem.result=0
               }
                madapter.notifyDataSetChanged()
                mobject.cars_total=0
                mobject.sum_total=0
                cars_count.text="0"
                sum_total.text="0"
            }
            R.id.Delete_all_items ->{
              mobject.items_List.clear()
                madapter.notifyDataSetChanged()
                mobject.cars_total=0
                mobject.sum_total=0
                cars_count.text="0"
                sum_total.text="0"
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        try {
            val out = FileOutputStream(File(FilePath.path + "/data"))
            ObjectOutputStream(out).use { obj ->
                obj.writeObject(mobject)
            }
        }catch (e:Exception){}
    }

    override fun setTime(time: String) {
        runOnUiThread {
            //mTime.text=time
        }

    }

    override fun onAddingNewCar(value: Int) {
        with(mobject)
        {
            cars_total++
            sum_total+=value
        }
        cars_count.text=mobject.cars_total.toString()
        sum_total.text=mobject.sum_total.toString()
    }

    override fun onDeleteCar(value: Int) {
        with(mobject)
        {
            cars_total--
            sum_total-=value
        }
        cars_count.text=mobject.cars_total.toString()
        sum_total.text=mobject.sum_total.toString()
    }
    /*fun CheckAndroidVersion():Boolean{
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
             return true
        }
        return false
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun CheckPermissions():Boolean{
        val write = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        val read = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
        val listPermissionsNeeded = ArrayList<String>()

        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE)
        }
        if(read != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this , listPermissionsNeeded.toArray(Array<String>(5){}),0)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }*/
}
object FilePath
{
    val path= Environment.getExternalStorageDirectory().absolutePath+"/CounterApp"
}
class MyData : Serializable{
    val items_List=ArrayList<item_data>()
    var cars_total=0
    var sum_total=0
}
