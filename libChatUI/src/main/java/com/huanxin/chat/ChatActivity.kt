package com.huanxin.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import cn.hadcn.keyboard.R
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.modules.chat.EaseChatFragment
import com.hyphenate.easeui.widget.EaseTitleBar
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : FragmentActivity() {
    lateinit var chatFragment: EaseChatFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //EaseIM初始化
        if(EaseIM.getInstance().init(applicationContext, EMOptions().apply {
                    appKey = "1101210416153550"
//                    setIMServer("")
                })){
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(true);
            //EaseIM初始化成功之后再去调用注册消息监听的代码 ...
        }

        chatFragment = EaseChatFragment()
        //pass parameters to chat fragment
        chatFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction().add(R.id.container, chatFragment).commit()

        title_bar.setLeftImageResource(R.drawable.default_right_icon)
        title_bar.setOnBackPressListener {
            finish()
        }
        title_bar.postDelayed({
            chatFragment.chatLayout.apply {
                chatInputMenu?.chatExtendMenu?.apply {
                    clear()
                    setEaseChatExtendMenuItemClickListener { itemId, view ->

                    }
                }
            }
        }, 200)
    }

    companion object{
        fun open(activity: Activity?, userId: String?){
            activity?.startActivity(Intent(activity, ChatActivity::class.java).putExtra("conversationId", userId).putExtra("chatType", 1))
        }
    }
}