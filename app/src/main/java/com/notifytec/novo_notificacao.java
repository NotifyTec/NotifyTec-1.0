package com.notifytec;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import funcoes.Funcoes;

public class novo_notificacao extends AppCompatActivity {

    //UI References
    private EditText expiraEm;
    private DatePickerDialog expiraEmDatePickerDialog;
    private TimePickerDialog expiraEmTimePickerDialog;
    private SimpleDateFormat dateFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_notificacao);

        //Carregando Spiner Curso
        ArrayAdapter arrayCurso = ArrayAdapter.createFromResource(this,
                R.array.curso_array, android.R.layout.simple_spinner_dropdown_item);
        MaterialBetterSpinner mdSpinnerCurso = (MaterialBetterSpinner) findViewById(R.id.spCurso);
        mdSpinnerCurso.setAdapter(arrayCurso);
        //fim spiner curso

        //Carregando Spiner Periodo
        ArrayAdapter arrayPeriodo = ArrayAdapter.createFromResource(this,
                R.array.periodo_array, android.R.layout.simple_spinner_dropdown_item);
        MaterialBetterSpinner mdSpinner_Periodo = (MaterialBetterSpinner) findViewById(R.id.spPeriodo);
        mdSpinner_Periodo.setAdapter(arrayPeriodo);
        //fim spiner Periodo


        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_novonotificacao);
        setSupportActionBar(toolbar);
            //Implantando a seta (<--) no canto esquerdo para "voltar". O icone um defino no xml "activity_novo_notificacao"
        getSupportActionBar().setHomeButtonEnabled(true);
        //fim Toolbar

        //FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            //Colorindo o fab
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_principal)));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //fim FAB

        //Expira Em... DatePickerDialog
        expiraEm = (EditText) findViewById(R.id.edExpira);
        expiraEm.setInputType(InputType.TYPE_NULL);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateField();
        setTimeField();

        expiraEm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //recebeu foco
                    expiraEmTimePickerDialog.show();
                    expiraEmDatePickerDialog.show();
                }
            }
        });
        expiraEm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expiraEmTimePickerDialog.show();
                expiraEmDatePickerDialog.show();
            }
        });
        //fim Expira Em... DatePickerDialog
    }

    private void setDateField() {
        Calendar newCalendar = Calendar.getInstance();
        expiraEmDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                expiraEm.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        //Setando data minima como a atual
        expiraEmDatePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
    }

    private void setTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        expiraEmTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hora = " às " + Funcoes.zeroEsquerda(hourOfDay, 2) + ":" + Funcoes.zeroEsquerda(minute, 2);
                expiraEm.append(hora);
            }

        }, newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
