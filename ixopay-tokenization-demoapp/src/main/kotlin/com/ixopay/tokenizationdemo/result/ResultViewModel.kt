package com.ixopay.tokenizationdemo.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {
	var token = MutableLiveData<String>()
	var fingerprint = MutableLiveData<String>()
	var errorMessage = MutableLiveData<String>()
	var errorStacktrace = MutableLiveData<String>()
}