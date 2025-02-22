package com.lglab.ivan.lgxeducontroller.games.trivia.listener;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.lglab.ivan.lgxeducontroller.R;
import com.lglab.ivan.lgxeducontroller.games.trivia.adapters.ListAdapter;
import com.lglab.ivan.lgxeducontroller.games.trivia.interfaces.IDraggableListener;

import java.util.List;

public class DragListener implements View.OnDragListener {

    private boolean isDropped = false;
    private boolean isDragEnded = true;

    private IDraggableListener listener;

    public DragListener(IDraggableListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if(event.getAction() == DragEvent.ACTION_DRAG_STARTED) {
            isDragEnded = false;
        }
        else if (event.getAction() == DragEvent.ACTION_DROP) {
            isDropped = true;
            int positionTarget = -1;

            View viewSource = (View) event.getLocalState();
            int viewId = v.getId();

            final int question_0_rv = R.id.question_0_rv;

            final int player_circle = R.id.player_circle;

            final int question_name_1 = R.id.question_name_1;
            final int question_1_rv = R.id.question_1_rv;

            final int question_name_2 = R.id.question_name_2;
            final int question_2_rv = R.id.question_2_rv;

            final int question_name_3 = R.id.question_name_3;
            final int question_3_rv = R.id.question_3_rv;

            final int question_name_4 = R.id.question_name_4;
            final int question_4_rv = R.id.question_4_rv;

            switch (viewId) {
                case question_0_rv:
                case player_circle:
                case question_name_1:
                case question_1_rv:
                case question_name_2:
                case question_2_rv:
                case question_name_3:
                case question_3_rv:
                case question_name_4:
                case question_4_rv:

                    RecyclerView target;
                    switch (viewId) {
                        case question_0_rv:
                            target = (RecyclerView)v;
                            break;
                        case question_name_1:
                        case question_1_rv:
                            target = ((ViewGroup)v.getParent()).findViewById(question_1_rv);
                            break;
                        case question_name_2:
                        case question_2_rv:
                            target = ((ViewGroup)v.getParent()).findViewById(question_2_rv);
                            break;
                        case question_name_3:
                        case question_3_rv:
                            target = ((ViewGroup)v.getParent()).findViewById(question_3_rv);
                            break;
                        case question_name_4:
                        case question_4_rv:
                            target = ((ViewGroup)v.getParent()).findViewById(question_4_rv);
                            break;
                        default:
                            target = (RecyclerView) v.getParent();
                            positionTarget = (int) v.getTag();
                    }

                    if (viewSource != null) {
                        RecyclerView source = (RecyclerView) viewSource.getParent();

                        ListAdapter adapterSource = (ListAdapter) source.getAdapter();
                        int positionSource = (int) viewSource.getTag();
                        int sourceId = source.getId();

                        Integer list = adapterSource.getList().get(positionSource);
                        List<Integer> listSource = adapterSource.getList();

                        listSource.remove(positionSource);
                        adapterSource.updateList(listSource);
                        adapterSource.notifyDataSetChanged();

                        ListAdapter adapterTarget = (ListAdapter) target.getAdapter();
                        List<Integer> customListTarget = adapterTarget.getList();
                        if (positionTarget >= 0) {
                            customListTarget.add(positionTarget, list);
                        } else {
                            customListTarget.add(list);
                        }
                        adapterTarget.updateList(customListTarget);
                        adapterTarget.notifyDataSetChanged();

                        listener.draggedViewOnRecyclerView(list, adapterTarget.answerId);

                        /*if (sourceId == rvBottom && adapterSource.getItemCount() < 1) {
                            listener.setEmptyListBottom(true);
                        }
                        if (viewId == tvEmptyListBottom) {
                            listener.setEmptyListBottom(false);
                        }
                        if (sourceId == rvTop && adapterSource.getItemCount() < 1) {
                            listener.setEmptyListTop(true);
                        }
                        if (viewId == tvEmptyListTop) {
                            listener.setEmptyListTop(false);
                        }*/
                    }
                    break;
            }
        }
        else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && !isDragEnded) {
            isDragEnded = true;
            RecyclerView source = v.getRootView().findViewById(R.id.question_0_rv);
            if(source != null) {
                ListAdapter adapterSource = (ListAdapter) source.getAdapter();
                if(adapterSource != null) {
                    adapterSource.enableBorders(false);
                }
            }
        }
        Log.d("draggable", String.valueOf(event.getAction()));

        if (!isDropped && event.getLocalState() != null) {
            ((View) event.getLocalState()).setVisibility(View.VISIBLE);
        }
        return true;
    }
}
