package com.eagletech.happynote.screen.acts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amazon.device.drm.LicensingService
import com.amazon.device.iap.PurchasingListener
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.FulfillmentResult
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.UserDataResponse
import com.eagletech.happynote.R
import com.eagletech.happynote.databinding.ActivityPaymentBinding
import com.eagletech.happynote.datauser.DataSharedPreferences

class PaymentActivity : AppCompatActivity() {
    private lateinit var paymentBinding: ActivityPaymentBinding
    private lateinit var dataSharedPreferences: DataSharedPreferences
    private lateinit var currentUserId: String
    private lateinit var currentMarketplace: String

    // Phải thêm sku các gói vào ứng dụng
    companion object {
        const val sub3Times = "com.eagletech.happynote.threenotes"
        const val sub5Times = "com.eagletech.happynote.fivenotes"
        const val sub10Times = "com.eagletech.happynote.tennotes"
        const val subPre = "com.eagletech.happynote.subpremium"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentBinding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(paymentBinding.root)
        dataSharedPreferences = DataSharedPreferences.getInstance(this)
        setupIAPOnCreate()
        setClickItems()

    }

    private fun setClickItems() {
        paymentBinding.buttonBuy3.setOnClickListener {
//            dataSharedPreferences.addLives(3)
            PurchasingService.purchase(sub3Times)
        }
        paymentBinding.buttonBuy5.setOnClickListener {
            PurchasingService.purchase(sub5Times)
        }
        paymentBinding.buttonBuy10.setOnClickListener {
            PurchasingService.purchase(sub10Times)
        }
        paymentBinding.buttonBuyPremiumWeek.setOnClickListener {
            PurchasingService.purchase(subPre)
//            dataSharedPreferences.isPremium = true
        }
        paymentBinding.buttonExitApp.setOnClickListener { finish() }
    }

    private fun setupIAPOnCreate() {
        val purchasingListener: PurchasingListener = object : PurchasingListener {
            override fun onUserDataResponse(response: UserDataResponse) {
                when (response.requestStatus!!) {
                    UserDataResponse.RequestStatus.SUCCESSFUL -> {
                        currentUserId = response.userData.userId
                        currentMarketplace = response.userData.marketplace
                        dataSharedPreferences.currentUserId(currentUserId)
                        Log.v("IAP SDK", "loaded userdataResponse")
                    }

                    UserDataResponse.RequestStatus.FAILED, UserDataResponse.RequestStatus.NOT_SUPPORTED ->
                        Log.v("IAP SDK", "loading failed")
                }
            }

            override fun onProductDataResponse(productDataResponse: ProductDataResponse) {
                when (productDataResponse.requestStatus) {
                    ProductDataResponse.RequestStatus.SUCCESSFUL -> {
                        val products = productDataResponse.productData
                        for (key in products.keys) {
                            val product = products[key]
                            Log.v(
                                "Product:", String.format(
                                    "Product: %s\n Type: %s\n SKU: %s\n Price: %s\n Description: %s\n",
                                    product!!.title,
                                    product.productType,
                                    product.sku,
                                    product.price,
                                    product.description
                                )
                            )
                        }
                        //get all unavailable SKUs
                        for (s in productDataResponse.unavailableSkus) {
                            Log.v("Unavailable SKU:$s", "Unavailable SKU:$s")
                        }
                    }

                    ProductDataResponse.RequestStatus.FAILED -> Log.v("FAILED", "FAILED")
                    else -> {}
                }
            }

            override fun onPurchaseResponse(purchaseResponse: PurchaseResponse) {
                when (purchaseResponse.requestStatus) {
                    PurchaseResponse.RequestStatus.SUCCESSFUL -> {

                        if (purchaseResponse.receipt.sku == sub3Times) {
                            dataSharedPreferences.addLives(3)
                        }
                        if (purchaseResponse.receipt.sku == sub5Times) {
                            dataSharedPreferences.addLives(10)
                        }
                        if (purchaseResponse.receipt.sku == sub10Times) {
                            dataSharedPreferences.addLives(15)
                        }

                        PurchasingService.notifyFulfillment(
                            purchaseResponse.receipt.receiptId,
                            FulfillmentResult.FULFILLED
                        )

                        dataSharedPreferences.isPremium = !purchaseResponse.receipt.isCanceled
                        Log.v("FAILED", "FAILED")
                    }

                    PurchaseResponse.RequestStatus.FAILED -> {}
                    else -> {}
                }
            }

            override fun onPurchaseUpdatesResponse(response: PurchaseUpdatesResponse) {
                // Process receipts
                when (response.requestStatus) {
                    PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL -> {
                        for (receipt in response.receipts) {
                            dataSharedPreferences.isPremium = !receipt.isCanceled
                        }
                        if (response.hasMore()) {
                            PurchasingService.getPurchaseUpdates(false)
                        }

                    }

                    PurchaseUpdatesResponse.RequestStatus.FAILED -> Log.d("FAILED", "FAILED")
                    else -> {}
                }
            }
        }
        PurchasingService.registerListener(this, purchasingListener)
        Log.d(
            "DetailBuyAct",
            "Appstore SDK Mode: " + LicensingService.getAppstoreSDKMode()
        )
    }


    override fun onResume() {
        super.onResume()
        PurchasingService.getUserData()
        val productSkus: MutableSet<String> = HashSet()
        productSkus.add(subPre)
        productSkus.add(sub3Times)
        productSkus.add(sub5Times)
        productSkus.add(sub10Times)
        PurchasingService.getProductData(productSkus)
        PurchasingService.getPurchaseUpdates(false)
    }
}