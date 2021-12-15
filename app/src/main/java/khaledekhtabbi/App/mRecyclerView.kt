package khaledekhtabbi.App

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item.view.*
import java.io.Serializable

data class item_data(val value:Int , var count:Int,var result:Int): Serializable
class item_adapter(val context:Context , val items:ArrayList<item_data>):RecyclerView.Adapter<item_adapter.mViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): mViewHolder {
        val mview=LayoutInflater.from(context).inflate(R.layout.list_item,p0,false)
        return mViewHolder(mview)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: mViewHolder, p1: Int) {
        p0.num=p1
       p0.setData(items[p1],p1)
    }

    inner class mViewHolder(mItem:View): RecyclerView.ViewHolder(mItem)
    {
        val add_btn: ImageButton
        val delete_btn:ImageButton
        val count_txt:TextView
        val result_txt:TextView
        var mListener:ListenerInterface

        var num=0
        init{
            add_btn=itemView.findViewById(R.id.Add_btn)
            delete_btn = itemView.findViewById(R.id.Delete_btn)
            count_txt=itemView.findViewById(R.id.Count_value)
            result_txt=itemView.findViewById(R.id.result_txt)
            mListener=context as ListenerInterface
            mItem.setOnClickListener {  }
            add_btn.setOnClickListener {
                items[num].count++
                items[num].result=items[num].count*items[num].value
                count_txt.text=items[num].count.toString()
                result_txt.text=items[num].result.toString()
                mListener.onAddingNewCar(items[num].value)
            }
            delete_btn.setOnClickListener {
                if(items[num].count>0)
                {
                    items[num].count--
                    items[num].result=items[num].count*items[num].value
                    count_txt.text=items[num].count.toString()
                    result_txt.text=items[num].result.toString()
                    mListener.onDeleteCar(items[num].value)
                }
            }
        }
    fun setData(it:item_data,pos:Int){
        itemView.Count_value.text=it.count.toString()
        itemView.Value_txt.text=it.value.toString()
        itemView.result_txt.text=it.result.toString()
    }
    }
}