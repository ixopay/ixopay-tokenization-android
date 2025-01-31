package com.ixopay.tokenizationdemo.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ixopay.tokenizationdemo.databinding.FragmentResultBinding
import com.ixopay.tokenizationdemo.util.TokenizeResult

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the second destination in the navigation.
 */
class ResultFragment : Fragment() {

	private var _binding: FragmentResultBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	private val args by navArgs<ResultFragmentArgs>()
	private val viewModel: ResultViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentResultBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val tokenizeResult = args.tokenizeResult
		when (tokenizeResult) {
			is TokenizeResult.Success -> {
				viewModel.token.value = tokenizeResult.token
				viewModel.fingerprint.value = tokenizeResult.fingerprint
				viewModel.errorMessage.value = ""
				viewModel.errorStacktrace.value = ""
			}

			is TokenizeResult.Error -> {
				viewModel.token.value = ""
				viewModel.fingerprint.value = ""
				viewModel.errorMessage.value = tokenizeResult.error
				viewModel.errorStacktrace.value = tokenizeResult.stackTrace
			}

			is TokenizeResult.InputError -> error("Unexpected tokenizeResult")
		}

		binding.buttonPrevious.setOnClickListener {
			val action = ResultFragmentDirections.actionResultFragmentToFormFragment()
			findNavController().navigate(action)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}