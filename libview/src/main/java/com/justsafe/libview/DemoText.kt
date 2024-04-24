package com.justsafe.libview

class DemoText {

    fun demo() {
        /*LBus.init(this)

        lifecycleOwner.bindLBus<LBusEvent>(Lifecycle.State.RESUMED, arrayOf("action1", "action2")) {
            when(it.action) {
                "action1" -> {
                    Log.e("LBus", "action1 "+it.intent.getStringExtra("name"))
                }
                "action2" -> {
                    Log.e("LBus", "action2 "+it.intent.getStringExtra("name"))
                }
            }
        }

        bind.bt.setOnClickListener {
            LBusEvent(initIntent = Intent("action1").apply {
                putExtra("name", "张三")
            }).send()

            LBusEvent(initIntent = Intent("action2").apply {
                putExtra("name", "李四")
            }).send()
        }*/
    }
}