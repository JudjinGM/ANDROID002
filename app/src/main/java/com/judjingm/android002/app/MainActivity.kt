package com.judjingm.android002.app

import android.view.View
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseActivity
import com.judjingm.android002.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun configureViews() {
        val navigationView = binding.navigationView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController
        navigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeScreenFragment, R.id.searchFragment, R.id.profileFragment -> {
                    if (binding.navigationView.visibility == View.GONE) {
                        binding.navigationView.visibility = View.VISIBLE
                        binding.divider.visibility = View.VISIBLE
                        binding.navigationView.animation =
                            AnimationUtils.loadAnimation(this, R.anim.slide_in_up)
                        binding.navigationView.animate()
                    }
                }

                else -> {
                    if (binding.navigationView.visibility == View.VISIBLE) {
                        binding.navigationView.animation =
                            AnimationUtils.loadAnimation(this, R.anim.slide_out_down)
                        binding.navigationView.animate()
                        binding.navigationView.visibility = View.GONE
                        binding.divider.visibility = View.GONE
                    }
                }
            }
        }
    }
}
