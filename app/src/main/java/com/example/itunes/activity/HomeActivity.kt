package com.example.itunes.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.itunes.R
import com.example.itunes.adapter.ListRecyclerAdapter
import com.example.itunes.model.Track
import com.example.itunes.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.collections.HashMap

class HomeActivity : AppCompatActivity() {

    lateinit var recyclerList: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var searchArtist: RelativeLayout
    lateinit var searchView: SearchView
    
    val trackInfoList = arrayListOf<Track>()

    lateinit var recyclerAdapter: ListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerList = findViewById(R.id.recyclerList)
        layoutManager = LinearLayoutManager(this@HomeActivity)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        searchArtist = findViewById(R.id.search_artist)
        searchView = findViewById(R.id.searchView)
        searchArtist.visibility = View.VISIBLE
        recyclerList.visibility = View.INVISIBLE
        progressLayout.visibility = View.INVISIBLE
        var search: String=""


        if (ConnectionManager().checkConnectivity(this@HomeActivity))
        {
            searchView.queryHint = "Search Tracks by Artist Name"
            searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    query?.toLowerCase(Locale.ROOT)
                    query?.replace(" ","+",false)
                    search = query.toString()
                    if(search.equals(" "))
                    {
                        recyclerList.visibility = View.INVISIBLE
                        Toast.makeText(this@HomeActivity, "Please Enter Name Of the Artist!", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        progressLayout.visibility = View.VISIBLE
                        trackInfoList.clear()
                        val queue = Volley.newRequestQueue(this@HomeActivity)
                        val url = "https://itunes.apple.com/search?term=${search}&entity=musicTrack&limit=25"

                        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener{


                            // Here we will handle the response
                            try {

                                val success=it.getInt("resultCount")
                                progressLayout.visibility = View.GONE

                                if (success>0){
                                    recyclerList.visibility=View.VISIBLE

                                    val data = it.getJSONArray("results")
                                    for (i in 0 until data.length()){
                                        val trackJsonObject = data.getJSONObject(i)

                                        val TrackObject = Track(
                                                trackJsonObject.getInt("trackId"),
                                                trackJsonObject.getString("trackName"),
                                                trackJsonObject.getInt("trackTimeMillis"),
                                                trackJsonObject.getString("primaryGenreName"),
                                                trackJsonObject.getString("artistName"),
                                                trackJsonObject.getInt("artistId")
                                        )


                                        trackInfoList.add(TrackObject)


                                        recyclerAdapter = ListRecyclerAdapter(this@HomeActivity,trackInfoList)

                                        recyclerList.adapter = recyclerAdapter
                                        recyclerList.layoutManager = layoutManager



                                    }

                                } else {
                                    recyclerList.visibility=View.INVISIBLE
                                    Toast.makeText(this@HomeActivity, "Artist Not Found!", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(this@HomeActivity, "Some unexpected error occurred!", Toast.LENGTH_SHORT).show()
                            }


                        }, Response.ErrorListener{
                            //Here we will handle the errors
                            Toast.makeText(this@HomeActivity, "Volley error occurred!", Toast.LENGTH_SHORT).show()

                        }){
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                return headers
                            }
                        }

                        queue.add(jsonObjectRequest)
                    }

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })




        }
        else
        {
            val dialog = AlertDialog.Builder(this@HomeActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this@HomeActivity.finish()
            }

            dialog.setNegativeButton("Exit") {text, listener ->
                ActivityCompat.finishAffinity(this@HomeActivity)
            }
            dialog.create()
            dialog.show()
        }






    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}