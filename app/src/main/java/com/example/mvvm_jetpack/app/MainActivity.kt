package com.example.mvvm_jetpack.app

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack.app.home.HomeFragment
import com.example.mvvm_jetpack.app.home.recommend.RecommendFragment
import com.example.mvvm_jetpack.app.hot.HotFragment
import com.example.mvvm_jetpack.app.mine.MineFragment
import com.example.mvvm_jetpack_lib.base.view.BaseNorActivity
import com.example.mvvm_jetpack_lib.utils.find
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : BaseNorActivity() {

    private val mFragments: ArrayList<Fragment> = ArrayList<Fragment>()

    private var mSelectIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTabItem()

        initFragment()

        addListener()
    }

    private fun addListener() {
        tab.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(t: TabLayout.Tab?) {
                selectFragment(t?.position ?: 0)
            }

        })
    }

    private fun initFragment() {
        mFragments.add(HomeFragment.newInstance("Home"))
        mFragments.add(HotFragment.newInstance("Hot"))
        mFragments.add(MineFragment.newInstance("Mine"))
        supportFragmentManager.beginTransaction().replace(
            R.id.mFrame,
            mFragments[0], mFragments[0]::class.java.simpleName
        )
            .commitNowAllowingStateLoss()
    }

    private fun selectFragment(index: Int) {
        if (index == mSelectIndex) {
            return
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(mFragments[mSelectIndex])
        val selectFragment = mFragments[index]
        if (!selectFragment.isAdded) {
            transaction.add(R.id.mFrame, selectFragment, selectFragment::class.java.simpleName)
                .show(selectFragment)
        } else {
            transaction.show(selectFragment)
        }
        transaction.commitAllowingStateLoss()
        mSelectIndex = index
    }

    private fun initTabItem() {
        val tabHome = LayoutInflater.from(this.applicationContext)
            .inflate(R.layout.tab_item, null)
        val img = tabHome.find<ImageView>(R.id.mTabImg)
        val text = tabHome.find<TextView>(R.id.mTabTv)
        img.setBackgroundResource(R.drawable.tab_img_home)
        text.text = "Home"
        //hot
        val tabHot = LayoutInflater.from(this.applicationContext)
            .inflate(R.layout.tab_item, null)
        val imgHot = tabHot.find<ImageView>(R.id.mTabImg)
        val textHot = tabHot.find<TextView>(R.id.mTabTv)
        imgHot.setBackgroundResource(R.drawable.tab_img_hot)
        textHot.text = "Hot"
        //mine
        val tabMine = LayoutInflater.from(this.applicationContext)
            .inflate(R.layout.tab_item, null)
        val imgMine = tabMine.find<ImageView>(R.id.mTabImg)
        val textMine = tabMine.find<TextView>(R.id.mTabTv)
        imgMine.setBackgroundResource(R.drawable.tab_img_mine)
        textMine.text = "Mine"

        tab.addTab(tab.newTab().setCustomView(tabHome))
        tab.addTab(tab.newTab().setCustomView(tabHot))
        tab.addTab(tab.newTab().setCustomView(tabMine))

    }

}

