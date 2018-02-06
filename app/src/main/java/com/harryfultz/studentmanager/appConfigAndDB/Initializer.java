package com.harryfultz.studentmanager.appConfigAndDB;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.eminayar.panter.DialogType;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.interfaces.OnSingleCallbackConfirmListener;
import com.harryfultz.studentmanager.R;
import com.harryfultz.studentmanager.activities.Mungesa;
import com.harryfultz.studentmanager.customAdapters.CheckBoxCustomAdapter;
import com.harryfultz.studentmanager.customAdapters.CustomAdapter;
import com.harryfultz.studentmanager.models.CheckBoxModel;
import com.harryfultz.studentmanager.models.Model;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.hanks.library.AnimateCheckBox;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Initializer implements AdapterView.OnItemClickListener, View.OnClickListener, OnDismissListener, AdapterView.OnItemLongClickListener {

    private Activity a;
    private CustomAdapter adapter;
    private CheckBoxCustomAdapter checkBoxListAdapter;
    private Model[] modelItems;
    private CheckBoxModel[] cbm;
    private DialogPlus checkBoxDialog;
    private CheckedTextView ctv[] = new CheckedTextView[23];
    private SmsManager smsM;
    private FloatingActionButton mFab = null;
    private UiThread uiThread;
    private AnimateCheckBox animateCheckBox;
    private ImageView imageView;
    private DatabaseHelper dbHelper;
    private Context context;
    private TextView noStudentTextView;


    private String messageStatus = "";

    // Integers
    private int x = 0; // <-- Counts the numbers of selected students to send message
    private int studentPosition; // <-- stores the position of a signle student
    private int y = 0; // <-- Counts the numbers of selected days the student has been absent
    private int daysPosition = 0; // <-- stores the position of a day a student has been absent
    private int hoursPosition = 0; // <-- stores the position of an hour a student has been absent


    public Initializer(Activity a, Context context) {
        this.a = a;
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        uiThread = new UiThread(a);

    }


    public void initializeAllViews() {
        initializeTextiVew(); // <- Initiallize text that show the inexistence of students
        initializeStudentListView(); // <-- Initialize the list
        initializeSmsManager(); // <-- initialize the SMS Android API
        fabInitializer(); // <-- Initialize Floating Action Button
        initializeImageView(); // <- Initialize imageView
    }

    // Function that determines what happens when an item is clicked
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // Students

        y = 0;  // Kur klikon një nxënës numri i ditëve të munguara bëhet zero
        this.studentPosition = position; // Këtu vendoset pozicioni i nxënësit të selektuar
        modelItems[studentPosition].setDays(5); // Krijon një array me ditët që mund të ketë munguar nxënësi
        modelItems[studentPosition].setAbsences(5, 7);
        adapter.cb = (CheckedTextView) view.findViewById(R.id.checkBox1); // <-- CheckedTextView object is created by changing value of cb which is found in the other class
        ctv[studentPosition] = adapter.cb;


        if (ctv[studentPosition].isChecked()) {  // Nqs një nxënës është i check-uar
            deselectStudent(studentPosition);    // deselektoje
        } else {                                 // Nqs një nxënës nuk është i check-uar
            selectStudent(studentPosition);      // selektoje
            checkBoxDialog();
        }

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        MaterialStyledDialog dialog = new MaterialStyledDialog(a);
        dialog.setTitle("Kujdes!");
        dialog.setDescription("Jeni i sigurt që doni të fshini nxënësin " + modelItems[position].getName() + " ?");
        dialog.setIcon(R.drawable.warning);
        dialog.withDialogAnimation(true, Duration.FAST);
        dialog.setHeaderColor(R.color.warning);
        dialog.setCancelable(false);
        dialog.setPositive("Po", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dbHelper.deleteStudent(modelItems[position].getName());
                a.finish();
                a.startActivity(new Intent(a, Mungesa.class));

            }
        });
        dialog.setNegative("Jo", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return false;
    }

    private void initializeTextiVew() {
        noStudentTextView = (TextView) a.findViewById(R.id.noStudentId);
        Cursor data = dbHelper.getListContent();
        int numRows = data.getCount();
        if (numRows == 0)
            noStudentTextView.setVisibility(View.VISIBLE);
        else
            noStudentTextView.setVisibility(View.INVISIBLE);
    }

    // Function to send messages multiple times
    private void sendMultipleMessages() {

        if(!AirplaneModeKt.AirplaneMode(context)){ // Checks whether the airplane mode is on or off
            MessageStorage messageStorage = new MessageStorage(a);

            for (int i = 0; i < modelItems.length; i++) {

                if (modelItems[i].getValue() != 0) {

                    printDays(i); // <- Forms the message to send

                    String message = messageStorage.getAbsenceMessage() + modelItems[i].getSms();
                    ArrayList<String> parts = smsM.divideMessage(message);
                    try {
                        smsM.sendMultipartTextMessage(modelItems[i].getPhoneNumber(), null, parts, null, null); // <-- Function to send message
                        messageStatus = "U dërgua.";
                        checkMessageStatus(messageStatus);
                        deselectStudent(i);

                    } catch (Exception e) {
                        e.printStackTrace();
                        messageStatus = "Dështoi!";
                        checkMessageStatus(messageStatus);
                        deselectStudent(i);
                    }

                }

            }
        }else{
            MyDynamicToast.errorMessage(a, "Çaktivizoni \"Airplane Mode\" në celularë");
        }


    }


    // Function that checks the status of the message if it is sent or not
    private void checkMessageStatus(String msgSts) {
        switch (msgSts) {
            case "Dështoi!":
                stateNotify(false);
                break;
            case "U dërgua.":
                stateNotify(true);
                break;

        }
    }


    // notifies the user if messages are sent depending on the state
    private void stateNotify(boolean state) {
        if (state) {
            MyDynamicToast.successMessage(context, messageStatus); // <-- Warn user that messages has been sent!
            imageView.setImageResource(R.drawable.completed);
            uiThread.animateCompletedImageView(a, imageView);
        } else {
            MyDynamicToast.warningMessage(context, messageStatus); // <-- Warn user if message is not sent
            imageView.setImageResource(R.drawable.wrong);
            uiThread.animateCompletedImageView(a, imageView);
        }
    }

    // creates a checkbox dialog with its characteristics
    private void checkBoxDialog() {
        populateCheckBoxListView();
        checkBoxDialog = DialogPlus.newDialog(a)
                .setAdapter(checkBoxListAdapter)
                .setHeader(R.layout.checkboxlistheader)
                .setOnItemClickListener(new OnItemClickListener() {
                                            @Override
                                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                                daysPosition = position;
                                                checkBoxListAdapter.checkBox = (AnimateCheckBox) view.findViewById(R.id.checkBoxButtonId);
                                                animateCheckBox = checkBoxListAdapter.checkBox;

                                                if (animateCheckBox != null) {
                                                    if (animateCheckBox.isChecked()) {
                                                        deselectDay();
                                                    } else {
                                                        selectDay();
                                                    }
                                                }
                                            }
                                        }
                )
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)

                .setCancelable(true)

                .setOnBackPressListener(new OnBackPressListener() {
                    @Override
                    public void onBackPressed(DialogPlus dialogPlus) {
                        uiThread.checkObject(null, null, dialogPlus, "dismiss", false);
                    }
                })
                .setOnDismissListener(this)
                .create();

        uiThread.checkObject(null, null, checkBoxDialog, "show", false);
    }

    // Selects a day
    private void selectDay() {
        uiThread.checkObject(animateCheckBox, null, null, null, true);
        cbm[daysPosition].setValue(1);
        modelItems[studentPosition].getDays()[daysPosition] = cbm[daysPosition].getItemName();
        y++;
        hoursDialog();
    }


    // Deselects a day
    private void deselectDay() {
        uiThread.checkObject(animateCheckBox, null, null, null, false);
        cbm[daysPosition].setValue(0);
        modelItems[studentPosition].getDays()[daysPosition] = null;
        modelItems[studentPosition].getAbsences()[daysPosition][hoursPosition] = null;
        y--;
    }

    // method to form the message that will be sent to the parents
    private void printDays(int pos) {
        String daysMessage = "";
        String totalAbsencesMessage = "";
        for (int j = 0; j < 5; j++) {

            if (modelItems[pos].getDays()[j] != null) {
                daysMessage = daysMessage + " " + modelItems[pos].getDays()[j] + " ";
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (modelItems[pos].getAbsences()[i][j] != null) {
                    totalAbsencesMessage = totalAbsencesMessage + " " + modelItems[pos].getAbsences()[i][j] + " ";
                }
            }
        }

        modelItems[pos].setSms(totalAbsencesMessage);
    }


    // This function sends message to all selected student's parents in the list
    private void sendMessage() {
        if (x == 0) {
            MyDynamicToast.warningMessage(context, "Zgjidhni minimalisht një nxënës!");
        } else {
            showAlertDialog();
        }
    }

    // creates a floating action button with its characteristics
    private void fabInitializer() {
        mFab = (FloatingActionButton) a.findViewById(R.id.fab);
        if (mFab != null) {
            mFab.setOnClickListener(this);
            uiThread.animateFab(mFab);
        }
    }

    // creates the alert dialog before the message will send
    private void showAlertDialog() {
        MaterialStyledDialog dialog = new MaterialStyledDialog(a);
        dialog.setTitle("Kujdes!");
        dialog.setDescription("Tarifa standarte do të aplikohen gjatë dërgimit të mesazheve!");
        dialog.setIcon(R.drawable.warning);
        dialog.withDialogAnimation(true, Duration.FAST);
        dialog.setHeaderColor(R.color.warning);
        dialog.setCancelable(false);
        dialog.setPositive("Vazhdo", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                sendMultipleMessages();

            }
        });
        dialog.setNegative("Anullo", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                deselectAllStudents();
            }
        });
        dialog.show();
    }

    // unchecks all students
    private void deselectAllStudents() {

        Cursor data = dbHelper.getListContent();
        int numRows = data.getCount();

        for (int i = 0; i < numRows; i++) {
            if (modelItems[i].getValue() != 0) {
                modelItems[i].setValue(0);
                uiThread.checkObject(null, ctv[i], null, null, false);
                x--;
            }
        }
    }

    // unchecks one student
    private void deselectStudent(int i) {
        modelItems[i].setValue(0);
        uiThread.checkObject(null, ctv[i], null, null, false);
        x--;
    }

    // selects one student
    private void selectStudent(int i) {
        modelItems[i].setValue(1);
        uiThread.checkObject(null, ctv[i], null, null, true);
        x++;
    }

    // return the checkBox dialog with its own characteristics
    private DialogPlus getDialog() {
        return checkBoxDialog;
    }

    public void exitAppCondition() {
        if (getDialog() == null || !getDialog().isShowing()) {
            a.finish();
        }
    }


    // Function that determines what happens when a certain view is clicked
    @Override
    public void onClick(View v) {
        if (v.equals(mFab)) {
            sendMessage();
        }
    }

    // defines listview characteristics
    private void initializeStudentListView() {
        ListView lv = (ListView) a.findViewById(R.id.listViewId); // <-- Initializes it by referencing to a view in layout
        if (lv != null) {
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // <-- Enables selecting multiple students
            lv.setOnItemClickListener(this); // <-- Enables listening for an item click
            lv.setOnItemLongClickListener(this);
            populateStudentListView(); // <-- Puts students informations in the list
            lv.setAdapter(adapter); // <-- Informations of the students are put to the listview through the adapter
        }
    }


    // Initializes sms object
    private void initializeSmsManager() {
        smsM = SmsManager.getDefault();
    }

    // Initializes imageView
    private void initializeImageView() {
        imageView = (ImageView) a.findViewById(R.id.completedImageViewId);
        if (imageView != null) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setScaleY(0);
            imageView.setScaleX(0);
            imageView.animate().alpha(1).start();
        }
    }

    // creates students at the array
    private void populateStudentListView() {
        Cursor data = dbHelper.getListContent();
        int numRows = data.getCount();
        if (numRows == 0) {
            noStudentTextView.setVisibility(View.VISIBLE);
        } else {
            noStudentTextView.setVisibility(View.INVISIBLE);
            int i = 0;
            modelItems = new Model[numRows];
            while (data.moveToNext()) {
                modelItems[i] = new Model(data.getString(1), data.getString(3), 0, data.getInt(2));
                i++;
            }
            adapter = new CustomAdapter(a, modelItems);
            sortAscending(adapter);
        }
    }

    // This method will sort names alphabetically
    private void sortAscending(CustomAdapter adapter) {
        adapter.sort(new Comparator<Model>() {
            @Override
            public int compare(Model f1, Model f2) {
                return f1.getName().compareTo(f2.getName());
            }
        });
    }

    private void populateCheckBoxListView() {
        cbm = new CheckBoxModel[5];
        cbm[0] = new CheckBoxModel("E Hënë", 0);
        cbm[1] = new CheckBoxModel("E Martë", 0);
        cbm[2] = new CheckBoxModel("E Mërkurë", 0);
        cbm[3] = new CheckBoxModel("E Enjte", 0);
        cbm[4] = new CheckBoxModel("E Premte", 0);
        checkBoxListAdapter = new CheckBoxCustomAdapter(a, cbm);
    }


    // performs some action when the checkBox dialog is dismissed
    @Override
    public void onDismiss(DialogPlus dialog) {
        if (y == 0) {
            deselectStudent(studentPosition);
            MyDynamicToast.warningMessage(context, "Përzgjidhni minimalisht një ditë mësimore!");
        }
    }

    private void hoursDialog() {
        PanterDialog hourDialog = new PanterDialog(a);

        hourDialog.setHeaderBackground(R.color.colorAccent);
        hourDialog.setHeaderLogo(R.drawable.ic_info);
        hourDialog.setDialogType(DialogType.SINGLECHOICE);
        hourDialog.isCancelable(true);
        hourDialog.items(loadHoursOption(), new OnSingleCallbackConfirmListener() {
            @Override
            public void onSingleCallbackConfirmed(PanterDialog dialog, int pos, String text) {
                hoursPosition = pos;
                modelItems[studentPosition].getAbsences()[daysPosition][hoursPosition] = modelItems[studentPosition].getDays()[daysPosition] + " " + text;
            }
        });
        hourDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (modelItems[studentPosition].getAbsences()[daysPosition][hoursPosition] == null) {
                    deselectDay();
                }
            }
        });
        hourDialog.show();
    }

    private ArrayList<String> loadHoursOption() {
        ArrayList<String> result = new ArrayList<>();
        String[] raw = a.getResources().getStringArray(R.array.hoursArray);
        Collections.addAll(result, raw);
        return result;
    }

}