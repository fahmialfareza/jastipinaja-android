package com.dinokeylas.jastipinaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.util.*

class TransactionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        val tabLayout: TabLayout = view.findViewById(R.id.tabs)
        val viewPager: ViewPager = view.findViewById(R.id.viewpager)

        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(activity?.supportFragmentManager!!)
        adapter.addFragment(TransactionListFragment(), "Transaksi")
        adapter.addFragment(MessageListFragment(), "Pesan")
        viewPager.adapter = adapter
    }

    internal class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, tittle: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(tittle)
        }

        @Nullable
        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }
}
