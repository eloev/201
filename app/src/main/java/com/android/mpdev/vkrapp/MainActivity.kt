package com.android.mpdev.vkrapp

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.mpdev.vkrapp.databinding.ActivityMainBinding
import com.android.mpdev.vkrapp.ui.firstScreen.FirstViewModel
import com.android.mpdev.vkrapp.ui.secondScreen.SecondViewModel
import com.android.mpdev.vkrapp.utils.NFCUtilManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var lifeCycleRegistry : LifecycleRegistry

    private lateinit var readViewModel: FirstViewModel
    private lateinit var writeViewModel: SecondViewModel

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
        lifeCycleRegistry = LifecycleRegistry(this)
        lifeCycleRegistry.markState(Lifecycle.State.CREATED)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        // Передаём каждый айди как группу
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_first, R.id.navigation_second
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
    }

    override fun onResume() {
        super.onResume()
        lifeCycleRegistry.markState(Lifecycle.State.RESUMED)
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
                showToast("Сообщение сохранено")
            } else {
                showToast("Не удалось сохранить сообщение. Попробуйте ещё")
            }
        } else {
            if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action){
                // Проверяем в какой фрагмент отправка
                val ndefMessageArray = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES
                )
                val ndefMessage = ndefMessageArray?.get(0) as NdefMessage
                val msg = String(ndefMessage.records[0].payload)
                //set message
                readViewModel?.setTagMessage(msg)
            } else {
                readViewModel?.setTagMessage("Пустая или неисправная метка")
            }
        }
    }

    private fun initUi() {

    }

    private fun enableNfc() {
        if (nfcAdapter?.isEnabled == true) {
            nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
        } else {
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            showToast("Включите NFC в настройках")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
