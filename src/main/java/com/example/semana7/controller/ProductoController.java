package com.example.semana7.controller;

import com.example.semana7.model.Producto;
import com.example.semana7.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

// importaciones pdf
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

// importaciones excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService productoService) {
        this.service = productoService;
    }

    @GetMapping
    public String listarProducto(Model model) {
        model.addAttribute("productos", this.service.listarTodas());
        return "productos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("producto", new Producto());
        return "form";
    }

    @PostMapping
    public String guardarProducto(@ModelAttribute Producto producto) {
        try {
            if (producto.getIdProducto() != null
                    && service.buscarPorId(producto.getIdProducto()).isPresent()) {
                Producto productoexistente = service.buscarPorId(producto.getIdProducto()).get();
                // Actualizar solo los campos modificables
                productoexistente.setNombreProducto(producto.getNombreProducto());
                productoexistente.setUnidadMedida(producto.getUnidadMedida());
                productoexistente.setStock(producto.getStock());
                productoexistente.setIdProveedor(producto.getIdProveedor());

                // Actualizar imagen solo si se subió una nueva
                if (producto.getArchivoImagen() != null && !producto.getArchivoImagen().isEmpty()) {
                    productoexistente.setImagen(producto.getArchivoImagen().getBytes());
                }
                service.guardar(productoexistente);

            } else {
                // Es un nuevo registro
                String nuevoId = "CRT" + String.format("%03d", service.listarTodas().size() + 1);
                producto.setIdProducto(nuevoId);
                if (producto.getArchivoImagen() != null && !producto.getArchivoImagen().isEmpty()) {
                    producto.setImagen(producto.getArchivoImagen().getBytes());
                }
                service.guardar(producto);
            }
        } catch (IOException e) {

        }

        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable String id, Model model) {
        model.addAttribute("producto", this.service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID invalido" + id)));
        return "form";
    }
    // Eliminar un Producto por su ID

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable String id) {
        service.eliminar(id);
        return "redirect:/productos"; // Redirigir a la lista de Producto después de eliminar
    }

    @GetMapping("/reporte/pdf")
    public void generarPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=productos_reporte.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        document.add(new Paragraph("Reporte de Productos").setBold().setFontSize(18));

        Table table = new Table(5);
        table.addCell("ID");
        table.addCell("Nombre Producto");
        table.addCell("Unidad de Medida");
        table.addCell("Stock");
        table.addCell("Proveedor");

        List<Producto> productos = service.listarTodas();
        for (Producto producto : productos) {
            table.addCell(producto.getIdProducto());
            table.addCell(producto.getNombreProducto());
            table.addCell(producto.getUnidadMedida());
            table.addCell(String.valueOf(producto.getStock()));
            table.addCell(producto.getIdProveedor());
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/reporte/excel")
    public void generarExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=productos_reporte.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos");

        Row headerRow = sheet.createRow(0);
        String[] columnHeaders = {"ID", "Nombre", "Descripción", "Precio"};
        for (int i = 0; i < columnHeaders.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
            cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        List<Producto> productos = service.listarTodas();
        int rowIndex = 1;
        for (Producto producto : productos) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(producto.getIdProducto());
            row.createCell(1).setCellValue(producto.getNombreProducto());
            row.createCell(2).setCellValue(producto.getUnidadMedida());
            row.createCell(3).setCellValue(producto.getStock());
            row.createCell(3).setCellValue(producto.getIdProveedor());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
