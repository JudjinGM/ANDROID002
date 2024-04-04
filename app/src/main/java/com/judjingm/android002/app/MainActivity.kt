package com.judjingm.android002.app

import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import app.cashadvisor.common.utill.extensions.logDebugMessage
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseActivity
import com.judjingm.android002.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

//    override fun onNewIntent(intent: Intent?) {
//        logDebugMessage("onNewIntent intent: $intent")
//
//        val appLinkAction: String? = intent?.action
//
//        logDebugMessage("onNewIntent appLinkAction: $appLinkAction")
//
//        val appLinkData: Uri? = intent?.data
//
//        logDebugMessage("onNewIntent appLinkData: $appLinkData")
//
//        super.onNewIntent(intent)
//    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logDebugMessage("onNewIntent intent: $intent")
        findNavController(R.id.rootFragmentContainerView).handleDeepLink(intent)

    }

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
