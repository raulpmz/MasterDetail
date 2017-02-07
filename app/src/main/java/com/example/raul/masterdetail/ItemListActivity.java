package com.example.raul.masterdetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.raul.masterdetail.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private String url_consulta;
    private JSONArray jSONArray;
    private JSONObject jsonObject;
    private DevuelveJSON devuelveJSON;
    public static ArrayList<Coche> arrayCoches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        arrayCoches = new ArrayList<Coche>();
        url_consulta = "http://iesayala.ddns.net/raulpmz/php.php";
        devuelveJSON = new DevuelveJSON();
        new ListaCoches().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).marca+ " - " + mValues.get(position).modelo);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    class ListaCoches extends AsyncTask<String, String, JSONArray> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            //--------------------------------------------------------------------------------//
            //      PARAMETROS DEL DIALOGO DE CARGA DE ACCESO A LA BASE DE DATOS WEB
            //--------------------------------------------------------------------------------//
            pDialog = new ProgressDialog(ItemListActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            //--------------------------------------------------------------------------------//
            //              CONSULTA PARA OBTENER EL OBJETO JSON CON LOS DATOS.
            //--------------------------------------------------------------------------------//
            try {
                HashMap<String, String> parametrosPost = new HashMap<>();
                //--------------------------------------------------------------------------------//
                //      SENTENCIA SQL CON LA QUE SE OBTIENE LA INFORMACION DE LOS OBJETOS JSON.
                //--------------------------------------------------------------------------------//
                parametrosPost.put("ins_sql", "SELECT * FROM COCHES, MODELOS WHERE COCHES.CODCOCHE = MODELOS.CODCOCHE");
                jSONArray = devuelveJSON.sendRequest(url_consulta, parametrosPost);

                if (jSONArray != null) {
                    System.out.println("Obtiene objeto jSon");
                    return jSONArray;

                }else{
                    System.out.println("No obtiene objeto jSon");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONArray json) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            //--------------------------------------------------------------------------------//
            //                  CONVERSIÓN DE OBJETO JSON A OBJETO JAVA.
            //--------------------------------------------------------------------------------//
            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    try {
                        jsonObject = json.getJSONObject(i);
                        Coche coche = new Coche();
                        coche.setMarca(jsonObject.getString("NomCoche")); // NOMBRE DEL OBJETO QUE QUIERO OBTENET: "NomCoche" (LA MARCA DEL COCHE).
                        // System.out.println(jsonObject.getString("NomCoche")); // PRUEBA DE QUE EL OBJETO HA SIDO OBTENIDO CORRECTAMENTE.
                        coche.setModelo(jsonObject.getString("NomModelo")); // NOMBRE DEL OBJETO QUE QUIERO OBTENET: "NomModelo" (EL MODELO DEL COCHE).
                        // System.out.println(jsonObject.getString("NomModelo"));
                        coche.setUrlFoto(jsonObject.getString("UrlFoto"));
                        arrayCoches.add(coche);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                DummyContent.rellenar();
            }else {
                Toast.makeText(ItemListActivity.this, "JSON Array nulo", Toast.LENGTH_LONG).show();
            }
        }
    }

}
