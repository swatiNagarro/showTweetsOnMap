package com.example.showtweetsonmap

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.showtweetsonmap.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private lateinit var navController: NavController
    private lateinit var tweetsViewModel: TweetsViewModelNew
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root

        var mapFragment =
            childFragmentManager.findFragmentById(R.id.twitter_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        tweetsViewModel = ViewModelProvider(this).get(TweetsViewModelNew::class.java)

        binding.btnSearch.setOnClickListener {

            startObservingTweets()
            tweetsViewModel.startStreaming(
                binding.searchEditText.text.toString()
            )
            binding.searchEditText.text.clear()

        }


        startObservingTweets()
    }


    private fun startObservingTweets() {
        tweetsViewModel.tweets.observe(this, {
            Log.e("test", "coming inside start obseving" + it.size)
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney = LatLng(-33.852, 151.211)
        val melbourne = LatLng(-37.8136, 144.9631)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.addMarker(MarkerOptions().position(melbourne).title("Marker in Melbourne"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        location?.let {
            val currentLoc = LatLng(it.latitude, it!!.longitude)
            mMap.addMarker(MarkerOptions().position(currentLoc).title("Marker in Melbourne"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc))
        }

        googleMap.setOnInfoWindowClickListener { marker ->
            navController.navigate(R.id.move_to_tweet_detail_fragment)
        }

    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            );
            return
        }
        fusedLocationClient?.lastLocation
            .addOnSuccessListener { location ->
                this.location = location
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireActivity(), "Failed on getting current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                } else {
                    Toast.makeText(
                        requireActivity(), "You need to grant permission to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}