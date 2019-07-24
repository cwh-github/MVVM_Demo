package com.example.mvvm_jetpack_lib.base.command


/**
 * About : kelin的ReplyCommand
 * 执行的命令回调, 用于ViewModel与xml之间的数据绑定
 */
class BindingCommand{
    //执行不带参数的命令
    private var execute: BindingAction? = null
    //是否执行
    private var canExecute0: BindingFunction<Boolean>? = null

    constructor(execute:BindingAction) {
        this.execute = execute
    }



    /**
     * @param execute     触发命令
     * @param canExecute0 true则执行,反之不执行
     */
    constructor(execute: BindingAction, canExecute0: BindingFunction<Boolean>) {
        this.execute = execute
        this.canExecute0 = canExecute0
    }



    /**
     * 执行BindingAction命令
     */
    fun execute() {
        if (execute != null && canExecute0()) {
            execute!!.call()
        }
    }



    /**
     * 是否需要执行
     *
     * @return true则执行, 反之不执行
     */
    private fun canExecute0(): Boolean {
        return if (canExecute0 == null) {
            true
        } else canExecute0!!.call()
    }
}
