package com.example.moviedb.core.error

import java.io.IOException

abstract class DomainError(message: String): Throwable(message)
class InternetUnavailable : IOException("No Network Available")

class GenericError(message: String = "Something went wrong"): DomainError(message)