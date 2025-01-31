package com.ixopay.tokenizationdemo.util

import java.io.Serializable

sealed class TokenizeResult : Serializable {
	companion object {
		@Suppress("unused")
		private val serialVersionUid: Long = 1
	}

	data class Success(
		val token: String,
		val fingerprint: String
	) : TokenizeResult()

	class InputError() : TokenizeResult()

	data class Error(
		val error: String,
		val stackTrace: String
	) : TokenizeResult()
}