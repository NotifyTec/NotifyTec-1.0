package com.notifytec;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.notifytec.Dao.UsuarioDao;
import com.notifytec.adapters.OpcoesAdapter;
import com.notifytec.contratos.CursoModel;
import com.notifytec.contratos.NotificacaoCompletaModel;
import com.notifytec.contratos.NotificacaoOpcaoModel;
import com.notifytec.contratos.PeriodoModel;
import com.notifytec.contratos.Resultado;
import com.notifytec.contratos.Token;
import com.notifytec.contratos.UsuarioModel;
import com.notifytec.service.NotificacaoService;
import com.notifytec.taks.CursoNovaNotificacaoTask;
import com.notifytec.taks.EnviarNotificacaoTask;
import com.notifytec.taks.PeriodoNovaNotifcacaoTask;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import funcoes.Funcoes;
import outros.VarConst;

public class NovaNotificacao extends BaseActivity {
    //UI References
    private EditText edTitulo;
    private EditText edDescricao;
    private Spinner spCurso;
    private Spinner spPeriodo;
    private EditText edExpiraEm;

    //TextView que fica no itemOpção da RecyclerView
    private TextView tvOpcao;

    private DatePickerDialog expiraEmDatePickerDialog;
    private TimePickerDialog expiraEmTimePickerDialog;
    private SimpleDateFormat dateFormatter;

    private RecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<String>();
    private OpcoesAdapter adapter;

    private String sDate = null, sTime = null, titulo, descri;
    private UUID curso, periodo;

    private UsuarioModel usuario;
    private NovaNotificacao mActivity;

