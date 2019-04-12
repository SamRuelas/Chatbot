package android.itesm.edu.chatbotsam

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val human = ChatUser(
            1,
            "Sam",
            BitmapFactory.decodeResource(resources,
                R.drawable.ic_account_circle)
        )


        val agent = ChatUser(
            2,
            "FRIDAY",
            BitmapFactory.decodeResource(resources,
                R.drawable.ic_account_circle)
        )

        FuelManager.instance.baseHeaders = mapOf( "Authorization" to "Bearer $ACCESS_TOKEN" )
        FuelManager.instance.basePath =  "https://api.dialogflow.com/v1/"

        FuelManager.instance.baseParams = listOf(
            "v" to "20170712",
            "sessionId" to UUID.randomUUID(),
            "lang" to "en"
        )

        my_chat_view.setOnClickSendButtonListener(
            View.OnClickListener {
                my_chat_view.send(
                    Message.Builder()
                        .setUser(human)
                        .setText(my_chat_view.inputText)
                        .build()
                )
                //ADD the next section
                Fuel.get("/query",
                    listOf("query" to my_chat_view.inputText))
                    .responseJson { _, _, result ->
                        val reply = result.get().obj().getJSONObject("result").getJSONObject("fulfillment").get("speech").toString()
                        //ADD the next
                        my_chat_view.send(Message.Builder()
                            .setRight(true)
                            .setUser(agent)
                            .setText(reply)
                            .build()
                        )
                    }



            })
    }
    companion object {
        private const val ACCESS_TOKEN = "62bb2b93811e4ad9b67de541b41f808f"

    }

}
