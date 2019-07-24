package com.example.mvvm_jetpack_lib.base.command;


/**
 * About : kelin的ReplyCommand
 * 执行的命令回调, 用于ViewModel与xml之间的数据绑定
 */
public class BindingComWithParams<T> {
    //执行带参数的命令
    private BindingConsumer<T> consumer;
    //是否执行命令
    private BindingFunction<Boolean> canExecute0;



    /**
     * @param execute 带泛型参数的命令绑定
     */
    public BindingComWithParams(BindingConsumer<T> execute) {
        this.consumer = execute;
    }


    /**
     * @param execute     带泛型参数触发命令
     * @param canExecute0 true则执行,反之不执行
     */
    public BindingComWithParams(BindingConsumer<T> execute, BindingFunction<Boolean> canExecute0) {
        this.consumer = execute;
        this.canExecute0 = canExecute0;
    }



    /**
     * 执行带泛型参数的命令
     *
     * @param parameter 泛型参数
     */
    public void execute(T parameter) {
        if (consumer != null && canExecute0()) {
            consumer.call(parameter);
        }
    }

    /**
     * 是否需要执行
     *
     * @return true则执行, 反之不执行
     */
    private boolean canExecute0() {
        if (canExecute0 == null) {
            return true;
        }
        return canExecute0.call();
}
}
