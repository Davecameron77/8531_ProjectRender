package ca.tinyweb.a8531_projectrender

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.renderscript.Toolkit
import java.lang.System.currentTimeMillis
import java.util.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var mBitmapIn: Bitmap
    private lateinit var mBitmapOut: Bitmap
    private val ITERATIONS = 100
    private val NUM_THREADS = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val executor = Executors.newFixedThreadPool(NUM_THREADS)

        val startTimeInMills = currentTimeMillis()
        for (i in 1..ITERATIONS) {
            val worker = Runnable {
                val resId = getResId()
                mBitmapIn = loadBitmap(resId)!!
                mBitmapOut = Bitmap.createBitmap(mBitmapIn.width, mBitmapIn.height, mBitmapIn.config)

                findViewById<ImageView>(R.id.displayin).setImageBitmap(mBitmapIn)
                mBitmapOut = Toolkit.colorMatrix(mBitmapIn, Toolkit.greyScaleColorMatrix)
                findViewById<ImageView>(R.id.displayout).setImageBitmap(mBitmapOut)
            }
            executor.execute(worker)
        }

        executor.shutdown()
        while (!executor.isTerminated) {

        }

        val endTimeInMills = currentTimeMillis()
        val executionTime = endTimeInMills - startTimeInMills


        Toast.makeText(
            this,
            "Execution took $executionTime ms for $ITERATIONS iterations", Toast.LENGTH_LONG
        ).show()
    }

    private fun loadBitmap(resource: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeResource(resources, resource, options)
    }

    private fun getResId(): Int {
        when (Random().nextInt(4 - 1 + 1) + 1) {
            1 -> return R.drawable.data
            2 -> return R.drawable.data1
            3 -> return R.drawable.data2
            4 -> return R.drawable.data3
        }
        return R.drawable.data
    }
}