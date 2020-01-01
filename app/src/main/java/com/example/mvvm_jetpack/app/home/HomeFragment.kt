package com.example.mvvm_jetpack.app.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack.app.home.follow.FollowFragment
import com.example.mvvm_jetpack.app.home.recommend.RecommendFragment
import com.example.mvvm_jetpack_lib.base.view.BaseNorFragment
import com.example.mvvm_jetpack_lib.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Description:
 * Date：2019/7/24-11:43
 * Author: cwh
 */
class HomeFragment : BaseNorFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    private val titles: Array<String> = arrayOf("推荐", "动态")
    private val mFragments: Array<Fragment> = arrayOf(
        RecommendFragment.newInstance("Recommend"),
        FollowFragment.newInstance("follow")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSearchView.setIconifiedByDefault(true)
        mSearchView.onActionViewExpanded()
        mSearchView.queryHint = "搜索你想要的内容"
        mSearchView.clearFocus()
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                ToastUtils.showToast(mActivity, "$query")
                mSearchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        mViewPager.adapter = MyAdapter(childFragmentManager, mFragments)
        mTabView.setupWithViewPager(mViewPager, true)


    }


    companion object {
        fun newInstance(tag: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            fragment.arguments = bundle
            return fragment
        }

    }


    inner class MyAdapter(fm: FragmentManager, private val mFragments: Array<Fragment>) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int = mFragments.size


        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

    }


}