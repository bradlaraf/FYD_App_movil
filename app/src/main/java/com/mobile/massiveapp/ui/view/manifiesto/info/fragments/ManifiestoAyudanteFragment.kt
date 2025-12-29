package com.mobile.massiveapp.ui.view.manifiesto.info.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentManifiestoAyudanteBinding


class ManifiestoAyudanteFragment : Fragment() {
    private var _binding:FragmentManifiestoAyudanteBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManifiestoAyudanteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}