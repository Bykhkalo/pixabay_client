package com.example.viewimages.view.fragments

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewimages.R
import com.example.viewimages.model.ImageSearchRequest
import com.example.viewimages.repository.MainRepository
import com.example.viewimages.utils.DebugUtils.TAG
import com.example.viewimages.utils.NavigationHost
import com.example.viewimages.view.ImageOverlayView
import com.example.viewimages.view.activity.ImageListActivity
import com.example.viewimages.view.recycler.ImageAdapter
import com.example.viewimages.viewmodel.ImagesViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.memebattle.pwc.util.NetworkState
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.fragment_image_list.*
import java.util.*


class ImageListFragment : Fragment(), ImageAdapter.ActivityCallbackOnClick {

    private lateinit var viewModel: ImagesViewModel

    private lateinit var errorTextView: TextView
    private lateinit var loadingProgressBar: ProgressBar

    private lateinit var toolbar: Toolbar
    private lateinit var navButtonIV: ImageView

    private lateinit var imageRecyclerView: RecyclerView

    private var currentImageUrl: String? = null
    private var currentSearchRequest = ImageSearchRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("searchKey", this, { requestKey, result ->

            Log.d(TAG, "onResult: RESULT")

            val imageSearchRequest = result.getSerializable("searchRequest") as ImageSearchRequest
            currentSearchRequest = imageSearchRequest
            currentSearchRequest.isNewRequest = true

        })

        savedInstanceState?.let {
            currentImageUrl = savedInstanceState.getString("imageUrl")
            currentSearchRequest = savedInstanceState.getSerializable("searchRequest") as ImageSearchRequest
            Log.d(TAG, "onCreate: Restored")
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }


    private fun init(view: View) {
        viewModel = getViewModel()

        errorTextView = view.findViewById(R.id.txt_error)
        loadingProgressBar = view.findViewById(R.id.loading_progress_bar)

        toolbar = view.findViewById(R.id.image_list_toolbar)
        navButtonIV = view.findViewById(R.id.nav_image_view)

        imageRecyclerView = view.findViewById(R.id.image_list)

        if (currentImageUrl != null) {
            showImageCallback(currentImageUrl)
        }

        initToolBar(view);
        initImageRecyclerView()
        initSwipeToRefresh()
    }

    private fun initToolBar(view: View) {
        val drawer = (activity as ImageListActivity?)!!.getsNavigationDrawer()

        if (drawer != null) {
            drawer.drawerListener = object : SNavigationDrawer.DrawerListener {
                override fun onDrawerOpening() {
                    val stateSet = intArrayOf(android.R.attr.state_checked * if (drawer.isDrawerOpen) -1 else 1)
                    navButtonIV.setImageState(stateSet, true)
                }

                override fun onDrawerClosing() {
                    val stateSet = intArrayOf(android.R.attr.state_checked * if (drawer.isDrawerOpen) -1 else 1)
                    navButtonIV.setImageState(stateSet, true)
                }

                override fun onDrawerOpened() {}
                override fun onDrawerClosed() {}
                override fun onDrawerStateChanged(newState: Int) {}
            }

            navButtonIV.setOnClickListener { if (drawer.isDrawerOpen) drawer.closeDrawer() else drawer.openDrawer() }

            drawer.onMenuItemClickListener = SNavigationDrawer.OnMenuItemClickListener {

                val imageSearchRequest = ImageSearchRequest()
                imageSearchRequest.isNewRequest = true

                when (it) {
                    0 -> {
                        imageSearchRequest.imageType = "all"
                    }
                    1 -> {
                        imageSearchRequest.imageType = "photo"
                    }
                    2 -> {
                        imageSearchRequest.imageType = "illustration"
                    }
                    3 -> {
                        imageSearchRequest.imageType = "vector"
                    }
                }

                setUpSearchRequest(imageSearchRequest)
            }
        }

        val searchView: SearchView = toolbar.menu.findItem(R.id.bar_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.isNotEmpty()) {
                        val inputMethodManager: InputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0);
                        searchView.clearFocus();

                        val imageSearchRequest = ImageSearchRequest()
                        imageSearchRequest.isNewRequest = true;
                        imageSearchRequest.imageType = currentSearchRequest.imageType
                        imageSearchRequest.query = query
                        setUpSearchRequest(imageSearchRequest)
                    }
                }

                return true;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false;
            }
        })

        val editFilterItem: MenuItem = toolbar.menu.findItem(R.id.bar_edit_filter)
        editFilterItem.setOnMenuItemClickListener { item ->
            val args = Bundle()
            args.putSerializable("searchRequest", currentSearchRequest)

            val fragment = ImageFilterFragment()
            fragment.arguments = args

            (activity as NavigationHost?)?.navigateTo(fragment, true)
            true
        }

    }

    private fun setUpSearchRequest(imageSearchRequest: ImageSearchRequest){
        currentSearchRequest = imageSearchRequest
        viewModel.begin(currentSearchRequest)
        currentSearchRequest.isNewRequest = false
    }

    private fun initImageRecyclerView() {
        val adapter = ImageAdapter({
            viewModel.retry()
        }, this)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) image_list.layoutManager =
                LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)
        else image_list.layoutManager = LinearLayoutManager(requireContext())

        image_list.adapter = adapter
        viewModel.images.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        viewModel.networkState.observe(viewLifecycleOwner, {
            adapter.setNetworkState(it)
        })

    }

    override fun onResume() {
        super.onResume()

        setUpSearchRequest(currentSearchRequest)
    }

    private fun initSwipeToRefresh() {
        viewModel.refreshState.observe(viewLifecycleOwner, {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
        swipe_refresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun getViewModel(): ImagesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repo = MainRepository(requireContext(), 10, 3)
                @Suppress("UNCHECKED_CAST")
                return ImagesViewModel(repo) as T
            }
        })[ImagesViewModel::class.java]
    }

    override fun showImageCallback(imageUrl: String?) {
        //This method for recyclerView item.onClick call

        val context: Context? = context

        if (imageUrl != null && !imageUrl.isEmpty()) {
            currentImageUrl = imageUrl

            val config = ImagePipelineConfig.newBuilder(context)
                    .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
                    .setResizeAndRotateEnabledForNetwork(true)
                    .setDownsampleEnabled(true)
                    .build()

            Fresco.initialize(context, config)

            val imageOverlayView = ImageOverlayView(context, imageUrl)

            ImageViewer.Builder(context, Arrays.asList(imageUrl))
                    .hideStatusBar(true)
                    .allowSwipeToDismiss(true)
                    .setOnDismissListener { currentImageUrl = null }
                    .setOverlayView(imageOverlayView)
                    .show()
        } else {
            Toast.makeText(context, "Error with url", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("imageUrl", currentImageUrl)
        outState.putSerializable("searchRequest", currentSearchRequest)
    }

}