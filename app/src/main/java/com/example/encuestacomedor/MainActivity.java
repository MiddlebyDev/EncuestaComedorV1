package com.example.encuestacomedor;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unstoppable.submitbuttonview.SubmitButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;


public class MainActivity extends AppCompatActivity {
    SubmitButton send2;
    ImageButton btnServicio1, btnServicioN1, btnServicio2, btnServicioN2, btnServicio3, btnServicioN3,
    btnServicio4, btnServicioN4, btnServicio5, btnServicioN5, btnServicio6, btnServicioN6;

    CheckBox turnouno, turnodos, turnotres, turnocuatro;
    TextView tvPregunta1, servicios2, servicios3, servicios4, tvPregunta2, turnos, tvPregunta3, tvBloqueActivo, pregunta1, pregunta2, pregunta3, pregunta4, pregunta5, pregunta6;
    TextView tvPregunta4, tvPregunta5, tvPregunta6, btnEnviar;
    LinearLayout turnocomida;

    public static final long PERIODO = 1500; //  x / 1000 = Segundos a esperar
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conexionDB();

        turnocomida = (LinearLayout) findViewById(R.id.trunocomida);
        send2 = findViewById(R.id.send1);
        send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertar();
            }
        });
        pregunta1=(TextView)findViewById(R.id.Pregunta1);
        pregunta2=(TextView)findViewById(R.id.Pregunta2);
        pregunta3=(TextView)findViewById(R.id.Pregunta3);
        pregunta4=(TextView)findViewById(R.id.Pregunta4);
        pregunta5=(TextView)findViewById(R.id.Pregunta5);
        pregunta6=(TextView)findViewById(R.id.Pregunta6);

        tvBloqueActivo =(TextView)findViewById(R.id.textView5);
        tvBloqueActivo.setVisibility(View.GONE);

        //Asignación de los ImageButton
        btnServicio1 = (ImageButton) findViewById(R.id.btnServicio1);
        btnServicioN1 = (ImageButton) findViewById(R.id.btnServicioN1);

        btnServicio2 = (ImageButton) findViewById(R.id.btnServicio2);
        btnServicioN2 = (ImageButton) findViewById(R.id.btnServicioN2);

        btnServicio3 = (ImageButton) findViewById(R.id.btnServicio3);
        btnServicioN3 = (ImageButton) findViewById(R.id.btnServicioN3);

        btnServicio4 = (ImageButton) findViewById(R.id.btnServicio4);
        btnServicioN4 = (ImageButton) findViewById(R.id.btnServicioN4);

        btnServicio5 = (ImageButton) findViewById(R.id.btnServicio5);
        btnServicioN5 = (ImageButton) findViewById(R.id.btnServicioN5);

        btnServicio6 = (ImageButton) findViewById(R.id.btnServicio6);
        btnServicioN6 = (ImageButton) findViewById(R.id.btnServicioN6);

        //Asignación TextView
        tvPregunta1 = (TextView) findViewById(R.id.tvPregunta1);
        tvPregunta2 = (TextView) findViewById(R.id.tvPregunta2);
        tvPregunta3 = (TextView) findViewById(R.id.tvPregunta3);
        tvPregunta4 = (TextView) findViewById(R.id.tvPregunta4);
        tvPregunta5 = (TextView) findViewById(R.id.tvPregunta5);
        tvPregunta6 = (TextView) findViewById(R.id.tvPregunta6);


        turnos = (TextView) findViewById(R.id.turno);
        servicios2 = (TextView) findViewById(R.id.servicio2);
        servicios3 = (TextView) findViewById(R.id.servicio3);
        servicios4 = (TextView) findViewById(R.id.servicio4);
        //btnEnviar = (TextView) findViewById(R.id.send1);

        turnos.setVisibility(View.GONE);
        tvPregunta1.setVisibility(View.GONE);
        servicios2.setVisibility(View.GONE);
        servicios3.setVisibility(View.GONE);
        servicios4.setVisibility(View.GONE);
        tvPregunta2.setVisibility(View.GONE);
        tvPregunta3.setVisibility(View.GONE);
        tvPregunta4.setVisibility(View.GONE);
        tvPregunta5.setVisibility(View.GONE);
        tvPregunta6.setVisibility(View.GONE);

        turnouno = (CheckBox) findViewById(R.id.turno1);
        turnodos = (CheckBox) findViewById(R.id.turno2);
        turnotres = (CheckBox) findViewById(R.id.turno3);
        //turnocuatro = (CheckBox) findViewById(R.id.turno4);


        Consultatabla();
    }

