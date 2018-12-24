package test.test.test.kotlintest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.ui_product_share.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_product_share)

        sharePictureSelector.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        sharePictureSelector.adapter = object: RecyclerView.Adapter<RecyclerView.ViewHolder>()          {

        }
//
//        header.refreshListener = object: Header.OnRefreshListener {
//            override fun onRefreshHeader(distance: Float): Float = 400f
//        }
//        contentList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        contentList.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
//                return object : RecyclerView.ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item, p0, false)) {}
//            }
//
//            override fun getItemCount(): Int = 50
//
//            override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
//                (p0.itemView as TextView).text = p1.toString()
//                p0.itemView.setOnClickListener {
//                    header.completed()
//                }
//            }
//        }
    }

}
