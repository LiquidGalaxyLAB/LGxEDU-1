package com.lglab.ivan.lgxeducontroller.legacy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lglab.ivan.lgxeducontroller.R;
import com.lglab.ivan.lgxeducontroller.activities.navigate.POIController;
import com.lglab.ivan.lgxeducontroller.legacy.beans.POI;

import java.util.List;

/**
 * Created by Ivan Josa on 7/07/16.
 */
public class PoisGridViewAdapter extends BaseAdapter {

    private List<POI> poiList;
    private Context context;
    private Activity activity;
    private Handler handler = new Handler();

    public PoisGridViewAdapter(List<POI> poiList, Context context, Activity activity) {
        this.poiList = poiList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.poiList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.poiList.get(i);

    }

    @Override
    public long getItemId(int i) {
        return this.poiList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        final POI currentPoi = this.poiList.get(i);

        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);

        RelativeLayout layout = new RelativeLayout(context);
        layout.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_rounded_grey, null));
        layout.setLayoutParams(params);

        //Rotation Button

        final ImageButton rotatePoiButton = new ImageButton(context);

        ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        rotatePoiButton.setLayoutParams(layoutParams);
        rotatePoiButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

        rotatePoiButton.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_rounded_grey, null));

        rotatePoiButton.setOnClickListener(view13 -> {
            VisitPoiTask visitPoiTask = new VisitPoiTask(currentPoi, true);
            visitPoiTask.execute();
        });

        rotatePoiButton.setEnabled(false);
        rotatePoiButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_autorenew_black_24dp, null));

        RelativeLayout.LayoutParams paramsRotate = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsRotate.width = Math.round(48 * context.getResources().getDisplayMetrics().density);
        paramsRotate.height = Math.round(48 * context.getResources().getDisplayMetrics().density);
        paramsRotate.addRule(RelativeLayout.ALIGN_PARENT_END);
        rotatePoiButton.setLayoutParams(layoutParams);
        rotatePoiButton.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));

        //View POI
        RelativeLayout.LayoutParams paramsView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageButton viewPoiButton = new ImageButton(context);
        paramsView.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsView.addRule(RelativeLayout.ALIGN_START);
        viewPoiButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_place_black_24dp, null));
        viewPoiButton.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_rounded_grey, null));
        viewPoiButton.setLayoutParams(paramsView);
        viewPoiButton.setOnClickListener(view12 -> {
            VisitPoiTask visitPoiTask = new VisitPoiTask(currentPoi, false);
            visitPoiTask.execute();

            disableOtherRotateButtons(viewGroup);
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(() -> {
                try {
                    rotatePoiButton.setEnabled(true);
                    rotatePoiButton.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.whiteGrey)));
                    rotatePoiButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_autorenew_black_24dp, null));
                } catch (Exception e) {

                }
            }, 5000);
        });

        layout.addView(viewPoiButton);

        int maxLengthPoiName = getMaxLength();

        //Poi Name
        RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView poiName = new TextView(context);

        if (currentPoi.getName().length() > maxLengthPoiName) {
            String name = currentPoi.getName().substring(0, maxLengthPoiName) + "...";
            poiName.setText(name);
        } else {
            poiName.setText(currentPoi.getName());
        }

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //Portrait Orientation
            poiName.setTextSize(15);
        } else {
            poiName.setTextSize(20);
        }


        poiName.setMaxLines(2);
        paramsText.addRule(RelativeLayout.CENTER_IN_PARENT);

        poiName.setOnClickListener(view1 -> {
            VisitPoiTask visitPoiTask = new VisitPoiTask(currentPoi, false);
            visitPoiTask.execute();

            disableOtherRotateButtons(viewGroup);
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(() -> {
                try {
                    rotatePoiButton.setEnabled(true);
                    rotatePoiButton.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.whiteGrey)));
                    rotatePoiButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_autorenew_black_24dp, null));
                } catch (Exception e) {

                }
            }, 5000);
        });

        layout.addView(poiName);
        poiName.setLayoutParams(paramsText);

        layout.addView(rotatePoiButton);
        rotatePoiButton.setLayoutParams(paramsRotate);

        return layout;
    }

    private void disableOtherRotateButtons(ViewGroup viewGroup) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            RelativeLayout poiItem = (RelativeLayout) viewGroup.getChildAt(i);
            ImageButton rotateButton = (ImageButton) poiItem.getChildAt(2);
            if (rotateButton.isEnabled()) {
                rotateButton.setEnabled(false);
                rotateButton.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));
                rotateButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_autorenew_black_24dp, null));
            }
        }
    }

    private int getMaxLength() {

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        if (smallestWidth > 800) {
            return 35;
        } else if (smallestWidth <= 800 && smallestWidth >= 600) {
            return 13;
        } else {
            return 10;
        }
    }

    private class VisitPoiTask extends AsyncTask<Void, Void, String> {
        POI currentPoi;
        boolean rotate;
        int rotationAngle = 10;
        int rotationFactor = 1;
        boolean changeVelocity = false;
        private AlertDialog dialog;

        VisitPoiTask(POI currentPoi, boolean rotate) {
            this.currentPoi = currentPoi;
            this.rotate = rotate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog == null) {
                activity.runOnUiThread(() -> {

                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);

                    String message = context.getResources().getString(R.string.viewing) + " " + this.currentPoi.getName() + " " + context.getResources().getString(R.string.inLG);
                    builder.setMessage(message);
                    builder.setView(R.layout.progress);
                    builder.setNegativeButton(context.getResources().getString(R.string.stop_tour), (dialog, id) -> dialog.cancel());

                    dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(true);

                    if (this.rotate) {
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.speedx2), (dialog, which) -> {
                        });

                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, context.getResources().getString(R.string.speeddiv2), (dialog, which) -> {
                        });
                    }


                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), (dialog, which) -> {
                        dialog.dismiss();
                        cancel(true);
                    });
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setOnCancelListener(dialog -> cancel(true));


                    dialog.show();
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_fast_forward_black_24dp, 0, 0);
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_fast_rewind_black_24dp, 0, 0);
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
                        changeVelocity = true;
                        rotationFactor = rotationFactor * 2;

                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(context.getResources().getString(R.string.speedx4));
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setText(context.getResources().getString(R.string.speeddiv2));
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setEnabled(true);
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_fast_rewind_black_24dp, 0, 0);

                        if (rotationFactor == 4) {
                            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    });
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(view -> {
                        changeVelocity = true;
                        rotationFactor = rotationFactor / 2;

                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(context.getResources().getString(R.string.speedx2));
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setText(context.getResources().getString(R.string.speeddiv4));
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_fast_forward_black_24dp, 0, 0);

                        if (rotationFactor == 1) {
                            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setEnabled(false);
                        }
                    });
                });
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                POIController.getInstance().moveToPOI(currentPoi, true);

                if (this.rotate) {
                    boolean isFirst = true;
                    POI newPoi = new POI(currentPoi);
                    while (!isCancelled()) {
                        POIController.getInstance().moveToPOI(newPoi, true);
                        if (isFirst) {
                            isFirst = false;
                            Thread.sleep(7000);
                        } else {
                            Thread.sleep(4000);
                        }

                        newPoi.setHeading(newPoi.getHeading() + this.rotationAngle * this.rotationFactor);
                        while (newPoi.getHeading() > 180) {
                            newPoi.setHeading(newPoi.getHeading() - 360);
                        }
                    }
                }
                return "";
            } catch (InterruptedException e) {
                activity.runOnUiThread(() -> Toast.makeText(context, context.getResources().getString(R.string.visualizationCanceled), Toast.LENGTH_LONG).show());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String success) {
            super.onPostExecute(success);
            if (success != null) {
                if (dialog != null) {
                    dialog.hide();
                    dialog.dismiss();
                }
            }
        }
    }
}
