package com.harryfultz.studentmanager.appConfigAndDB;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.hanks.library.AnimateCheckBox;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.Objects;

class UiThread {

    private Activity a;

     UiThread(Activity a) {
        this.a = a;
    }

     void checkObject(final AnimateCheckBox animateCheckBox, final CheckedTextView checkedTextView, final DialogPlus checkBoxDialog, final String action, final boolean state) {
        if (animateCheckBox == null && checkedTextView == null && checkBoxDialog == null) {
            MyDynamicToast.warningMessage(a, "All objects are null!");
        } else if (animateCheckBox != null) {
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animateCheckBox.setChecked(state); // Deselekton diten
                }
            });
        } else if (checkedTextView != null) {
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkedTextView.setChecked(state);  // Deselekton nxenesin
                }
            });
        } else {
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Objects.equals(action, "dismiss")) {
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkBoxDialog.dismiss();
                            }
                        });
                    } else if (Objects.equals(action, "show")) {
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkBoxDialog.show();
                            }
                        });
                    }
                }
            });
        }
    }

     void animateFab(final FloatingActionButton fab) {
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fab.setScaleX(0);
                fab.setScaleY(0);
                fab.animate().setDuration(900).scaleX(1).scaleY(1).start();
            }
        });
    }

     void animateCompletedImageView(final Activity a, final ImageView imageView){
        new CountDownTimer(2000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            imageView.animate().setDuration(900).scaleX(1).scaleY(1).start();
                    }
                });
            }

            @Override
            public void onFinish() {
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.animate().setDuration(900).scaleX(0).scaleY(0).start();
                    }
                });
            }
        }.start();
    }


}
