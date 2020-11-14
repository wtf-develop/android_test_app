package ru.wtfdev.kitty

import android.os.Bundle
import ru.wtfdev.kitty.utils.BaseActivty

class DialogActivity(override var startFragment: String) : BaseActivty() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
    }
}