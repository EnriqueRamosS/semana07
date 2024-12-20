package com.example.semana7.model;

import jakarta.persistence.*;

import java.util.Base64;
import org.springframework.web.multipart.MultipartFile;

@Entity
public class Producto {

    @Id
    private String idProducto;

    private String nombreProducto;

    private String unidadMedida;

    private int stock;

    private String idProveedor;

    @Lob
    private byte[] imagen;

    @Transient
    private MultipartFile archivoImagen;

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public MultipartFile getArchivoImagen() {
        return archivoImagen;
    }

    public void setArchivoImagen(MultipartFile archivoImagen) {
        this.archivoImagen = archivoImagen;
    }

    // Método para convertir la imagen a Base64
    public String getImagenBase64() {
        if (imagen != null) {
            return Base64.getEncoder().encodeToString(imagen);
        }
        return null;
    }

}
