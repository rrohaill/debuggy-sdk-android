package dev.rohail.debuggy.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabsAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(
    fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val mFragments: MutableList<Fragment> = mutableListOf()
    private val mTitles: MutableList<String> = mutableListOf()

    override fun getCount(): Int = mFragments.size

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getPageTitle(position: Int): CharSequence? = mTitles.getOrNull(position)

    fun addFragment(fragment: Fragment, fragmentTitle: String) {
        mFragments.add(fragment)
        mTitles.add(fragmentTitle)
    }

}