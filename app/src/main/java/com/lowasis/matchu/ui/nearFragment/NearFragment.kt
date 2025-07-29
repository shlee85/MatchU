package com.lowasis.matchu.ui.nearFragment

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lowasis.matchu.R
import com.lowasis.matchu.databinding.FragmentNearBinding


class NearFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, LocationListener {
    companion object {
        private val TAG = NearFragment::class.java.simpleName
    }

    private var gMap : GoogleMap? = null

    private var binding: FragmentNearBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()
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

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)

                // 지도에 마커 추가
                binding?.mapView?.getMapAsync { googleMap ->
                    googleMap.clear() // 기존 마커 제거
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(currentLatLng)
                            .title("내 위치")
                    )

                    // 카메라 위치 이동 (애니메이션-부드럽게)
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)
                    )
                }
            } else {
                // 위치 못 받아왔을 때 처리 (예: 토스트)
                Toast.makeText(requireContext(), "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}