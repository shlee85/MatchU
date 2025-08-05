package com.lowasis.matchu.ui.nearFragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.Camera
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
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lowasis.matchu.R
import com.lowasis.matchu.databinding.DialogUserInfoBinding
import com.lowasis.matchu.databinding.FragmentNearBinding


class NearFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, LocationListener {
    companion object {
        private val TAG = NearFragment::class.java.simpleName
    }

    private var gMap : GoogleMap? = null

    private var binding: FragmentNearBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: LatLng? = null //현재 위치 설정

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        binding = FragmentNearBinding.inflate(inflater, container,  false)

        binding?.mapView?.onCreate(savedInstanceState)
        binding?.mapView?.getMapAsync(this)   //지도 초기화

        binding?.btnRefreshLcation?.setOnClickListener {
            Log.i(TAG, "btnRefreshLocation click!")

            getCurrentLocationMoveCamera()
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
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
                Log.i(TAG, "getCurrentLocation()")
                val currentLatLng = LatLng(location.latitude, location.longitude)

                currentLocation = currentLatLng

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

                showUserMarker()    //가 데이터 마커를 표시 해준다.
            } else {
                // 위치 못 받아왔을 때 처리 (예: 토스트)
                Toast.makeText(requireContext(), "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentLocationMoveCamera() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        //현재 권한 체크
        if(ActivityCompat.checkSelfPermission(
            requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if(location != null) {
                val latLng = LatLng(location.latitude, location.longitude)

                gMap?.clear()    //기존 마커 제거
                gMap?.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("내 위치")
                )

                gMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } else {
                Toast.makeText(context, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //유저 가데이터 정의
    private fun createFakeUserMarker(): List<UserMarkerData> {
        Log.i(TAG, "createFakeUserMarker()")
        Log.i(TAG, "currentLocation [$currentLocation]")
        val base = currentLocation ?: LatLng(37.5665, 126.9780) // fallback

        fun randomOffset() = (Math.random() - 0.5) * 0.006  // 약 ±0.003

        Log.i(TAG, "CreateFakeUserMarker!!")

        return listOf(
            UserMarkerData("지민", base.offset(randomOffset(), randomOffset()), "여", "24", "혼밥 친구 구해요!", R.drawable.girl1),
            UserMarkerData("현수", base.offset(randomOffset(), randomOffset()), "남", "비공개", "영화 같이 보실 분?", R.drawable.girl2),
            UserMarkerData("수아", base.offset(randomOffset(), randomOffset()), "여", "27", "고양이 좋아하는 분이 좋아요", R.drawable.girl3),
            UserMarkerData("민재", base.offset(randomOffset(), randomOffset()), "남", "30", "카페 데이트 좋아합니다 ☕", R.drawable.girl4),
            UserMarkerData("유진", base.offset(randomOffset(), randomOffset()), "여", "26", "드라이브 좋아요!", R.drawable.girl5)
        )
    }

    private fun LatLng.offset(latOffset: Double, lngOffset: Double): LatLng {
        return LatLng(this.latitude + latOffset, this.longitude + lngOffset)
    }

    //마커 표시 + 클릭 리스너 설정
    private fun showUserMarker() {
        val userList = createFakeUserMarker()

        userList.forEach { user ->
            Log.i(TAG, "addMarker!!!")
            val marker = gMap?.addMarker(
                MarkerOptions()
                    .position(user.latLng)
                    .title(user.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE
                    ))
            )
            marker?.tag = user

            Log.i(TAG, "showMarkar = ${marker?.tag}")
        }

        gMap?.setOnMarkerClickListener { marker ->
            val user = marker.tag as? UserMarkerData
            user?.let {
                showUserPopup(it)
            }
            true
        }
    }

    // 팝업 다이얼로그 표시
    private fun showUserPopup(user: UserMarkerData) {
        val binding = DialogUserInfoBinding.inflate(layoutInflater)

        binding.imgProfile.setImageResource(user.profileResId)
        binding.txtName.text = user.name
        binding.txtGenderAge.text = "${user.gender} / ${user.age}"
        binding.txtBio.text = user.bio

        //AlertDialog에 ViewBinding으로 만든 뷰 삽입
        AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton("닫기", null)
            .show()
    }

    // 데이터 클래스 정의
    data class UserMarkerData(
        val name: String,
        val latLng: LatLng,
        val gender: String,
        val age: String,
        val bio: String,
        val profileResId: Int
    )
}