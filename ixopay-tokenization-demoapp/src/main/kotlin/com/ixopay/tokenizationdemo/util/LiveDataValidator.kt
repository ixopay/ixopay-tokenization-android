package com.ixopay.tokenizationdemo.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LiveDataValidator(private val liveData: LiveData<String>) {
	private val validationRules = mutableListOf<ValidationRule>()

	var error = MutableLiveData<String?>()

	val valid: Boolean
		get() {
			validationRules.forEach {
				try {
					if (it.predicate.invoke(liveData.value)) {
						emitErrorMessage(it.errorMessage)
						return false
					}
				} catch (e: Exception) {
					emitErrorMessage(e.localizedMessage)
					return false
				}
			}

			emitErrorMessage(null)
			return true
		}

	private fun emitErrorMessage(messageRes: String?) {
		error.value = messageRes
	}

	fun addRule(errorMsg: String, predicate: ValidatorPredicate) {
		validationRules.add(ValidationRule(predicate, errorMsg))
	}
}