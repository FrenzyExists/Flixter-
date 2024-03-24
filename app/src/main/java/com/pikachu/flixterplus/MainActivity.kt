package com.pikachu.flixterplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.pikachu.flixterplus.R.id
import com.pikachu.flixterplus.databinding.ActivityMainBinding

//https://api.themoviedb.org/3/movie/now_playing?api-key=a07e22bc18f5cb106bfe4cc1f83ad8ed
//https://api.themoviedb.org/3/movie/now_playing?&api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed
const val BASE_URL = "https://api.themoviedb.org/3/movie/"
const val SEARCH_API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
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
                id.explore -> replaceFragment(ExploreFragment())
                id.search -> replaceFragment(SearchFragment())
                id.wishlist -> replaceFragment(WishlistFragment())
                id.about -> replaceFragment(AboutFragment())
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