open class DataApiHolder(
    resultOutsideListener: ResultOutsideListener
) : BaseAPIDataHolder(resultOutsideListener) {

    val aTest: Int = 0

    init {
        test()
    }

    fun test(){
        println()
    }

}