    private List<CursoModel> cursos;
    private List<PeriodoModel> periodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_notificacao);

        mActivity = this;
        edTitulo = (EditText) findViewById(R.id.edTitulo);
        edDescricao = (EditText) findViewById(R.id.edDescri);
        spCurso = (Spinner) findViewById(R.id.spCurso);
        spPeriodo = (Spinner) findViewById(R.id.spPeriodo);
        tvOpcao = (TextView) findViewById(R.id.edOpcao);
        usuario = new UsuarioDao(getApplicationContext()).get();

        spCurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mostrarErro("SELECIONOU CUrso");
                Object item = parentView.getItemAtPosition(position);
                if(item !=null){
                    curso = cursos.get(position).getId();
                    new PeriodoNovaNotifcacaoTask(mActivity, cursos.get(position).getId()).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                mostrarErro("SELECIONOU CUrso");
            }
        });

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
                if (validarCampos()){
                    //00/00/0000 às 00:00
                    String date = edExpiraEm.getText().toString();
                    Date dateDate = null;
                    if(date != null && !date.isEmpty()){
                        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy 'às' HH:mm");
                        try {
                            dateDate = s.parse(date);
                        }catch(Exception ex){

                        }
                    }

                    new EnviarNotificacaoTask(mActivity,
                            periodo,
                            titulo,
                            descri,
                            dateDate,
                            mList
                            ).execute();
                }
            }
        });
        //fim FAB



        //Expira Em... DatePickerDialog
        edExpiraEm = (EditText) findViewById(R.id.edExpiraEm);
        edExpiraEm.setInputType(InputType.TYPE_NULL);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setTimeField();
        setDateField();


        edExpiraEm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //recebeu foco
                    expiraEmTimePickerDialog.show();
                    expiraEmDatePickerDialog.show();
                    if (!TextUtils.isEmpty(sDate) && !TextUtils.isEmpty(sTime)){
                        edExpiraEm.setText(sDate);
                        edExpiraEm.append(sTime);
                    }
                }
            }
        });
        edExpiraEm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expiraEmTimePickerDialog.show();
                expiraEmDatePickerDialog.show();
                if (!TextUtils.isEmpty(sDate) && !TextUtils.isEmpty(sTime)){
                    edExpiraEm.setText(sDate);
                    edExpiraEm.append(sTime);
                }
            }
        });
        //fim Expira Em... DatePickerDialog

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_opcoes);
//        mRecyclerView.setHasFixedSize(true);//dizendo que o tamanho o RecyclerView ñ vai alterar
        LinearLayoutManager ll = new LinearLayoutManager(getBaseContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(ll);


        //Add Opção Notificação
        ImageButton imgBtn = (ImageButton) findViewById(R.id.btnAdd);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Função que valida e add a Opção no Adapter
                addOpcaoAdpter();
            }
        });
        //Criando Adapter
        adapter = new OpcoesAdapter(getBaseContext(), mList);

        new CursoNovaNotificacaoTask(this).execute();
    }

    public void antesCarregarCurso(){
        setCarregando(true, "Carregando cursos...");
    }

    public void setCursos(Resultado<List<CursoModel>> resultado){
        setCarregando(false, null);

        if(resultado.isSucess()){
            cursos = resultado.getResult();

            List<String> itens = new ArrayList<>();
            for(CursoModel c : resultado.getResult()){
                itens.add(c.getApelido());
            }

            ArrayAdapter<String> a = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, itens);
            a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCurso.setAdapter(a);
        }else{
            mostrarErro(resultado.getMessage());
        }
    }

    public void antesCarregarPeriodo(){
        setCarregando(true, "Carregando períodos...");
    }

    public void setPeriodos(Resultado<List<PeriodoModel>> resultado){
        setCarregando(false, null);

        if(resultado.isSucess()){
            periodos = resultado.getResult();

            List<String> itens = new ArrayList<>();
            for(PeriodoModel c : resultado.getResult()){
                itens.add(String.valueOf(c.getNumero()));
            }

            ArrayAdapter<String> a = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, itens);
            a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPeriodo.setAdapter(a);

            spPeriodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    periodo = periodos.get(position).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });
        }else{
            mostrarErro(resultado.getMessage());
        }
    }

    public void resultadoEnviar(Resultado<NotificacaoCompletaModel> n){
        setCarregando(false, null);

        if(n.isSucess()){
            if(n.getResult().getOpcoes() != null && n.getResult().getOpcoes().size() > 0){
                abrirEnquetes();
            }else{
                abrirAvisos();
            }
            finish();
        }else{
            mostrarErro(n.getMessage());
        }
    }

    public void antesEnviar(){
        setCarregando(true, "Enviando notificação...");
    }

    private void setDateField() {
        sDate = null;
        Calendar newCalendar = Calendar.getInstance();
        expiraEmDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                sDate = dateFormatter.format(newDate.getTime());
//                expiraEm.setText(sDate);
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        //Setando data minima como a atual
        expiraEmDatePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
    }

    private void setTimeField() {
        sTime = null;
        Calendar newCalendar = Calendar.getInstance();
        expiraEmTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (!TextUtils.isEmpty(sDate)){
                    //Escolhi uma Data
                    sTime = " às " + Funcoes.zeroEsquerda(hourOfDay, 2) + ":" + Funcoes.zeroEsquerda(minute, 2);
                    edExpiraEm.setText(sDate + sTime);
                } else {
                    //Não escolhi uma Data
                    edExpiraEm.setText("");
                }
            }
        }, newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), true);
    }

    private void addOpcaoAdpter(){
        String opc = tvOpcao.getText().toString();

        if (!TextUtils.isEmpty(opc)){
            mList.add( tvOpcao.getText().toString() );
            tvOpcao.setText("");
            mRecyclerView.setAdapter( adapter );
        }
    }

    private boolean validarCampos(){
        //Campo obrigatorios Título, Conteúdo, Curso e Período são obrigatórios
        titulo = edTitulo.getText().toString();
        descri = edDescricao.getText().toString();

        Boolean valido = null;

        if (TextUtils.isEmpty(titulo)){
            edTitulo.setError(getString(R.string.error_field_required));
            edDescricao.requestFocus();
            edTitulo.requestFocus();
            valido = false;
        }
        else if (TextUtils.isEmpty(descri)){
            edDescricao.setError(getString(R.string.error_field_required));
            edTitulo.requestFocus();
            edDescricao.requestFocus();
            valido = false;
        }
        else if (curso == null){
            mostrarErro(getString(R.string.error_field_required));
            //spCurso.setError();
            spCurso.requestFocus();
            valido = false;
        }
        else if (periodo == null){
            mostrarErro(getString(R.string.error_field_required));
            //spPeriodo.setError(getString(R.string.error_field_required));
            spPeriodo.requestFocus();
            valido = false;
        }
        else valido = true;

        return valido;
    }

    public boolean isEnquete(){
        return mList.size() > 0;
    }

    public String montarExpiraEm(){
        if (sDate != null && sTime != null){
            String aux = sTime.substring(4);
            String f = sDate + " " + aux + ":00";
            f = f.replaceAll("-","/");
            return f;
        } else {
            return null;
        }
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