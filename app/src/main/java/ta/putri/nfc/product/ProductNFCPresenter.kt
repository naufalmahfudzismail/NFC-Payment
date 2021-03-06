package ta.putri.nfc.product

import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ta.putri.nfc.repository.ApiFactory
import java.lang.NullPointerException

class ProductNFCPresenter {

    private var productNfcView: ProductNFCView? = null

    private val service = ApiFactory.makeRetrofitService()
    var job: Job? = null

    fun retriveProduct(code: String, productNfcView: ProductNFCView?) {
        this.productNfcView = productNfcView
        productNfcView?.onLoading()

        job = GlobalScope.launch(Dispatchers.Main){
            try {
                val data = service.getProductAsync(code)
                val result = data.await()

                productNfcView?.getResponses(result.body())
                productNfcView?.onFinish()

            } catch (e: NullPointerException) {
                productNfcView?.error(e.toString())
                productNfcView?.onFinish()
            }
        }

    }

    fun viewOnDestroy() {
        job?.cancel()
        productNfcView = null
    }
}