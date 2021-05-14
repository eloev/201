package com.android.mpdev.vkrapp

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.mpdev.vkrapp.databinding.ActivityMainBinding
import com.android.mpdev.vkrapp.databinding.SidebarNavBinding
import com.android.mpdev.vkrapp.ui.firstScreen.FirstViewModel
import com.android.mpdev.vkrapp.ui.pass.PassViewModel
import com.android.mpdev.vkrapp.ui.secondScreen.SecondViewModel
import com.android.mpdev.vkrapp.utils.NFCUtilManager
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var lifeCycleRegistry : LifecycleRegistry

    private lateinit var readViewModel: FirstViewModel
    private lateinit var writeViewModel: SecondViewModel
    private lateinit var passViewModel: PassViewModel

    private var nfcAdapter: NfcAdapter? = null

    private var pendingIntent: PendingIntent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        readViewModel = ViewModelProvider(this).get(FirstViewModel::class.java)
        writeViewModel = ViewModelProvider(this).get(SecondViewModel::class.java)
        passViewModel = ViewModelProvider(this).get(PassViewModel::class.java)

        lifeCycleRegistry = LifecycleRegistry(this)
        lifeCycleRegistry.currentState = Lifecycle.State.CREATED

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.host_fragment)
        navView.setupWithNavController(navController)

        val toolbarMain: Toolbar = findViewById(R.id.toolbar_main)
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout = binding.drawerLayout)
        toolbarMain.setupWithNavController(navController, appBarConfiguration)

        val sideBar = binding.sidebarNavView
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
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (writeViewModel?.isWriteTagOptionOn) {
            val messageWrittenSuccessfully = NFCUtilManager.createNFCMessage(writeViewModel?.messageToSave, intent)
            writeViewModel?.isWriteTagOptionOn = false
            writeViewModel?._closeDialog.value = true

            if (messageWrittenSuccessfully){
                //запись успешна
                showToast(getString(R.string.write_success))
            } else {
                //запись неуспешна
                showToast(getString(R.string.write_error))
            }
        } else {
            if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action){
                // Проверяем в какой фрагмент отправка
                val ndefMessageArray = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES
                )
                val ndefMessage = ndefMessageArray?.get(0) as NdefMessage
                val msg = String(ndefMessage.records[0].payload)
                //идентификация входа
                if (msg == "200" && passViewModel.passIsVisible){
                    onBackPressed()
                    readViewModel?.setTagMessage(getString(R.string.pass_success))
                }
                else{
                    //показываем сообщение
                    readViewModel?.setTagMessage(msg)
                }
            } else {
                //Пустая метка
                readViewModel?.setTagMessage(getString(R.string.read_empty))
            }
        }
    }

    private fun initUi() {

    }

    private fun enableNfc() {
        if (nfcAdapter?.isEnabled == true) {
            nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
        } else {
            //выводим экран включения NFC
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            showToast(getString(R.string.nfc_disabled))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
