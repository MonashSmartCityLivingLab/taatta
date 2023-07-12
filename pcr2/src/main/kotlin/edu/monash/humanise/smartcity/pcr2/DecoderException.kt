package edu.monash.humanise.smartcity.pcr2

/**
 * An exception class for decoding errors in [Decoder].
 */
class DecoderException : IllegalArgumentException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}