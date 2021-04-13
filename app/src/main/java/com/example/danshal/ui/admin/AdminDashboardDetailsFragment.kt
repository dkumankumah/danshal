package com.example.danshal.ui.admin

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.danshal.MainActivity
import com.example.danshal.databinding.AdminDashboardDetailsFragmentBinding
import com.example.danshal.models.Content

class AdminDashboardDetailsFragment : Fragment() {
    private lateinit var adminDashboardDetailsViewModel: AdminDashboardViewModel

    private var _binding: AdminDashboardDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val contentList = arrayListOf<Content>()
    private val contentDetailsAdapter = AdminContentAdapter(contentList, ::removeContentItem, ::editContentItem)

    private val viewModel: AdminDashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adminDashboardDetailsViewModel =
            ViewModelProvider(this).get(AdminDashboardViewModel::class.java)

        _binding = AdminDashboardDetailsFragmentBinding.inflate(inflater, container, false)

        (activity as MainActivity).supportActionBar?.title = "Admin ${viewModel.detailContentType}s"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun editContentItem(content: Content) {
        when(viewModel.detailContentType){
            Content.TYPE.EVENT -> {
                Log.i("DASHBOARD", "Edit event ${content.title}")
            }
            Content.TYPE.GIVEAWAY -> {
                Log.i("DASHBOARD", "Edit give away ${content.title}")
            }
            Content.TYPE.POST -> {
                Log.i("DASHBOARD", "Edit post ${content.title}")
            }
        }
    }

    private fun removeContentItem(content: Content) {
        when(viewModel.detailContentType){
            Content.TYPE.EVENT -> {
                viewModel.removeEvent(content.id)
            }
            Content.TYPE.GIVEAWAY -> {
                Log.i("DASHBOARD", "Remove give away ${content.title}")
                viewModel.removeGiveaway(content.id)
            }
            Content.TYPE.POST -> {
                Log.i("DASHBOARD", "Remove post ${content.title}")
                viewModel.removePost(content.id)
            }
        }
        // Remove from content list
        contentList.remove(content)
        contentDetailsAdapter.notifyDataSetChanged()
    }

    private fun initViews() {
        binding.rvContent.layoutManager = GridLayoutManager(context, 1)
        binding.rvContent.adapter = contentDetailsAdapter

        binding.rvContent.addItemDecoration(
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

        when (viewModel.detailContentType) {
            Content.TYPE.EVENT -> {
                viewModel.eventListData.observe(viewLifecycleOwner, Observer { list ->
                    contentList.addAll(list)
                })
            }
            Content.TYPE.POST -> {
                viewModel.postListData.observe(viewLifecycleOwner, Observer { list ->
                    contentList.addAll(list)
                })
            }
            Content.TYPE.GIVEAWAY -> {
                viewModel.giveawayListData.observe(viewLifecycleOwner, Observer { list ->
                    contentList.addAll(list)
                })
            }
        }

        contentDetailsAdapter.notifyDataSetChanged()
    }
}