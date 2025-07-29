package com.lowasis.matchu.ui.nearFragment

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.lowasis.matchu.R
import com.lowasis.matchu.databinding.FragmentNearBinding

class NearFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, LocationListener {
    companion object {
        private val TAG = NearFragment::class.java.simpleName
    }

    private var gMap : GoogleMap? = null

    private var binding: FragmentNearBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        binding = FragmentNearBinding.inflate(inflater, container,  false)

        binding?.mapView?.onCreate(savedInstanceState)
        binding?.mapView?.getMapAsync(this)   //지도 초기화

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onMapReady(map: GoogleMap) {
        Log.i(TAG, "onMapReady!!")

        gMap = map

        //지도 초기 위치 설정 (예:서울)
        val location = LatLng(37.5665, 126.9788)    //기본 좌표 설정
        gMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.i(TAG, "onMarkerClick!!")

        return false
    }

    override fun onMapClick(p0: LatLng) {
        Log.i(TAG, "onMapClick!!")
    }

    override fun onLocationChanged(p0: Location) {
        Log.i(TAG, "onLocationChanged!!")
    }


    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")

        binding?.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause()")

        binding?.mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy()")

        binding?.mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.i(TAG, "onLowMemory()")

        binding?.mapView?.onLowMemory()
    }
}