package com.example.danshal.ui.home

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.danshal.R
import com.example.danshal.databinding.FragmentHomeBinding
import com.example.danshal.models.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val content = arrayListOf<Content>()
    private val giveAway = arrayListOf<GiveAway>()

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var giveAwayAdapter: GiveAwayAdapter

    private var currentEventType: String? = null
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentEventType = getString(R.string.title_event)
        // let the app know that this fragment is expecting menu related callbacks
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val itemToHide = menu.findItem(R.id.action_settings)
        itemToHide.isVisible = false

        menu.findItem(R.id.action_filter).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                openFilterWindow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
//        activity?.viewModelStore?.clear()

        binding.rvEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvGiveAway.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val controller =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_down_to_up)

        // Adds spacing between rv items
        binding.rvEvents.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            ).also { deco ->
                with(ShapeDrawable(RectShape())) {
                    intrinsicHeight = (resources.displayMetrics.density * 32).toInt()
                    alpha = 0
                    deco.setDrawable(this)
                }
            })

        loadData()
        giveAwayAdapter = GiveAwayAdapter(giveAway, ::onGiveAwayClick)
        homeAdapter = HomeAdapter(content, ::onContentClick)
        binding.rvEvents.layoutAnimation = controller
        binding.rvGiveAway.layoutAnimation = controller
        binding.rvEvents.adapter = homeAdapter
        binding.rvGiveAway.adapter = giveAwayAdapter
    }

    private fun loadData() {
        viewModel.currentContentType.observe(viewLifecycleOwner, {
            content.clear()
            viewModel.loadAllContent()
        })

        viewModel.getContent().observe(viewLifecycleOwner, {
            val tempList = arrayListOf<Content>()

            tempList.addAll(it)
            content.addAll(tempList)

            homeAdapter.contentItems = content.sortedWith(compareBy(Content::getSeconds))
            homeAdapter.notifyDataSetChanged()
            if (!viewModel.initLoad) {
                binding.rvEvents.scheduleLayoutAnimation()
            }
        })

        viewModel.getGiveAway().observe(viewLifecycleOwner, {
            giveAway.clear()

            if (it.isNotEmpty()) {
                giveAway.addAll(it)
                giveAwayAdapter.giveAway = giveAway
                giveAwayAdapter.notifyDataSetChanged()
            } else {
                binding.tvGiveaway.visibility = View.GONE
                binding.rvGiveAway.visibility = View.GONE
            }
        })
    }

    private fun openFilterWindow() {
        val dialogItems =
            arrayOf(
                getString(R.string.title_up_event),
                getString(R.string.title_content)
            )
        val checkedItem = dialogItems.indexOf(viewModel.currentContentType.value.toString())

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(resources.getString(R.string.title_filter))
                .setNeutralButton(resources.getString(R.string.action_cancel_filter)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton(resources.getString(R.string.action_filter)) { dialog, which ->
                    // Respond to positive button press
                    viewModel.currentContentType.value = currentEventType
                }
                // Single-choice items (initialized with checked item)
                .setSingleChoiceItems(dialogItems, checkedItem) { dialog, which ->
                    // Respond to item chosen
                    currentEventType = dialogItems[which]
                }
                .show()
        }
    }

    private fun onGiveAwayClick(giveAway: GiveAway) {
        viewModel.setCurrentGiveAway(giveAway)
        GiveawayDialogFragment().newInstance()
            ?.show(parentFragmentManager, "giveaway_dialog_fragment")
    }

    private fun onContentClick(content: Content) {
        viewModel.setCurrentContent(content)

        if (content.postType == Content.TYPE.EVENT) {
            EventDialogFragment().newInstance()?.show(parentFragmentManager, "event_fragment")
        } else {
            PostDialogFragment().newInstance()?.show(parentFragmentManager, "post_fragment")
        }
    }
}