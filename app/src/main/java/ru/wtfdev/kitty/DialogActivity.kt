package ru.wtfdev.kitty

import android.content.Intent
import android.os.Bundle
import ru.wtfdev.kitty._navigation.implementation.BaseActivty
import ru.wtfdev.kitty.add_link.AddLinkView


class DialogActivity : BaseActivty() {
    override var startFragment: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        if (Intent.ACTION_SEND == intent?.action) {
            if ("text/plain" == intent.type) {
                startFragment = AddLinkView::class.qualifiedName ?: ""
            }
        } else {
            startFragment = intent?.getStringExtra("startFragment") ?: ""
        }
        if (startFragment.isEmpty()) {
            finish()
            return
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
    }
}