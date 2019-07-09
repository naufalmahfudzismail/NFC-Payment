package ta.putri.prodouct_barcode.login

import android.content.Context
import android.database.sqlite.SQLiteException
import kotlinx.coroutines.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import ta.putri.prodouct_barcode.utlis.database
import ta.putri.prodouct_barcode.model.CurrentUser
import ta.putri.prodouct_barcode.model.UserModel
import ta.putri.prodouct_barcode.repository.ApiFactory

class LoginPresenter(val context: Context, val view: LoginView) {

    private var isRegistered = false
    private var isPasswordtrue = false
    private var isLogged = false
    private var message = " "
    private var id = ""

    private val service = ApiFactory.makeRetrofitService()
    var job: Job? = null

    fun login(email: String, password: String) {

        view.onLoading()

        doAsync {

            job = GlobalScope.launch(Dispatchers.Default) {
                try {

                    val data = service.login(email, password)
                    val result = data.await()
                    withContext(Dispatchers.Main) {
                        uiThread {
                            view.getResponses(result.body())
                            view.onFinish()
                        }
                    }

                } catch (e: SQLiteException) {
                    uiThread {
                        context.toast(e.localizedMessage)
                        view.onFinish()
                    }
                }
            }

        }
    }
}