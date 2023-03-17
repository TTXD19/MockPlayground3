class Kid {

    fun wantMoney() {
        val handling = DataApiHolder(object : ResultOutsideHandler() {
            override fun onSuccess() {
                testSuccess()
            }

            override fun onFailure() {
                testFailure()
            }
        })
        handling.sendApiRequest()
    }

    fun testSpy(){
        if (true){
            testSuccess()
            return
        }
        testFailure()
    }

    fun testSuccess() = Unit
    fun testFailure() = Unit
}