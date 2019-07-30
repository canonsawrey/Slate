package com.example.mealslate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.example.mealslate.list.ListFragment
import com.example.mealslate.plan.PlanFragment
import com.example.mealslate.preferences.PreferencesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private var currentTab: BottomNavigationTab? = null
    //private val actions = PublishRelay.create<Action>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        determineCurrentTab(savedInstanceState)
        initNavigationBar()
    }

    private fun determineCurrentTab(savedInstanceState: Bundle?) {
        val id = savedInstanceState?.getInt(EXTRA_SELECTED_TAB)
            ?: intent.getIntExtra(EXTRA_SELECTED_TAB, R.id.navigation_list)

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

        fun launch(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

}
