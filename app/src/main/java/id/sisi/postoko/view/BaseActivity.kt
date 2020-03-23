package id.sisi.postoko.view

import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.model.MessageEvent
import id.sisi.postoko.utils.MySession
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messageEvent: MessageEvent) {
        MySession.eventLogOut(this, messageEvent)
    }
}