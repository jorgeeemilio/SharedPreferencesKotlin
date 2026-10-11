package es.studium.sharedpreferenceskotlin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "telefonos";

    private EditText edtNombre;
    private EditText edtTelefono;
    private TextView tvListadoTelefonos;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar SharedPreferences con el nombre "telefonos"
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Referenciar elementos de la interfaz
        edtNombre = findViewById(R.id.edtNombre);
        edtTelefono = findViewById(R.id.edtTelefono);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnBorrar = findViewById(R.id.btnBorrar);
        tvListadoTelefonos = findViewById(R.id.tvListadoTelefonos);

        // Listeners de los botones
        btnGuardar.setOnClickListener(v -> guardarTelefono());
        btnBorrar.setOnClickListener(v -> confirmarBorrado());

        // Mostrar listado guardado al iniciar la actividad
        mostrarTelefonos();
    }

    private void guardarTelefono() {
        String nombre = edtNombre.getText() != null ? edtNombre.getText().toString().trim() : "";
        String telefono = edtTelefono.getText() != null ? edtTelefono.getText().toString().trim() : "";

        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, R.string.toast_campos_vacios, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(nombre, telefono);
        editor.apply();

        Toast.makeText(this, R.string.toast_guardado, Toast.LENGTH_SHORT).show();

        // Limpiar campos de texto
        edtNombre.setText("");
        edtTelefono.setText("");

        // Actualizar el listado en pantalla
        mostrarTelefonos();
    }

    private void confirmarBorrado() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialogo_titulo)
                .setMessage(R.string.dialogo_mensaje)
                .setPositiveButton(R.string.dialogo_si, (dialog, which) -> borrarListado())
                .setNegativeButton(R.string.dialogo_no, null)
                .show();
    }

    private void borrarListado() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        mostrarTelefonos();
        Toast.makeText(this, R.string.toast_borrado, Toast.LENGTH_SHORT).show();
    }

    private void mostrarTelefonos() {
        Map<String, ?> todosTelefonos = sharedPreferences.getAll();

        if (todosTelefonos == null || todosTelefonos.isEmpty()) {
            tvListadoTelefonos.setText(R.string.sin_telefonos);
            return;
        }

        List<String> nombres = new ArrayList<>(todosTelefonos.keySet());
        nombres.sort(String.CASE_INSENSITIVE_ORDER);

        StringBuilder sb = new StringBuilder();
        for (String nombre : nombres) {
            Object telefono = todosTelefonos.get(nombre);
            if (telefono != null) {
                sb.append("• ").append(nombre).append(": ").append(telefono).append("\n");
            }
        }

        tvListadoTelefonos.setText(sb.toString().trim());
    }
}