package com.example.retromeme

import android.content.Intent
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val baseurl="https://meme-api.com/"

class MainActivity : AppCompatActivity() {
    var url:String?=null
    var imgurl1:String?=null
    var imgurl:String?=null
    var flag:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme(flag)

    }

    fun loadMeme( flag:Int)
    {
        val retroFit=Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseurl).build().create(getdata::class.java)

        val RetroData=retroFit.get()

        RetroData.enqueue(object :Callback<DataItem>{
            override fun onResponse(call: Call<DataItem>, response: Response<DataItem>) {
               val Response=response.body()
                imgurl= Response?.url

                if(flag==1)
                {
                    imgurl1=url
                    url=imgurl
                }

                else if(flag==0)
                {
                    url=imgurl1
                }
                //textView.text=url
                Glide.with(this@MainActivity).load(url).listener(object :RequestListener<Drawable>
                {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                       progressBar.visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE
                        return false
                    }

                }).into(memeimage)

            }

            override fun onFailure(call: Call<DataItem>, t: Throwable) {
                Toast.makeText(this@MainActivity,"someThing Wrong!!!",Toast.LENGTH_LONG)
                Log.d("MainActivity","Error"+t.message)


            }

        })


    }

    fun share(view: View) {
val intent=Intent(Intent.ACTION_SEND)
        intent.type="plain/text"
        intent.putExtra(Intent.EXTRA_TEXT,"This the meme I got from redit$url")
        val choser=Intent.createChooser(intent,"Share this meme")
        startActivity(choser)
    }
    fun previous(view: View) {
loadMeme(0)
    }
    fun next(view: View) {
   loadMeme(1)
    }
}