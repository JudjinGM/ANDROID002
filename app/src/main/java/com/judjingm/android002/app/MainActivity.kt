package com.judjingm.android002.app

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.judjingm.android002.R
import com.judjingm.android002.common.ui.BaseActivity
import com.judjingm.android002.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
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

        requestNotificationPermission()


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("FCM TAG", token)
        })

    }

    private fun requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}
