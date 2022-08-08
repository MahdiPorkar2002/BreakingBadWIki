package com.example.breakingbadwiki.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.breakingbadwiki.R
import com.example.breakingbadwiki.data.Person
import com.example.breakingbadwiki.databinding.ActivityMainBinding
import com.example.breakingbadwiki.databinding.DialogSignUpBinding
import com.example.breakingbadwiki.fragment.ExploreFragment
import com.example.breakingbadwiki.fragment.PhotographerFragment
import com.example.breakingbadwiki.fragment.ProfileFragment
import com.example.breakingbadwiki.fragment.TrendFragment
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)
        val actionBarDrawerToggle =
            ActionBarDrawerToggle(
                this, binding.drawerLayoutMain, binding.toolbarMain,
                R.string.openDrawer, R.string.closeDrawer
            )
        actionBarDrawerToggle.syncState()
        binding.drawerLayoutMain.addDrawerListener(actionBarDrawerToggle)

        binding.navigationViewMain.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.menu_writer -> {
                    binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
                    val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    dialog.titleText = "Alert!"
                    dialog.confirmText = "Confirm"
                    dialog.cancelText = "Cancel"
                    dialog.contentText = "Wanna be a writer?"

                    dialog.setCancelClickListener {
                        dialog.dismiss()
                    }

                    dialog.setConfirmClickListener {
                        dialog.dismiss()
                        Toast.makeText(this, "you are now a writer", Toast.LENGTH_SHORT).show()
                    }

                    dialog.show()

                }

                R.id.menu_photograph -> {
                    binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
                    // load fragment
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.add(R.id.frame_main, PhotographerFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()

                    // check menu item
                    binding.navigationViewMain.menu.getItem(1).isChecked = true

                    // close drawer
                    binding.drawerLayoutMain.closeDrawer(GravityCompat.START)

                }
                R.id.menu_video_maker -> {

                    Snackbar.make(binding.root, "You can create video", Snackbar.LENGTH_LONG)
                        .setAction("Signup") {
                            // new window appears
                        }.setActionTextColor(ContextCompat.getColor(this, R.color.orange_dark))
                        .show()
                    binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
                }
                R.id.menu_translator -> {
                    binding.drawerLayoutMain.closeDrawer(GravityCompat.START)

                    // open an activity
                    val intent = Intent(this, MainActivity3::class.java)
                    startActivity(intent)
                }

                ////////////////////////////////////////////////////////////

                R.id.menu_open_wikimedia -> {
                    binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
                    openWebsite("https://breakingbad.fandom.com/wiki/Breaking_Bad_Wiki")
                }
                R.id.menu_open_wikipedia -> {
                    binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
                }

            }
            true
        }

        firstRun()

        var currentBottomNavigation = R.id.menu_explore
        var person = Person("","","","")
        binding.bottomNavigationMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_explore -> {
                    currentBottomNavigation = R.id.menu_explore
                    replaceFragment(ExploreFragment())
                }
                R.id.menu_trend -> {
                    currentBottomNavigation = R.id.menu_trend
                    replaceFragment(TrendFragment())
                }
                R.id.menu_profile -> {

                        if (person.name.isNotEmpty()) {
                            replaceFragment(ProfileFragment(person))
                        } else {
                        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        sweetAlertDialog.titleText = "You are not a writer!"
                        sweetAlertDialog.confirmText = "SignUp"
                        sweetAlertDialog.cancelText = "Cancel"
                        sweetAlertDialog.contentText = "Wanna be a writer?"

                        sweetAlertDialog.setCancelClickListener {
                            sweetAlertDialog.dismiss()
                            binding.bottomNavigationMain.menu.findItem(currentBottomNavigation).isChecked =
                                true
                        }

                        sweetAlertDialog.setConfirmClickListener {
                            sweetAlertDialog.dismiss()
                            val alertDialog = AlertDialog.Builder(this).create()
                            val dialogBinding = DialogSignUpBinding.inflate(layoutInflater)
                            alertDialog.setView(dialogBinding.root)
                            alertDialog.setCancelable(true)
                            alertDialog.show()
                            binding.bottomNavigationMain.menu.findItem(currentBottomNavigation).isChecked =
                                true

                            dialogBinding.btnSignUp.setOnClickListener {
                                currentBottomNavigation = R.id.menu_profile
                                binding.bottomNavigationMain.menu.findItem(currentBottomNavigation).isChecked =
                                    true




                                if (dialogBinding.dialogEdtName.length() > 0 && dialogBinding.dialogEdtGmail.length() > 0 && dialogBinding.dialogEdtId.length() > 0) {

                                    val txtName = dialogBinding.dialogEdtName.text.toString()
                                    val txtGmail = dialogBinding.dialogEdtGmail.text.toString()
                                    val txtID = dialogBinding.dialogEdtId.text.toString()
                                    person = Person(txtName, "Writer", txtGmail, txtID)
                                    replaceFragment(ProfileFragment(person))
                                    alertDialog.dismiss()

                                } else {
                                    Toast.makeText(this, "Complete all parts", Toast.LENGTH_SHORT)
                                        .show()
                                }


                            }
                        }

                        sweetAlertDialog.show()
                    }

                }
            }

            binding.navigationViewMain.menu.getItem(1).isChecked = false

            true
        }
        binding.bottomNavigationMain.setOnItemReselectedListener {
            // do nothing
        }

    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_main, fragment)
        transaction.commit()
    }

    private fun firstRun() {
        replaceFragment(ExploreFragment())
    }


    override fun onBackPressed() {
        super.onBackPressed()
        // check menu item off
        binding.navigationViewMain.menu.getItem(1).isChecked = false
    }
}