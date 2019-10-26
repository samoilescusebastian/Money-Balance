package ss.test;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView showPierderi;
    private EditText editPierderi;
    private int pierderi;
    private TextView showCastig;
    private EditText editCastig;
    private int castig;
    private  TextView showProfit;
    private int profit;
    private boolean fileLoaded;

    private void updateUI(TextView tV,String temp){
        tV.setText(temp);
    }
    private void initializeRecords(){
        pierderi=0;
        castig=0;
        profit=0;
        saveRecords();
        updateUI(showCastig,String.valueOf(castig));
        updateUI(showPierderi,String.valueOf(pierderi));
        updateUI(showProfit,String.valueOf(profit));
    }
    private void saveRecords(){
        String records = Integer.toString(castig)+'*'+Integer.toString(pierderi);
        FileOutputStream outputFile = null;
        try {
            outputFile = openFileOutput("SBet.txt", Context.MODE_PRIVATE);
            outputFile.write(records.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                outputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getApplicationContext(),records,Toast.LENGTH_LONG).show();


    }
    private int loadRecords(StringBuffer buffer){
        FileInputStream inputFile=null;
        try {
            inputFile = openFileInput("SBet.txt");
            int read = -1;
            while((read=inputFile.read())!=-1){
                buffer.append((char)read);
            }

        } catch (FileNotFoundException e) {
            return 0;
        } catch (IOException e) {
            return 0;
        }
        finally {
            try {
                inputFile.close();
            } catch (IOException e) {
                return 0;
            }

        }
       Toast.makeText(getApplicationContext(),"Data has been successfully loaded",Toast.LENGTH_LONG).show();
        return 1;
    }
    private void updateRecords(TextView tV, EditText eT, TextView showProfit, int sum){
        updateUI(tV,String.valueOf(Math.abs(sum)));
        eT.setText("0");
        profit=castig+pierderi;
        updateUI(showProfit,String.valueOf(profit));
        saveRecords();


    }
    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void confirmationBox(final String type,final View view){

        AlertDialog.Builder confirmation = new AlertDialog.Builder(MainActivity.this);
        confirmation.setTitle("Confirmation");
        confirmation.setMessage("Do you want to continue the process?");
        confirmation.setCancelable(false);
        confirmation.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"The process has been completed",Toast.LENGTH_LONG).show();
                hideKeyboardFrom(getApplicationContext(),view);
                if(type.compareTo("castig")==0){
                    castig += Integer.parseInt(editCastig.getText().toString());
                    updateRecords(showCastig, editCastig, showProfit, castig);
                }
                else
                    if(type.compareTo("pierderi")==0){
                        pierderi-=Integer.parseInt(editPierderi.getText().toString());
                        updateRecords(showPierderi,editPierderi,showProfit,pierderi);
                    }

            }
        });
       confirmation.setNegativeButton("No", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
           }
       });
        AlertDialog alert = confirmation.create();
        alert.show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(MainActivity.this, "Da-te dreacu!", Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addCastigButton;
        addCastigButton = (Button) findViewById(R.id.addCastig);
        showCastig = (TextView) findViewById(R.id.textShowCastig);
        editCastig = (EditText) findViewById(R.id.textEditCastig);
        castig = Integer.parseInt(showCastig.getText().toString());

        Button addPierderiButton;
        addPierderiButton = (Button) findViewById(R.id.addPierderi);
        showPierderi = (TextView) findViewById(R.id.textShowPierderi);
        editPierderi = (EditText) findViewById(R.id.textEditPierderi);
        pierderi = Integer.parseInt(showPierderi.getText().toString());

        showProfit = (TextView) findViewById(R.id.textShowProfit);
        profit = Integer.parseInt(showProfit.getText().toString());

        Button resetButton;
        resetButton = (Button) findViewById(R.id.resetButton);
        File myFile = new File("SBet.txt");

        if(!myFile.exists()) {
            try {
                myFile.createNewFile();
                initializeRecords();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Da-te dreacu!", Toast.LENGTH_LONG).show();
            }
        }
        StringBuffer buffer = new StringBuffer();
        if(buffer.equals(null)||loadRecords(buffer)==0){
            Toast.makeText(MainActivity.this, "Da-te dreacu!", Toast.LENGTH_LONG).show();

        }
        else{

       String castigSaved = buffer.substring(0, buffer.indexOf("*"));
        String pierderiSaved = buffer.substring(buffer.indexOf("*") + 1);
        castig = Integer.valueOf(castigSaved);
        pierderi = Integer.valueOf(pierderiSaved);
        profit = castig + pierderi;
        updateUI(showCastig, String.valueOf(castig));
        updateUI(showPierderi, String.valueOf(pierderi));
        updateUI(showProfit, String.valueOf(profit));
        }

       // initializeRecords();
        addCastigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationBox("castig",v);
               /*
                showCastig.setText(String.valueOf(castig));
                editCastig.setText("0");
                profit+=castig;
                showProfit.setText(String.valueOf(profit));
                */
            }
        });

        addPierderiButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                confirmationBox("pierderi",v);
               /*
                showPierderi.setText(String.valueOf(pierderi));
                editPierderi.setText("0");
                profit-=pierderi;
                showProfit.setText(String.valueOf(profit)); */
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.authentication_layout,null);
                mBuilder.setView(mView);
                final AlertDialog passwordDialog = mBuilder.create();
                passwordDialog.show();
                final EditText password=(EditText)mView.findViewById(R.id.editTextPassword);
                final Button confirmButton=(Button)mView.findViewById(R.id.confirmPasswordButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String passwordText =password.getText().toString();
                        if(!passwordText.isEmpty()) {

                            if (passwordText.compareTo("159357")==0) {
                                Toast.makeText(MainActivity.this, "The records have been reseted!", Toast.LENGTH_LONG).show();
                                initializeRecords();
                                passwordDialog.dismiss();
                            }
                            else {
                                Toast tempToast = Toast.makeText(MainActivity.this, "Incorrect password!\n  Please try again", Toast.LENGTH_LONG);
                                TextView tempView = (TextView) tempToast.getView().findViewById(android.R.id.message);
                                tempView.setTextColor(Color.RED);
                                tempToast.show();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Please insert a valid password!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
}
