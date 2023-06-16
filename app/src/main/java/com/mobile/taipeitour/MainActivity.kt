package com.mobile.taipeitour

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.mobile.taipeitour.views.AttractionFragment

class MainActivity : AppCompatActivity() {

    companion object{
        var main: MainActivity? = null
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main_container)

        this.also { main = it }

        val fg = AttractionFragment()
        addFragment(fg)
        replaceFragment(fg)
    }

    private fun addFragment(f: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.content_fragment, f)
        transaction.commit()
    }

    private fun replaceFragment(f : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_fragment, f)
        transaction.commit()
    }
}