package dev.rohail.debuggy.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.rohail.debuggy.R
import dev.rohail.debuggy.databinding.FragmentHeadersBinding
import dev.rohail.debuggy.interceptor.utils.OkHttpUtils

/**
 * A simple [Fragment] subclass.
 * Use the [HeadersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HeadersFragment : Fragment() {

    private lateinit var logMapper: Map<String, List<String>>

    private var _binding: FragmentHeadersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHeadersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBody()
        setUpShareActionBtn()
    }

    private fun setUpBody() {
        binding.headersDetail.text = OkHttpUtils.stringifyHeaders(logMapper)
    }

    private fun setUpShareActionBtn() {
        binding.headersShareContent.setOnClickListener {
            val shareIntent: Intent =
                createShareIntent(
                    getString(R.string.app_name),
                    binding.headersDetail.text.toString()
                )
            requireContext().startActivity(shareIntent)
        }
    }

    private fun createShareIntent(subject: String?, shareBody: String?): Intent {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        return sharingIntent
    }

    companion object {
        @JvmStatic
        fun newInstance(headers: Map<String, List<String>>) =
            HeadersFragment().apply {
                logMapper = headers
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}