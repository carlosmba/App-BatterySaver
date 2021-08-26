package com.mendozasolutions.batterysaver.core

sealed class Failure{
    data class NetworkConnection(val message : String): Failure()
    data class ServerError(val message : String): Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()
}
