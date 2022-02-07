package com.velagissellint.a65.all.presentation.contactDetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.velagissellint.a65.all.containersDi.ContainerAppContainer
import com.velagissellint.a65.all.presentation.ViewModelFactory
import com.velagissellint.a655.R
import javax.inject.Inject

@Suppress("TooManyFunctions")
class ContactDetailsFragment : Fragment(), CompoundButton.OnCheckedChangeListener,
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener {
    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var contactDetailsViewModel: ContactDetailsViewModel
    private lateinit var mMap: GoogleMap
    private var markerLast: Marker? = null
    private var id: Int? = null
    private var listMarkersFromDb = mutableListOf<Marker>()
    private var listForDirection = mutableListOf<Marker>()
    private var polyline: Polyline? = null
    private val fragmentHelper = FragmentHelper()

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var switchAlarm: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ID_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as ContainerAppContainer).appContainer()
            ?.plusPersonDetailsComponent()?.inject(this)
        activity?.title = getString(R.string.title_for_ContactDetailsFragment)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        contactDetailsViewModel =
            ViewModelProvider(this, factory).get(ContactDetailsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactDetailsViewModel.getContact(id)

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        switchAlarm = requireView().findViewById(R.id.SwitchBirthday)
        switchAlarm?.setOnCheckedChangeListener(this)
        observeProgressBar(progressBar)
        makeDirektion()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapp) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val buttonBuildDirection = requireView().findViewById<Button>(R.id.buildDirection)
        buttonBuildDirection.setOnClickListener {
            buttonDirectionClick()
        }

        val buttonAll = requireView().findViewById<Button>(R.id.buttonAll)
        buttonAll.setOnClickListener {
            buttonAllClick()
        }
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            observeViewModel()
        } else {
            getPermission()
        }
    }

    private fun observeProgressBar(progressBar: ProgressBar) {
        contactDetailsViewModel
            .isLoadingPublic
            .observe(viewLifecycleOwner, {
                progressBar.isVisible = it
            })
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun observeViewModel() {
        contactDetailsViewModel.contact.observe(viewLifecycleOwner, {
            val ivPhoto = requireView().findViewById<ImageView>(R.id.imvPhoto)
            val tvName = requireView().findViewById<TextView>(R.id.tvName)
            val tvPhoneNumber = requireView().findViewById<TextView>(R.id.phoneNumber)
            val email = requireView().findViewById<TextView>(R.id.email)
            val description = requireView().findViewById<TextView>(R.id.description)
            val switchNotify = requireView().findViewById<Switch>(R.id.SwitchBirthday)
            it?.let { contact ->
                activity?.runOnUiThread {
                    ivPhoto.setImageURI(Uri.parse(contact.imageResource))
                    tvName.text = contact.fullName
                    tvPhoneNumber.text = contact.phoneNumber
                    email.text = contact.email
                    description.text = contact.description
                    switchNotify.isChecked = fragmentHelper.isAlarmSet(requireContext(), id)
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                observeViewModel()
            }
        } else {
            getPermission()
            Toast.makeText(
                requireContext(),
                getString(R.string.no_access_to_contacts),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked)
            id?.let {
                contactDetailsViewModel.contact.value?.let { detailContact ->
                    contactDetailsViewModel.onReciver(
                        it,
                        detailContact
                    )
                }
            }
        else id?.let {
            contactDetailsViewModel.offReciver(it)
        }
    }

    override fun onDestroyView() {
        switchAlarm = null
        super.onDestroyView()
    }

    private fun getPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSIONS_REQUEST_READ_CONTACTS
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val settingsOfMap = mMap.uiSettings
        settingsOfMap.isZoomControlsEnabled = true
        settingsOfMap.isRotateGesturesEnabled = true
        mMap.setOnMapLongClickListener(this)
        mMap.setOnMarkerClickListener(this)
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_map))
        contactDetailsViewModel.geoFromDb.observe(viewLifecycleOwner, { geocoderMetaData ->
            if (geocoderMetaData != null) {
                val loc = geocoderMetaData.let {
                    fragmentHelper.convertStringToLAtLng(it)
                }
                markerLast =
                    loc.let { latLng -> MarkerOptions().position(latLng).title(geocoderMetaData.text) }
                        .let { markerOptions -> mMap.addMarker(markerOptions) }

                loc.let { latLng -> CameraUpdateFactory.newLatLng(latLng) }
                    .let { cameraUpdate -> mMap.moveCamera(cameraUpdate) }
            }
        })
    }

    override fun onMapLongClick(latLng: LatLng) {
        val position = latLng.longitude.toString() + "," + latLng.latitude.toString()
        contactDetailsViewModel.loadGeo(position)
        contactDetailsViewModel.geoCoderMetaDataLiveData.observe(viewLifecycleOwner, {
            markerLast?.remove()
            markerLast = mMap.addMarker(
                MarkerOptions().position(latLng)
                    .title(it.text)
            )
        })
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (contactDetailsViewModel.isModeDirection) {
            listForDirection.add(marker)
            fragmentHelper.setColorToMarker(marker, BitmapDescriptorFactory.HUE_BLUE)
            if (contactDetailsViewModel.howMarkersClickInModeDirections == 1) {
                contactDetailsViewModel.loadDirection(
                    fragmentHelper.latLngToString(listForDirection[0].position),
                    fragmentHelper.latLngToString(listForDirection[1].position)
                )
            }
            ++contactDetailsViewModel.howMarkersClickInModeDirections
        } else marker.showInfoWindow()
        return true
    }

    private fun makeDirektion() {
        contactDetailsViewModel.resultDirectionLiveData.observe(
            viewLifecycleOwner,
            { resultDirection ->
                if (resultDirection.routes?.isNotEmpty() == true) {
                    val listOfPointsForPolyline =
                        PolyUtil.decode(resultDirection.routes?.get(0)?.overviewPolyline?.points)
                    val line = PolylineOptions()
                    line.width(WIDTH_FOR_POLYLINE).color(R.color.black)
                    val latLngBuilder = LatLngBounds.Builder()
                    for (i in listOfPointsForPolyline) {
                        line.add(i)
                        latLngBuilder.include(i)
                    }
                    polyline = mMap.addPolyline(line)
                    val size = resources.displayMetrics.widthPixels
                    val latLngBounds = latLngBuilder.build()
                    val track = CameraUpdateFactory.newLatLngBounds(
                        latLngBounds,
                        size,
                        size,
                        PADDING_FOR_DIRECTION_CAMERA
                    )
                    listOfPointsForPolyline.clear()
                    mMap.moveCamera(track)
                } else {
                    resetDirection()
                }
            })
    }

    private fun resetDirection() {
        contactDetailsViewModel.howMarkersClickInModeDirections = 0
        for (i in listForDirection) {
            if (i.position == markerLast?.position)
                fragmentHelper.setColorToMarker(i, BitmapDescriptorFactory.HUE_RED)
            else
                fragmentHelper.setColorToMarker(i, BitmapDescriptorFactory.HUE_GREEN)
        }
        polyline?.remove()
        polyline = null
        contactDetailsViewModel.isModeDirection = false
        listForDirection.clear()
    }

    private fun buttonDirectionClick() {
        if (contactDetailsViewModel.isModeDirection) {
            Toast.makeText(
                requireContext(),
                getString(R.string.exit_from_direction_mode),
                Toast.LENGTH_SHORT
            )
                .show()
            resetDirection()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.enter_to_direction_mode),
                Toast.LENGTH_SHORT
            )
                .show()
            contactDetailsViewModel.isModeDirection = true
        }
    }

    private fun buttonAllClick() {
        if (contactDetailsViewModel.isAllTouch) {
            if (contactDetailsViewModel.howMarkersClickInModeDirections > 0)
                resetDirection()
            for (i in listMarkersFromDb) {
                i.remove()
            }
            listMarkersFromDb.clear()
            contactDetailsViewModel.isAllTouch = false
        } else {
            contactDetailsViewModel.listGeoFromDb.observe(viewLifecycleOwner, {
                for (i in it) {
                    val loc = fragmentHelper.convertStringToLAtLng(i)
                    mMap.addMarker(
                        MarkerOptions().position(loc)
                            .title(i.text).icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_GREEN
                                )
                            )
                    )
                        ?.let { marker ->
                            listMarkersFromDb.add(
                                marker
                            )
                        }
                }
                markerLast?.let { marker -> listMarkersFromDb.add(marker) }
                val builder = LatLngBounds.Builder()
                for (marker in listMarkersFromDb) {
                    builder.include(marker.position)
                }
                val bounds = builder.build()
                val padding = PADDING
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                mMap.animateCamera(cu)
                listMarkersFromDb.remove(markerLast)
            })
            contactDetailsViewModel.isAllTouch = true
        }
    }

    companion object {
        private const val ID_ARG = "id"
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
        private const val PADDING = 100
        private const val WIDTH_FOR_POLYLINE = 15f
        private const val PADDING_FOR_DIRECTION_CAMERA = 25

        fun newInstance(id: Int) = ContactDetailsFragment().apply {
            arguments = bundleOf(ID_ARG to id)
        }
    }
}