public void timerSubmit(){
    handler = new Handler();

    handler.postDelayed( new Runnable(){
        @Override
        public void run(){
            limpiar();
        }
    }, PERIODO );
}

//region Timer que se reinicia al presionar un boton

private Handler handlerManos = new Handler();

    Runnable runnableMano = new Runnable() {
        @Override
        public void run() {
            limpiar();
        }
    };

    public void timerManos(){

        handlerManos.removeCallbacks(runnableMano);

        handlerManos.postDelayed(runnableMano, 10000);
    }

// endregion


public void Consultatabla(){

    try {
        Statement stm = conexionDB().createStatement();
        ResultSet rs = stm.executeQuery("EXEC SP_CONSULTAR_BLOQUE_ACTIVO");
        if (rs.next()){
            tvBloqueActivo.setText(rs.getString(1));
            //System.out.println(""+tvBloqueActivo.getText().toString());

            String Cadena = "EXEC SP_CONSULTAR_PREGUNTAS " + tvBloqueActivo.getText().toString();

            ResultSet rs5 = stm.executeQuery(Cadena);
            if (rs5.next()) {
                pregunta1.setText(rs5.getString(2));
                pregunta2.setText(rs5.getString(3));
                pregunta3.setText(rs5.getString(4));
                pregunta4.setText(rs5.getString(5));
                pregunta5.setText(rs5.getString(6));
                pregunta6.setText(rs5.getString(7));
            }
        }
    }catch (SQLException e) {
        //System.out.println("insercion-> "+e.getMessage());
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

}

    public Connection conexionDB() {
        Connection conexion2 = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion2 = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.4.41;databaseName=Comedor;user=PruebasJosue;password=123456789123456*;");
            //System.out.println("conexcion"+conexion2.toString());

        } catch (Exception e) {
            //System.out.println("coneccion-> "+e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return conexion2;
    }

    public void insertar() {

        if (turnos.getText().equals("")){
            Toast.makeText(MainActivity.this, "Debe seleccionar un turno", Toast.LENGTH_LONG).show();
            send2.doResult(false);
            turnouno.setError("Required");
            turnodos.setError("Required");
            turnotres.setError("Required");
            //turnocuatro.setError("Required");
        }
        else if (tvPregunta1.getText().equals("")) {
            Toast.makeText(MainActivity.this, "Falta pregunta 1 ", Toast.LENGTH_LONG).show();
            send2.doResult(false);
        } else if (tvPregunta2.getText().equals("")) {
            Toast.makeText(MainActivity.this, "Falta pregunta 2 ", Toast.LENGTH_LONG).show();
            send2.doResult(false);
        } else if (tvPregunta3.getText().equals("")) {
            Toast.makeText(MainActivity.this, "Falta pregunta 3 ", Toast.LENGTH_LONG).show();
            send2.doResult(false);
        } else if (tvPregunta4.getText().equals("")) {
            Toast.makeText(MainActivity.this, "Falta pregunta 4 ", Toast.LENGTH_LONG).show();
            send2.doResult(false);
        } else if (tvPregunta5.getText().equals("")) {
            Toast.makeText(MainActivity.this, "Falta pregunta 5 ", Toast.LENGTH_LONG).show();
            send2.doResult(false);
        } else if (tvPregunta6.getText().equals("")) {
            Toast.makeText(MainActivity.this, "Falta pregunta 6 ", Toast.LENGTH_LONG).show();
            send2.doResult(false);
        }
        else {
            try {
                PreparedStatement pst = conexionDB().prepareStatement("EXEC SP_AGREGAR_RESPUESTA ?,?,?,?,?,?,?,?");
                pst.setInt(1, Integer.parseInt(tvBloqueActivo.getText().toString()));
                pst.setString(2, turnos.getText().toString());
                pst.setString(3, tvPregunta1.getText().toString());
                pst.setString(4, tvPregunta2.getText().toString());
                pst.setString(5, tvPregunta3.getText().toString());
                pst.setString(6, tvPregunta4.getText().toString());
                pst.setString(7, tvPregunta5.getText().toString());
                pst.setString(8, tvPregunta6.getText().toString());

                pst.executeUpdate();

                send2.doResult(true);
                timerSubmit();
            } catch (SQLException e) {
                send2.doResult(false);
                //System.out.println("insercion-> "+e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                timerSubmit();
            }
        }
    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        if (checked) {
            switch (view.getId()) {
                case R.id.turno1:

                    turnos.setText("Turno 1");
                    turnodos.setChecked(false);
                    turnotres.setChecked(false);
                    //turnocuatro.setChecked(false);

                    break;
                case R.id.turno2:
                    turnos.setText("Turno 2");
                    turnouno.setChecked(false);
                    turnotres.setChecked(false);
                    //turnocuatro.setChecked(false);

                    break;
                case R.id.turno3:
                    turnos.setText("Turno 3");
                    turnouno.setChecked(false);
                    turnodos.setChecked(false);
                    //turnocuatro.setChecked(false);

                    break;
                /*case R.id.turno4:
                    turnos.setText("Turno 4");
                    turnouno.setChecked(false);
                    turnodos.setChecked(false);
                    turnotres.setChecked(false);

                    break;*/
            }
            send2.reset();
        } else {
            turnos.setText("");
        }
        timerManos();
    }

    public void cambiarMano(View view) {
        switch (view.getId()) {
            case  R.id.btnServicio1:
            case R.id.btnServicioN1:
                if (view.getId() == btnServicio1.getId()) {
                    btnServicio1.setSelected(true);
                    btnServicioN1.setSelected(false);
                    btnServicio1.setBackgroundResource(R.color.gris);
                    btnServicioN1.setBackgroundResource(R.color.white);

                    tvPregunta1.setText("BUENO");
                }
                else {
                    btnServicioN1.setSelected(true);
                    btnServicio1.setSelected(false);
                    btnServicio1.setBackgroundResource(R.color.white);
                    btnServicioN1.setBackgroundResource(R.color.gris);

                    tvPregunta1.setText("MALO");
                }
                break;
            case R.id.btnServicio2:
            case R.id.btnServicioN2:
                if (view.getId() == btnServicio2.getId()) {
                    btnServicio2.setSelected(true);
                    btnServicioN2.setSelected(false);
                    btnServicio2.setBackgroundResource(R.color.gris);
                    btnServicioN2.setBackgroundResource(R.color.white);

                    tvPregunta2.setText("BUENO");
                }
                else {
                    btnServicioN2.setSelected(true);
                    btnServicio2.setSelected(false);
                    btnServicio2.setBackgroundResource(R.color.white);
                    btnServicioN2.setBackgroundResource(R.color.gris);

                    tvPregunta2.setText("MALO");
                }
                break;
            case R.id.btnServicio3:
            case  R.id.btnServicioN3:
                if (view.getId() == btnServicio3.getId()) {
                    btnServicio3.setSelected(true);
                    btnServicioN3.setSelected(false);
                    btnServicio3.setBackgroundResource(R.color.gris);
                    btnServicioN3.setBackgroundResource(R.color.white);

                    tvPregunta3.setText("BUENO");
                }
                else {
                    btnServicioN3.setSelected(true);
                    btnServicio3.setSelected(false);
                    btnServicio3.setBackgroundResource(R.color.white);
                    btnServicioN3.setBackgroundResource(R.color.gris);

                    tvPregunta3.setText("MALO");
                }
                break;
            case R.id.btnServicio4:
            case  R.id.btnServicioN4:
                if (view.getId() == btnServicio4.getId()) {
                    btnServicio4.setSelected(true);
                    btnServicioN4.setSelected(false);
                    btnServicio4.setBackgroundResource(R.color.gris);
                    btnServicioN4.setBackgroundResource(R.color.white);

                    tvPregunta4.setText("BUENO");
                }
                else {
                    btnServicioN4.setSelected(true);
                    btnServicio4.setSelected(false);
                    btnServicio4.setBackgroundResource(R.color.white);
                    btnServicioN4.setBackgroundResource(R.color.gris);

                    tvPregunta4.setText("MALO");
                }
                break;
            case R.id.btnServicio5:
            case  R.id.btnServicioN5:
                if (view.getId() == btnServicio5.getId()) {
                    btnServicio5.setSelected(true);
                    btnServicioN5.setSelected(false);
                    btnServicio5.setBackgroundResource(R.color.gris);
                    btnServicioN5.setBackgroundResource(R.color.white);

                    tvPregunta5.setText("BUENO");
                }
                else {
                    btnServicioN5.setSelected(true);
                    btnServicio5.setSelected(false);
                    btnServicio5.setBackgroundResource(R.color.white);
                    btnServicioN5.setBackgroundResource(R.color.gris);

                    tvPregunta5.setText("MALO");
                }
                break;
            case R.id.btnServicio6:
            case  R.id.btnServicioN6:
                if (view.getId() == btnServicio6.getId()) {
                    btnServicio6.setSelected(true);
                    btnServicioN6.setSelected(false);
                    btnServicio6.setBackgroundResource(R.color.gris);
                    btnServicioN6.setBackgroundResource(R.color.white);

                    tvPregunta6.setText("BUENO");
                }
                else {
                    btnServicioN6.setSelected(true);
                    btnServicio6.setSelected(false);
                    btnServicio6.setBackgroundResource(R.color.white);
                    btnServicioN6.setBackgroundResource(R.color.gris);

                    tvPregunta6.setText("MALO");
                }
                break;
            default:
                break;
        }
        send2.reset();
        timerManos();
    }

    public void limpiar() {
        turnos.setText("");
        tvPregunta1.setText("");
        tvPregunta2.setText("");
        tvPregunta3.setText("");
        tvPregunta4.setText("");
        tvPregunta5.setText("");
        tvPregunta6.setText("");

        turnouno.setChecked(false);
        turnodos.setChecked(false);
        turnotres.setChecked(false);
        //turnocuatro.setChecked(false);

        turnouno.setError(null);
        turnodos.setError(null);
        turnotres.setError(null);
        //turnocuatro.setError(null);

        btnServicio1.setBackgroundResource(R.color.white);
        btnServicioN1.setBackgroundResource(R.color.white);

        btnServicio2.setBackgroundResource(R.color.white);
        btnServicioN2.setBackgroundResource(R.color.white);

        btnServicio3.setBackgroundResource(R.color.white);
        btnServicioN3.setBackgroundResource(R.color.white);

        btnServicio4.setBackgroundResource(R.color.white);
        btnServicioN4.setBackgroundResource(R.color.white);

        btnServicio5.setBackgroundResource(R.color.white);
        btnServicioN5.setBackgroundResource(R.color.white);

        btnServicio6.setBackgroundResource(R.color.white);
        btnServicioN6.setBackgroundResource(R.color.white);

        send2.reset();
    }
}