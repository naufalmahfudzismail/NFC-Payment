package ta.putri.prodouct_barcode.customer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ta.putri.prodouct_barcode.repository.ApiFactory
import java.lang.NullPointerException

class NFCPresenter() {

    private var nfcView : NFCView? = null

    private val service = ApiFactory.makeRetrofitService()
    var job: Job? = null

    fun retriveProduct(code : String, nfcView: NFCView?){
        this.nfcView = nfcView
        nfcView?.onLoading()
        doAsync {
            job = GlobalScope.launch(Dispatchers.Main){

                try {
                    val data = service.getProductAsync(code)
                    val result = data.await()
                    uiThread {
                        nfcView?.getResponses(result.body())
                        nfcView?.onFinish()
                    }
                }

                catch (e : NullPointerException){
                    nfcView?.error(e.toString())
                    nfcView?.onFinish()
                }
            }
        }
    }

    fun viewOnDestroy() {
        job?.cancel()
        nfcView = null
    }
}