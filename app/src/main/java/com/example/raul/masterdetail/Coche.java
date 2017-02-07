package com.example.raul.masterdetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raul on 28/1/17.
 */

public class Coche {

    String marca, modelo, urlFoto;

    public Coche() {
        this.modelo = "";
        this.marca = "";
        this.urlFoto = "";
    }

    public Coche(String marca, String modelo, String urlFoto) {
        this.modelo = modelo;
        this.marca = marca;
        this.urlFoto = urlFoto;
    }

    public String getMarca() {return marca;}

    public void setMarca(String marca) {this.marca = marca;}

    public String getModelo() {return modelo;}

    public void setModelo(String modelo) {this.modelo = modelo;}

    public String getUrlFoto(){return urlFoto;}

    public void setUrlFoto(String urlFoto){this.urlFoto = urlFoto;}

    public List listaModelo(ArrayList<Coche> listado){
        List<String> listaModelos = new ArrayList<>();

        for(int i = 0 ; i < listado.size(); i++){
            if (listado.get(i).getMarca().equals("AUDI") ){
                listaModelos.add(listado.get(i).getModelo());
            }
            if (listado.get(i).getMarca().equals("BMW") ){
                listaModelos.add(listado.get(i).getModelo());
            }
            if (listado.get(i).getMarca().equals("MERCEDES") ){
                listaModelos.add(listado.get(i).getModelo());
            }
            if (listado.get(i).getMarca().equals("SEAT") ){
                listaModelos.add(listado.get(i).getModelo());
            }
            if (listado.get(i).getMarca().equals("VOLKSWAGEN") ){
                listaModelos.add(listado.get(i).getModelo());
            }
        }
        return listaModelos;
    }
}
