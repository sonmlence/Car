package com.example.car.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.car.R
import com.example.car.pref.Prefs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var pref: Prefs
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        pref = Prefs(this)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHost.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_host)

        val startDestination = when {
            !pref.isOnBoardShown() -> R.id.onBoardFragment
            pref.isUserLoggedIn() || auth.currentUser != null -> R.id.notesFragment
            else -> R.id.authFragment
        }

        navGraph.setStartDestination(startDestination)
        navController.graph = navGraph
    }

    fun logoutAndExit() {
        FirebaseAuth.getInstance().signOut()
        val gso = GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
        gso.signOut().addOnCompleteListener {
            pref.setUserLoggedIn(false)
            finishAffinity()
        }
    }
}
