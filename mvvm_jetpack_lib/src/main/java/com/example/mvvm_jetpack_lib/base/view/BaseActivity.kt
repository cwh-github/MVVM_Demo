package com.example.mvvm_jetpack_lib.base.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.mvvm_jetpack_lib.base.viewmodel.BaseViewModel
import com.example.mvvm_jetpack_lib.utils.LogUtils


/**
 * Description:
 * Date：2019/7/17-18:36
 * Author: cwh
 */
abstract class BaseActivity<VM : BaseViewModel<*>, V : ViewDataBinding> : BaseNorActivity() {
    /**
     * ViewModel
     */
    protected abstract val mViewModel: VM
    /**
     * 对应的ViewDataBinding
     */
    lateinit var mBinding: V
    /**
     * 需要加载的layout Id
     */
    abstract var layoutId: Int

    /**
     * ViewModel 在对应的DataBindingView中生成的ID
     */
    abstract var mViewModelVariableId: Int

    protected open var mProgressDialog: DialogFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //一些初始化参数和设置，在setContentView 前
        initParam()
        //初始化，获取DataBinding
        initDataBingView()
        //注册一些常用的LiveData 事件
        registerNormalUIChangeEvent()
        //一些数据和View的初始化
        initDataAndView()
        //注册一些根据ViewModel中数据变化而进行UI改变的Observer
        initViewObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mBinding.isInitialized) {
            mBinding.unbind()
        }
    }

    /**
     * 页面需要接受的一些参数  和 一些需要在setContentView 前初始化的方法
     */
    public open fun initParam() {

    }

    /**
     * 初始化获取ViewDataBing,setContentView
     */
    protected open fun initDataBingView() {
        mBinding = DataBindingUtil.setContentView<V>(this, layoutId)
        //将ViewModel 与 其ID进行关联
        mBinding.setVariable(mViewModelVariableId, mViewModel)
        //ViewModel 具有相关View的生命周期
        lifecycle.addObserver(mViewModel)
        //设置LifecycleOwner，让DataBinding能感知到LiveData数据的变化
        mBinding.lifecycleOwner = this
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
                onBackPressed()
            }
        })

        /**
         * 结束Activity 事件
         *
         * mViewModel.mUIChangeEvent.onFinishEvent.value=Event(Unit)
         */
        mViewModel.mUIChangeEvent.onFinishEvent.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                if (!this.isFinishing) {
                    finish()
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
                intent.setClass(this, clazz)
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
                intent.setClass(this, it)
                startActivity(intent)
            }
        })


    }


    private fun showDialog() {
        mProgressDialog?.let {
            if (it.dialog == null || !it.dialog.isShowing) {
                it.show(supportFragmentManager, "ProgressDialog")
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
    public open fun initDataAndView() {
    }

    /**
     * 一些通过监听ViewModel数据变化来改变View的Observer
     */
    protected open fun initViewObserver() {
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