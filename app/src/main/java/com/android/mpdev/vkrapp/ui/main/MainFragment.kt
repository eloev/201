package com.android.mpdev.vkrapp.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html.fromHtml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.mpdev.vkrapp.R
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "MainFragment"

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val viewPager: ViewPager2 = view.findViewById(R.id.main_promo)
        viewPager.adapter = ViewPagerAdapter(fillList())

        val navController = NavHostFragment.findNavController(this)

        //логин
        val mainUserImg: ImageView = view.findViewById(R.id.main_user_img)
        val mainUsername: TextView = view.findViewById(R.id.main_username)
        mainUserImg.setOnClickListener {
            //FirebaseAuth.getInstance().signOut()
        }

        //бонусы
        val mainBonusTv: TextView = view.findViewById(R.id.main_bonus_tv)
        val mainBonusCount: TextView = view.findViewById(R.id.main_bonus_count)
        val mainBonusImg: ImageView = view.findViewById(R.id.main_bonus_img)

        mainBonusTv.setOnClickListener { navController.navigate(R.id.navigation_bonus) }
        mainBonusCount.setOnClickListener { navController.navigate(R.id.navigation_bonus) }
        mainBonusImg.setOnClickListener { navController.navigate(R.id.navigation_bonus) }

        //чеки
        val mainReceiptTv: TextView = view.findViewById(R.id.main_receipt_tv)
        val mainReceiptCount: TextView = view.findViewById(R.id.main_receipt_count)
        val mainReceiptImg: ImageView = view.findViewById(R.id.main_receipt_img)

        mainReceiptTv.setOnClickListener { navController.navigate(R.id.navigation_receipt) }
        mainReceiptCount.setOnClickListener { navController.navigate(R.id.navigation_receipt) }
        mainReceiptImg.setOnClickListener { navController.navigate(R.id.navigation_receipt) }

        //акции
        val mainPromoTv: TextView = view.findViewById(R.id.main_promo_tv)
        val mainPromoTvs: TextView = view.findViewById(R.id.main_promo_tvs)

        val layout: LinearLayout = view.findViewById(R.id.main_promo_dots)
        val dots: Array<TextView?> = arrayOfNulls(4)
        val promoText: List<Int> = listOf(R.string.promo1, R.string.promo2, R.string.promo3, R.string.promo4)
        val promoTexts: List<Int> = listOf(R.string.promo1s, R.string.promo2s, R.string.promo3s, R.string.promo4s)





        @SuppressLint("SetTextI18n")
        fun selectedDots(position: Int) {
            for (i in dots.indices) {
                if (i == position) {
                    dots[i]?.setTextColor(resources.getColor(R.color.colorPrimary))
                    mainPromoTv.text = " " + getString(promoText[i])
                    mainPromoTvs.text = getString(promoTexts[i])
                } else {
                    dots[i]?.setTextColor(resources.getColor(R.color.gray200))
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun setIndicators() {
            for (i in dots.indices) {
                dots[i] = TextView(activity)
                dots[i]?.text = "${fromHtml("&#9679;")}  "
                dots[i]?.textSize = 24F
                layout.addView(dots[i])
            }
        }

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedDots(position);
                super.onPageSelected(position)
            }
        })

        setIndicators()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private class ViewPagerAdapter(private val images: List<Image>) :
        RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder>() {

        data class Image(val imageSrc: Int)

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var mainPromoImg: ImageView? = null
            init {
                mainPromoImg = itemView.findViewById(R.id.main_promo_img)
            }
            fun bindView(image: Image){
                mainPromoImg?.setImageResource(image.imageSrc)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.main_img_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindView(images[position])
        }

        override fun getItemCount(): Int {
            return images.size
        }
    }

    private fun fillList(): List<ViewPagerAdapter.Image> {
        return listOf(
            ViewPagerAdapter.Image(R.mipmap.promo1),
            ViewPagerAdapter.Image(R.mipmap.promo2),
            ViewPagerAdapter.Image(R.mipmap.promo3),
            ViewPagerAdapter.Image(R.mipmap.promo4)
        )
    }
}
