package com.harryfultz.studentmanager.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import com.harryfultz.studentmanager.R
import com.harryfultz.studentmanager.appConfigAndDB.StatusBarColor
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment

class IntroActivity : AppIntro2() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(AppIntro2Fragment.newInstance("Mirësevini në aplikacionin e mësuesit", "Ky program është dizenjuar për mesueset/it e shkollës \"Harry Fultz\"", R.drawable.hflogo, Color.parseColor("#23487c")))
        addSlide(AppIntro2Fragment.newInstance("Lajmëro prindërit për mungesa", "Dërgo lajmërime prindërve të nxënësve në kujdestari për mungesat gjatë orëve të javës.", R.drawable.absences, Color.parseColor("#3693D0")))
        addSlide(AppIntro2Fragment.newInstance("Mbledhje me prindër", "Lajmëroni prindërit për organizimin e mbledhjeve me prindër në shkollë.", R.drawable.family, Color.parseColor("#ff1744")))
        addSlide(AppIntro2Fragment.newInstance("Ankesë", "Njoftoni prindërit e nxënësit në rast problemesh në shkollë.", R.drawable.complain, Color.parseColor("#36CF8E")))


        StatusBarColor.changeStatusBarColor(this)


    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        startActivity(Intent(this, MainPage::class.java))
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        startActivity(Intent(this, MainPage::class.java))
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
    }

}
