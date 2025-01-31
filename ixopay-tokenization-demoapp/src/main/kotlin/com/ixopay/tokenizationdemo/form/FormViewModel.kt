package com.ixopay.tokenizationdemo.form

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.ixopay.api.tokenization.CardData
import com.ixopay.api.tokenization.InvalidParameterException
import com.ixopay.api.tokenization.TokenizationApi
import com.ixopay.api.tokenization.TokenizationApiException
import com.ixopay.tokenizationdemo.BuildConfig
import com.ixopay.tokenizationdemo.util.LiveDataValidator
import com.ixopay.tokenizationdemo.util.TokenizeResult
import java.util.*

class FormViewModel : ViewModel() {
	var pan = MutableLiveData<String>()
	val panValidator = LiveDataValidator(pan).apply {
		addRule("PAN is required") { it.isNullOrBlank() }
	}

	var cardholder = MutableLiveData<String>()
	val cardholderValidator = LiveDataValidator(cardholder).apply {
		addRule("Cardholder is required") { it.isNullOrBlank() }
	}

	var expiryMonth = MutableLiveData<String>()
	val expiryMonthValidator = LiveDataValidator(expiryMonth).apply {
		addRule("Expiry month is required") { it.isNullOrBlank() }
		addRule("Expiry month must be a number") { it?.toIntOrNull() == null }
		addRule("Expiry month must smaller or equal to 12") { val month = it?.toIntOrNull(); return@addRule month == null || month > 12 }
		addRule("Expiry month must greater or equal to 1") { val month = it?.toIntOrNull(); return@addRule month == null || month < 1 }
	}

	var expiryYear = MutableLiveData<String>()
	val expiryYearValidator = LiveDataValidator(expiryYear).apply {
		addRule("Expiry year is required") { it.isNullOrBlank() }
		addRule("Expiry year must be a number") { it?.toIntOrNull() == null }
		addRule("Expiry month must at least be the current year") { val year = it?.toIntOrNull(); return@addRule year == null || year < Calendar.getInstance().get(Calendar.YEAR) }
	}

	var cvv = MutableLiveData<String>()
	val cvvValidator = LiveDataValidator(cvv).apply {
		addRule("CVV is required") { it.isNullOrBlank() }
	}

	val isValidMediator = MediatorLiveData<Boolean>()

	init {
		isValidMediator.value = false
		isValidMediator.addSource(cardholder) { validateForm() }
		isValidMediator.addSource(pan) { validateForm() }
		isValidMediator.addSource(expiryMonth) { validateForm() }
		isValidMediator.addSource(expiryYear) { validateForm() }
		isValidMediator.addSource(cvv) { validateForm() }
	}

	fun validateForm() {
		val validators = listOf(cardholderValidator, panValidator, expiryMonthValidator, expiryYearValidator, cvvValidator)
		var valid = true
		validators.forEach {
			valid = valid && it.valid
			true
		}
		isValidMediator.value = valid
	}

	private val cardData: CardData
		get() {
			return CardData(
				pan.value,
				cvv.value,
				cardholder.value,
				expiryMonth.value!!.toInt(),
				expiryYear.value!!.toInt(),
			)
		}

	private val _tokenizeResult = MutableLiveData<TokenizeResult?>()
	val tokenizeResult: LiveData<TokenizeResult?> = _tokenizeResult

	private val _isLoading = MutableLiveData<Boolean>(false)
	val isLoading: LiveData<Boolean> = _isLoading

	fun tokenize() {
		_isLoading.value = true  // Show loading indicator

		viewModelScope.launch(Dispatchers.IO) {
			try {
				val token = apiClient.tokenizeCardData(this@FormViewModel.cardData)
				_tokenizeResult.postValue(TokenizeResult.Success(token.token, token.fingerprint))
			} catch (e: TokenizationApiException) {
				Log.e("ixopay", "API Error", e)
				_tokenizeResult.postValue(TokenizeResult.Error(e.message ?: "", e.stackTraceToString()))
			} catch (e: InvalidParameterException) {
				Log.e("ixopay", "Invalid parameter error", e)
				e.cvvError?.let { cvvValidator.error.postValue(it.toString()) }
				e.monthError?.let { expiryMonthValidator.error.postValue(it.toString()) }
				e.yearError?.let { expiryYearValidator.error.postValue(it.toString()) }
				e.panError?.let { panValidator.error.postValue(it.toString()) }
				_tokenizeResult.postValue(TokenizeResult.InputError())
			} catch (e: Exception) {
				Log.e("ixopay", "Unknown error", e)
				_tokenizeResult.postValue(TokenizeResult.Error(e.message ?: "", e.stackTraceToString()))
			} finally {
				_isLoading.postValue(false)
			}
		}
	}

	fun clearTokenizeResult() {
		_tokenizeResult.value = null
	}

	companion object {
		val apiClient: TokenizationApi by lazy {
			TokenizationApi.builder()
				.gatewayHost(BuildConfig.IXOPAY_GATEWAY_HOST)
				.tokenizationHost(BuildConfig.IXOPAY_TOKENIZATION_HOST)
				.integrationKey(BuildConfig.IXOPAY_INTEGRATION_KEY)
				.build()
		}
	}
}