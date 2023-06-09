package com.example.mytestapp

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var handler: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = DBHelper(this)

        MyToolBar().show(this,"Welcome to Pet Friends",false,true)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)



        val navigationView: NavigationView = findViewById(R.id.nav_view)

        val headerView: View = navigationView.getHeaderView(0)
        val textView: TextView = headerView.findViewById(R.id.nav_header_textView)
        val userLoggedInCredentials = handler.selectUserLoggedIn()
        if (userLoggedInCredentials != null) {
            val user = handler.selectUserData(userLoggedInCredentials.email, userLoggedInCredentials.password)
            if (user != null) {
                textView.text = user.name + " " + user.lastname

            }
        }

        navigationView.setNavigationItemSelectedListener(this)

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.nav_item_one ){
            val intent: Intent = Intent(this,StartupActivity::class.java)
            startActivity(intent)
        }
        if (item.itemId == R.id.nav_item_two){
            val intent: Intent = Intent(this,RequestsActivity::class.java)
            startActivity(intent)
        }
        if (item.itemId == R.id.nav_item_three){
            val intent: Intent = Intent(this,PetSittersList::class.java)
            startActivity(intent)
        }
        if (item.itemId == R.id.nav_item_four){
            val intent: Intent = Intent(this,PetListActivity::class.java)
            startActivity(intent)
        }
        if (item.itemId == R.id.nav_item_five){
            val intent: Intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {

    }
}