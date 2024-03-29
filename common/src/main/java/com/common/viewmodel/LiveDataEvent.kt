package com.common.viewmodel


class LiveDataEvent {

    companion object {

        /**
         * @param SUCCESS -通用数据库成功
         * @return
         */
        const val SUCCESS: Int = 0

        const val QuerySuccess: Int = 0x01

        const val QueryNameId: Int = 0x02

        const val QueryPatient: Int = 0x03

        const val QueryPatientNull: Int = 0x04


        /**
         * @param FAIL -通用错误
         * @return -
         */
        const val FAIL: Int = 0x88

    }

    var action = 0
    var any: Any? = null
    var anyOne: Any? = null


    //有参次构造器
    constructor(action:Int,any:Any){
        this.action=action
        this.any=any
    }

    //有双参次构造器
    constructor(action:Int,any:Any,anyOne: Any?){
        this.action=action
        this.any=any
        this.anyOne=anyOne
    }
}