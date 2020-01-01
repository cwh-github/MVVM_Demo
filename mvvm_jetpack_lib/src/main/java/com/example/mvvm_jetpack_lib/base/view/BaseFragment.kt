package com.example.mvvm_jetpack_lib.base.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.mvvm_jetpack_lib.base.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * Description:Fragment,对于不需要用到DataBinding的界面，直接写其父类
 * DataViewBinding即可，这样不会使用DataBinding,而会直接使用inflate
 * ,此时mViewModelVariableId=-1即可
 * 对于不需要自己定义ViewModel的界面，直接使用NoViewModel即可(还是可以使用ViewModel的
 * 一些基本方法，如跳转界面，显示加载对话框)
 * Date：2019/7/17-18:35
 * Author: cwh
 */
abstract class BaseFragment<VM : BaseViewModel<*>, V : ViewDataBinding> : BaseNorFragment() {

    /**
     * ViewModel
     */
    abstract val mViewModel: VM

    /**
     * DataBinding
     */
    lateinit var mBinding: V

    /**
     * 需要显示的布局的ID
     */
    abstract val layoutId: Int

    /**
     * ViewModel在DataBinding 中的ID
     * 若为不想使用ViewDataBinding的Activity,mViewModelVariableId=-1即可
     * (且指定的ViewDataBinding类型为ViewDataBinding)
     */
    abstract var mViewModelVariableId: Int

    /**
     * 加载对话框
     */
    protected open var mProgressDialog: DialogFragment? = null

    private lateinit var activity: Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context?.let {
            activity=context as Activity
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val cls = getParamsType()?.actualTypeArguments?.get(1) as? Class<*>
        //ViewBinding是ViewDataBinding的子类
        return if (cls != null && ViewDataBinding::class.java != cls
            && ViewDataBinding::class.java.isAssignableFrom(cls)
        ) {
            mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            mBinding.root
        }else{
            inflater.inflate(layoutId,container,false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mBinding.isInitialized) {
            mBinding.unbind()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataBindingView()
        //注册一些常用的LiveData 事件
        registerNormalUIChangeEvent()
        //一些数据和View的初始化
        initDataAndView()
        //注册一些根据ViewModel中数据变化而进行UI改变的Observer
        initViewObserver()
    }

    private fun getParamsType(): ParameterizedType? {
        val type = javaClass.genericSuperclass
        return if (type is ParameterizedType) {
            type
        } else null
    }

    /**
     * 将相关数据设置到DataBingding 中
     */
    protected open fun initDataBindingView() {
        val cls = getParamsType()?.actualTypeArguments?.get(1) as? Class<*>
        if (cls != null && ViewDataBinding::class.java != cls
            && ViewDataBinding::class.java.isAssignableFrom(cls)
        ) {
            mBinding.setVariable(mViewModelVariableId, mViewModel)
            mBinding.lifecycleOwner = this.viewLifecycleOwner
            lifecycle.addObserver(mViewModel)
        }
    }

    /**
     * 注册一些通用的View 改变的LiveData
     */
    protected open fun registerNormalUIChangeEvent() {
        /**
         * 加载对话框是否显示
         *
         *  通过 mViewModel.mUIChangeEvent.isShowLoadView.value=Event(true) 来改变
         *
         *  dialog 是否显示 true 显示  false 不显示
         *
         */
        mViewModel.mUIChangeEvent.isShowLoadView.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                if (it) {
                    showDialog()
                } else {
                    dismissDialog()
                }
            }
        })

        /**
         * 触发返回事件
         *
         * mViewModel.mUIChangeEvent.onBackEvent.value=Event(Unit)
         *
         */
        mViewModel.mUIChangeEvent.onBackEvent.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                if(::activity.isInitialized){
                    activity.onBackPressed()
                }
            }
        })

        /**
         * 结束Activity 事件
         *
         * mViewModel.mUIChangeEvent.onFinishEvent.value=Event(Unit)
         */
        mViewModel.mUIChangeEvent.onFinishEvent.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                if(::activity.isInitialized){
                    if (!activity.isFinishing) {
                        activity.finish()
                    }
                }


            }
        })

        /**
         * 跳转到指定的Activity
         *
         * val intent=Intent(this,MainActivity::class.java)
        mViewModel.mUIChangeEvent.startActivityEventWithIntent.value=Event(intent)
         *
         */
        mViewModel.mUIChangeEvent.startActivityEventWithIntent.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                startActivity(it)
            }
        })

        /**
         * 跳转到其他Activity
         *
         *
         */
        mViewModel.mUIChangeEvent.startActivityEventWithMap.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                val intent = Intent()
                val clazz = it[BaseViewModel.NormalUIChangeLiveData.clazz] as Class<*>
                val bundle = it[BaseViewModel.NormalUIChangeLiveData.bundle] as Bundle?
                intent.setClass(mActivity, clazz)
                bundle?.let {
                    intent.putExtras(bundle)
                }
                startActivity(intent)
            }
        })

        /**
         * 跳转Activity
         */
        mViewModel.mUIChangeEvent.startActivityEventWithClass.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                val intent = Intent()
                intent.setClass(mActivity, it)
                startActivity(intent)
            }
        })


    }


    private fun showDialog() {
        mProgressDialog?.let {
            if (it.dialog == null || !it.dialog.isShowing) {
                it.show(childFragmentManager, "ProgressDialog")
            }
        }
    }

    private fun dismissDialog() {
        mProgressDialog?.let {
            if (it.dialog != null && it.dialog.isShowing) {
                it.dismiss()
            }
        }

    }

    /**
     * 初始化一些初始数据和View
     */
    protected open fun initDataAndView() {
    }

    /**
     * 一些通过监听ViewModel数据变化来改变View的Observer
     */
    protected open  fun initViewObserver() {
    }

    /**
     * 创建ViewModel，由于现在定义的ViewModel含有Repository，
     * 所以一般能调用该方法生成ViewModel，需要自己创建Factory来创建
     *
     * 此方法调用的是系统默认的{@link AndroidViewModelFactory}
     *
     * 此Factory默认的是找一个以application为参数的构造方法构造实例，如果此构造方法不存在，会报错，
     * 就算生成成功，此时Repository为null,不能调用Repository的方法获取数据
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T : ViewModel> createViewModel(fragment: Fragment, cls: Class<T>): T {
        return ViewModelProviders.of(fragment).get(cls)
    }

    /**
     * 创建ViewModel，由于现在定义的ViewModel含有Repository，
     * 所以一般能调用该方法生成ViewModel，需要自己创建Factory来创建
     *
     * 此方法调用的是系统默认的{@link AndroidViewModelFactory}
     *
     * 此Factory默认的是找一个以application为参数的构造方法构造实例，如果此构造方法不存在，会报错，
     * 就算生成成功，此时Repository为null,不能调用Repository的方法获取数据
     *
     * @param cls
     * @param <T>
     * @return</T>
     * */
    fun <T : ViewModel> createViewModel(activity: FragmentActivity, cls: Class<T>): T {
        return ViewModelProviders.of(activity).get(cls)
    }


}