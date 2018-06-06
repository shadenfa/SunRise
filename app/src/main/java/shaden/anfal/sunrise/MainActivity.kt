package shaden.anfal.sunrise

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.net.HttpURLConnection
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun GetTime(view:View){
        val city_name=city.text.toString()
        val url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22$city_name%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        Task().execute(url)
    }

    inner class Task:AsyncTask<String,String,String>(){

        override fun doInBackground(vararg p0: String?): String {
           try{
               val url=URL(p0[0])
               val urlconnect = url.openConnection() as HttpURLConnection
               urlconnect.connectTimeout=700

               val JStr= convert(urlconnect.inputStream)
               publishProgress(JStr)
           }catch(ex:Exception){

           }
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            val json=JSONObject(values[0])
            val query=json.getJSONObject("query")
            val results=query.getJSONObject("results")
            val channel=results.getJSONObject("channel")
            val astronomy=channel.getJSONObject("astronomy")
            val sunrise=astronomy.getString("sunrise")
            val sunset=astronomy.getString("sunset")
            rise.text="Sunrise is "+sunrise
            set.text="Sunset is "+sunset

        }


    }
fun convert(inputStream:InputStream):String{

    val read=BufferedReader(InputStreamReader(inputStream))
    var line:String
    var str:String=""

    try {
        do {

            line = read.readLine()
            if (line != null)
                str += line
        } while (line != null)
        read.close()
    } catch (ex:Exception){}

    return str;
}
}
