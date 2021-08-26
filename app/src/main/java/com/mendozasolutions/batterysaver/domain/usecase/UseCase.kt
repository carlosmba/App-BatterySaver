package com.mendozasolutions.batterysaver.domain.usecase

import com.mendozasolutions.batterysaver.core.Result

abstract class UseCase<out Type, in Params> {


    abstract fun run(params : Params) : Result<Any, Type>

    fun execute(onResult : (Result<Any, Type>) -> Unit, params : Params){

    }

    class None

}