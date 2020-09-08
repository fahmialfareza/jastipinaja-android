package com.dinokeylas.jastipinaja

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.snap.CreditCard
import com.midtrans.sdk.corekit.models.snap.CustomerDetails
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import kotlinx.android.synthetic.main.activity_payment.*
import java.util.*

class PaymentActivity : AppCompatActivity(), TransactionFinishedCallback {
    companion object {
        const val ARG_TOTAL_TAGIHAN = "ARG_TOTAL_TAGIHAN"
        const val ARG_ALAMAT_KIRIM = "ARG_ALAMAT_KIRIM"
    }

    private var totalTagihan = 0
    private var alamatKirim = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        init()
    }

    private fun init() {
        initObject()
        initUI()
        eventUI()
    }

    private fun initObject() {
        totalTagihan = intent.getIntExtra(ARG_TOTAL_TAGIHAN, 0)
        alamatKirim = intent.getStringExtra(ARG_ALAMAT_KIRIM)
        makePayment()
    }

    private fun initUI() {
        tvAlamatKirim?.text = alamatKirim
        tvTotalTagihan?.text = "Total Tagihan ${totalTagihan}"
    }

    private fun eventUI(){
        btnPay?.setOnClickListener {
            clickPay()
        }
    }

    private fun makePayment(){
        SdkUIFlowBuilder.init()
            .setContext(this)
            .setMerchantBaseUrl(BuildConfig.BASE_URL)
            .setClientKey(BuildConfig.CLIENT_KEY)
            .setTransactionFinishedCallback(this)
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
            .buildSDK()
    }

    private fun clickPay(){
        MidtransSDK.getInstance().transactionRequest = transactionRequest("101", totalTagihan, 1, "susu")
        MidtransSDK.getInstance().startPaymentUiFlow(this)
    }

    private fun customerDetails(): CustomerDetails {
        val cd = CustomerDetails()
        cd.name = "Arif"
        cd.email = "rahmanarif710@gmail.com"
        cd.phone = "087761293100"
        return cd
    }

    private fun transactionRequest(
        id: String?,
        price: Int,
        qty: Int,
        name: String?
    ): TransactionRequest {
        val request = TransactionRequest(System.currentTimeMillis().toString() + " ", 2000.0)

        val details = ItemDetails(id, price.toDouble(), qty, name)
        val itemDetails = ArrayList<ItemDetails>()

        itemDetails.add(details)
        request.itemDetails = itemDetails

        val creditCard = CreditCard()
        creditCard.isSaveCard = false
        creditCard.authentication = CreditCard.AUTHENTICATION_TYPE_RBA
        request.creditCard = creditCard
        return request
    }

    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> Toast.makeText(
                    this,
                    "Transaction Sukses " + result.response.transactionId,
                    Toast.LENGTH_LONG
                ).show()
                TransactionResult.STATUS_PENDING -> Toast.makeText(
                    this,
                    "Transaction Pending " + result.response.transactionId,
                    Toast.LENGTH_LONG
                ).show()
                TransactionResult.STATUS_FAILED -> Toast.makeText(
                    this,
                    "Transaction Failed" + result.response.transactionId,
                    Toast.LENGTH_LONG
                ).show()
            }
            result.response.validationMessages
        } else if (result.isTransactionCanceled) {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, ignoreCase = true)) {
                Toast.makeText(
                    this,
                    "Transaction Invalid" + result.response.transactionId,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show()
            }
        }

    }
}