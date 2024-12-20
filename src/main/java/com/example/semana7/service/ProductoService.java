package com.example.semana7.service;

import com.example.semana7.model.Producto;
import com.example.semana7.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
     @Autowired
     private ProductoRepository repository;
         /**
     * Funcion para listar la tabla producto
     * @return 
     */
    public List<Producto> listarTodas() {
        return repository.findAll();
    }
        /**
     * Funcion para guardar datos de una producto
     * @param producto 
     */
    public void guardar(Producto producto) {
        repository.save(producto);
    }
     //Funcion para buscar una producto por id
    
    public Optional<Producto> buscarPorId(String id) {
       return repository.findById(id);
    }
        /**
     * Funcion para eliminar el registro de una producto
     * @param id 
     */
    public void eliminar(String id) {
        repository.deleteById(id);
    }
}
