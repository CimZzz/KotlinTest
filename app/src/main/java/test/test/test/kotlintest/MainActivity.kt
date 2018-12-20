package test.test.test.kotlintest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        header.refreshListener = object: Header.OnRefreshListener {
            override fun onRefreshHeader(distance: Float): Float = 400f
        }
        contentList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        contentList.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item, p0, false)) {}
            }

            override fun getItemCount(): Int = 50

            override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
                (p0.itemView as TextView).text = p1.toString()
            }
        }
    }

}
