abstract class BaseAPIDataHolder(val resultOutsideListener: ResultOutsideListener) {


    var resultListener: ResultInsideListener
    var a = true

    init {
        resultListener = object : ResultInsideListener {
            override fun onSuccess() {
                resultOutsideListener.onSuccess()
            }

            override fun onFailure() {
                resultOutsideListener.onFailure()
            }
        }
    }

    fun sendApiRequest() {
        if (a){
            resultListener.onSuccess()
            return
        }
        resultListener.onFailure()
    }
}