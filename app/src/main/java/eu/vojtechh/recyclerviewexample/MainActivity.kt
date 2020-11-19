package eu.vojtechh.recyclerviewexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        val sampleAdapter = SampleAdapter()
        recyclerView.adapter = sampleAdapter
        sampleAdapter.submitList(getSampleItems())
    }

    // The images are very high resolution, this is not ideal and makes the app load for a few seconds
    // at the start
    private fun getSampleItems() = listOf(
        SampleModel(1, "The Grand Canyon", "At least it looks like that", R.mipmap.one),
        SampleModel(2, "People on a beach", "Having fun and all that", R.mipmap.two),
        SampleModel(3, "Christmas", "Winter is coming", R.mipmap.three),
        SampleModel(4, "A stormy night", "That is a lot of volts", R.mipmap.four),
        SampleModel(5, "Not a ThinkPad", "Still a great laptop", R.mipmap.five),
        SampleModel(6, "A puppy", "With curly hair", R.mipmap.six),
        SampleModel(7, "Road tripping", "Looks like a nice car", R.mipmap.seven),
        SampleModel(8, "A desert", "Not like a cake, but you know", R.mipmap.eight),
    )
}