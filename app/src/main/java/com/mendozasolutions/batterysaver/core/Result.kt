package com.mendozasolutions.batterysaver.core

sealed class Result<out F, out S>(){

    data class Fail<F>(val failure : F) : Result<F, Nothing>()

    data class Success<S>(val data : S) : Result<Nothing, S>()


}
