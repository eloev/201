package com.android.mpdev.vkrapp

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.mpdev.vkrapp.databinding.ActivityMainBinding
import com.android.mpdev.vkrapp.ui.firstScreen.FirstViewModel
import com.android.mpdev.vkrapp.ui.pass.AddProduct
import com.android.mpdev.vkrapp.ui.receipt.Receipt
import com.android.mpdev.vkrapp.ui.receipt.ReceiptViewModel
import com.android.mpdev.vkrapp.ui.secondScreen.SecondViewModel
import com.android.mpdev.vkrapp.utils.NFCUtilManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var lifeCycleRegistry: LifecycleRegistry

    private lateinit var readViewModel: FirstViewModel
    private lateinit var writeViewModel: SecondViewModel
    private lateinit var receiptViewModel: ReceiptViewModel

    private var nfcAdapter: NfcAdapter? = null

    private var pendingIntent: PendingIntent? = null

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        readViewModel = ViewModelProvider(this).get(FirstViewModel::class.java)
        writeViewModel = ViewModelProvider(this).get(SecondViewModel::class.java)
        receiptViewModel = ViewModelProvider(this).get(ReceiptViewModel::class.java)

        lifeCycleRegistry = LifecycleRegistry(this)
        lifeCycleRegistry.currentState = Lifecycle.State.CREATED

        val navView: BottomNavigationView = binding.navView
        val sideBar = binding.sidebarNavView
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.host_fragment)
        val toolbarMain: Toolbar = findViewById(R.id.toolbar_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_pass,
                R.id.navigation_receipt,
                R.id.navigation_bonus,
                R.id.navigation_main,
                R.id.navigation_catalog
            ), drawerLayout
        )
        toolbarMain.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        sideBar.setupWithNavController(navController)

        pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
    }

    override fun onResume() {
        super.onResume()
        lifeCycleRegistry.currentState = Lifecycle.State.RESUMED
        enableNfc()
        signIn()
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val navController = findNavController(R.id.host_fragment)

        if (writeViewModel?.isWriteTagOptionOn) {
            val messageWrittenSuccessfully =
                NFCUtilManager.createNFCMessage(writeViewModel?.messageToSave, intent)
            writeViewModel?.isWriteTagOptionOn = false
            writeViewModel?._closeDialog.value = true

            if (messageWrittenSuccessfully) {
                //???????????? ??????????????
                showToast(getString(R.string.write_success))
            } else {
                //???????????? ??????????????????
                showToast(getString(R.string.write_error))
            }
        } else {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                // ?????????????????? ?? ?????????? ???????????????? ????????????????
                val ndefMessageArray = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES
                )
                val ndefMessage = ndefMessageArray?.get(0) as NdefMessage
                val msg = String(ndefMessage.records[0].payload)
                //?????????????????????????? ??????????
                if (msg == "200" && receiptViewModel.passIsVisible) {
                    //onBackPressed()
                    readViewModel?.setTagMessage(getString(R.string.pass_success))
                    receiptViewModel?.passIsInit = true
                }
                else if (msg == "200" && !receiptViewModel.passIsVisible){
                    navController.navigate(R.id.navigation_pass)
                }
                else if(msg == "kitkat" || msg == "lipton" || msg == "milk"){
                    AddProduct(msg)
                }
                else if(msg == "201"){
                    val sdf = SimpleDateFormat("HH:mm, dd.MM.yyyy")
                    val dateNow = sdf.format(Date())
                    val receipt = Receipt(UUID.randomUUID(), dateNow, receiptViewModel.passPrice.toInt(), receiptViewModel.passProduct)
                    receiptViewModel.addReceipt(receipt)
                }
                else {
                    //???????????????????? ??????????????????
                    readViewModel?.setTagMessage(msg)
                }
            } else {
                //???????????? ??????????
                readViewModel?.setTagMessage(getString(R.string.read_empty))
            }
        }
    }

    private fun enableNfc() {
        if (nfcAdapter?.isEnabled == true) {
            nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
        } else {
            //?????????????? ?????????? ?????????????????? NFC
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            showToast(getString(R.string.nfc_disabled))
        }
    }

    private fun signIn(){
        //?????????????????? ?????????? ???? ?? ??????????????
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        if(user == null){
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
