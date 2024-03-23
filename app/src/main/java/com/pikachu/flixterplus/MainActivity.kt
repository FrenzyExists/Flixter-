package com.pikachu.flixterplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.pikachu.flixterplus.R.id
import com.pikachu.flixterplus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ExploreFragment())

        binding.bottomNavigationView2.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.explore -> replaceFragment(ExploreFragment())
                R.id.search -> replaceFragment(SearchFragment())
                R.id.wishlist -> replaceFragment(WishlistFragment())
                R.id.about -> replaceFragment(AboutFragment())

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val supportFragmentManager = supportFragmentManager
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(id.movieFrame, fragment).commit()
    }
}