package com.example.slate.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.IdRes
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.example.slate.R
import com.example.slate.list.ListFragment
import com.example.slate.plan.PlanFragment
import com.example.slate.preferences.PreferencesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private var currentTab: BottomNavigationTab? = null
    //private val actions = PublishRelay.create<Action>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        determineCurrentTab(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavigationBar()
    }

    /**
     * Configures the bottom navigation bar listener to open the correct screen based on which
     * BottomNavTab is selected.
     */
    private fun initNavigationBar() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val tab = BottomNavigationTab.fromId(item.itemId)
            selectTab(tab)
            true
        }

        bottomNavigation.selectedItemId = currentTab?.id ?: R.id.navigation_list
    }

    private fun determineCurrentTab(savedInstanceState: Bundle?) {
        val id = savedInstanceState?.getInt(EXTRA_SELECTED_TAB)
            ?: intent.getIntExtra(
                EXTRA_SELECTED_TAB,
                R.id.navigation_list
            )

        currentTab = BottomNavigationTab.fromId(id)
    }

    /**
     * Opens tab fragment
     *
     * @param tab whichever tab has been selected and must be opened.
     */
    private fun selectTab(tab: BottomNavigationTab) {
        val fragment = supportFragmentManager.findFragmentByTag(tab.toString())
            ?: tab.newFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tab.toString())
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val bundle = outState.apply {
            putInt(EXTRA_SELECTED_TAB, bottomNavigation.selectedItemId)
        }
        super.onSaveInstanceState(bundle)
    }

    fun itemClicked(uniqueId: Long) {
        println(uniqueId)
    }


    enum class BottomNavigationTab(@IdRes val id: Int, val newFragment: () -> Fragment) {
        SERVICES(R.id.navigation_plan, { PlanFragment() }),
        ACTIVITY(R.id.navigation_list, { ListFragment() }),
        SETTINGS(R.id.navigation_preferences, { PreferencesFragment() });

        companion object {
            fun fromId(@IdRes id: Int): BottomNavigationTab {
                for (tab in values()) {
                    if (id == tab.id) {
                        return tab
                    }
                }

                throw IllegalArgumentException("No tab with id of $id found.")
            }
        }
    }

    companion object {
        private const val EXTRA_SELECTED_TAB = "extra:selected_tab"

        fun launch(activity: Activity, @IdRes tabIdRes: Int = R.id.navigation_list) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra(EXTRA_SELECTED_TAB, tabIdRes)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        fun launch(activity: Activity, context: Context) {
            val bundle = ActivityOptionsCompat.makeCustomAnimation(context,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle()
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent, bundle)
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

}
