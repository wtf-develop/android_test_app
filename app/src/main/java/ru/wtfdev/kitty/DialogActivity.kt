package ru.wtfdev.kitty

import android.os.Bundle
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty.utils.BaseActivty


class DialogActivity : BaseActivty() {
    override var startFragment: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        startFragment = intent?.getStringExtra("startFragment") ?: ""
        super.onCreate(savedInstanceState)
        if (startFragment.isEmpty()) {
            finish()
            return
        }
        DaggerComponent.create().inject(this)
        setContentView(R.layout.activity_dialog)
    }
}