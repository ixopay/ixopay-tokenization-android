package com.ixopay.tokenizationdemo.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.ixopay.tokenizationdemo.R
import com.ixopay.tokenizationdemo.databinding.FragmentFormBinding
import com.ixopay.tokenizationdemo.util.TokenizeResult

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FormFragment : Fragment() {

	private var _binding: FragmentFormBinding? = null

	// This property is only valid between onCreateView and onDestroyView.
	private val binding get() = _binding!!

	private val viewModel: FormViewModel by navGraphViewModels(R.id.nav_graph)

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentFormBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
			binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
			binding.buttonTokenize.isEnabled = !isLoading
		}

		viewModel.tokenizeResult.observe(viewLifecycleOwner) { tokenizeResult ->
			if (tokenizeResult == null) {
				return@observe
			}

			if (tokenizeResult is TokenizeResult.Error) {
				Snackbar.make(view, "Error: ${tokenizeResult.error}", Snackbar.LENGTH_LONG).show()
			} else if (tokenizeResult is TokenizeResult.InputError) {
				Snackbar.make(view, "Input verification failed", Snackbar.LENGTH_LONG).show()
				return@observe
			}

			val action = FormFragmentDirections.actionFormFragmentToResultFragment(tokenizeResult)
			findNavController().navigate(action)
			viewModel.clearTokenizeResult()
		}

		binding.buttonTokenize.setOnClickListener {
			viewModel.tokenize()
		}

		binding.cvv.setOnEditorActionListener { v, actionId, event ->
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				binding.buttonTokenize.performClick()
				return@setOnEditorActionListener true
			}
			false
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}