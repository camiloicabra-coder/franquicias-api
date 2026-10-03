package com.nequi.franquicias.controller;

import com.nequi.franquicias.repository.FranquiciaRepository;
import com.nequi.franquicias.repository.ProductoRepository;
import com.nequi.franquicias.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FranquiciaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private FranquiciaRepository franquiciaRepository;

    @BeforeEach
    void limpiarBaseDatos() {
        productoRepository.deleteAll();
        sucursalRepository.deleteAll();
        franquiciaRepository.deleteAll();
    }

    @Test
    void debeCrearFranquicia() throws Exception {

        String body = """
                {
                    "nombre": "McDonald's"
                }
                """;

        mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre")
                        .value("McDonald's"));
    }
    @Test
    void noDebeCrearFranquiciaConNombreVacio() throws Exception {

        String body = """
            {
                "nombre": ""
            }
            """;

        mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isBadRequest());
               // .andExpect(jsonPath("$.message")
                      //  .value("Error de validación"));
    }
    @Test
    void debeActualizarNombreDeFranquicia() throws Exception {

        String crearBody = """
            {
                "nombre": "Franquicia Original"
            }
            """;

        MvcResult resultadoCreacion = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(crearBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String response = resultadoCreacion
                .getResponse()
                .getContentAsString();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(response)
                .get("id")
                .asLong();

        String actualizarBody = """
            {
                "nombre": "Franquicia Actualizada"
            }
            """;

        mockMvc.perform(
                        put("/api/franquicias/" + franquiciaId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(actualizarBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(franquiciaId))
                .andExpect(jsonPath("$.nombre")
                        .value("Franquicia Actualizada"));
    }
    @Test
    void noDebeActualizarFranquiciaInexistente() throws Exception {

        String body = """
            {
                "nombre": "Franquicia Inexistente"
            }
            """;

        mockMvc.perform(
                        put("/api/franquicias/999999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isNotFound());
    }
    @Test
    void debeCrearSucursalEnFranquicia() throws Exception {

        String franquiciaBody = """
            {
                "nombre": "Franquicia Prueba"
            }
            """;

        MvcResult resultadoFranquicia = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(franquiciaBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String franquiciaResponse = resultadoFranquicia
                .getResponse()
                .getContentAsString();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(franquiciaResponse)
                .get("id")
                .asLong();

        String sucursalBody = """
            {
                "nombre": "Sucursal Bogotá"
            }
            """;

        mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sucursalBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre")
                        .value("Sucursal Bogotá"))
                .andExpect(jsonPath("$.franquiciaId")
                        .value(franquiciaId));
    }
    @Test
    void noDebeActualizarSucursalDeOtraFranquicia() throws Exception {

        // Crear franquicia 1
        String franquicia1Body = """
            {
                "nombre": "Franquicia 1"
            }
            """;

        MvcResult resultadoFranquicia1 = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(franquicia1Body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquicia1Id = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia1.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear franquicia 2
        String franquicia2Body = """
            {
                "nombre": "Franquicia 2"
            }
            """;

        MvcResult resultadoFranquicia2 = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(franquicia2Body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquicia2Id = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia2.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear sucursal en franquicia 1
        String sucursalBody = """
            {
                "nombre": "Sucursal Original"
            }
            """;

        MvcResult resultadoSucursal = mockMvc.perform(
                        post("/api/franquicias/" + franquicia1Id + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sucursalBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursalId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Intentar actualizar la sucursal usando otra franquicia
        String actualizarBody = """
            {
                "nombre": "Sucursal Modificada"
            }
            """;

        mockMvc.perform(
                        put("/api/franquicias/" + franquicia2Id
                                + "/sucursales/" + sucursalId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(actualizarBody)
                )
                .andExpect(status().isNotFound());
    }
    @Test
    void debeCrearProductoEnSucursal() throws Exception {

        // Crear franquicia
        String franquiciaBody = """
            {
                "nombre": "Franquicia Productos"
            }
            """;

        MvcResult resultadoFranquicia = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(franquiciaBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear sucursal
        String sucursalBody = """
            {
                "nombre": "Sucursal Centro"
            }
            """;

        MvcResult resultadoSucursal = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sucursalBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursalId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear producto
        String productoBody = """
            {
                "nombre": "Producto A",
                "stock": 100
            }
            """;

        mockMvc.perform(
                        post("/api/sucursales/" + sucursalId + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(productoBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre")
                        .value("Producto A"))
                .andExpect(jsonPath("$.stock")
                        .value(100))
                .andExpect(jsonPath("$.sucursalId")
                        .value(sucursalId));
    }
    @Test
    void noDebeCrearProductoConStockNegativo() throws Exception {

        // Crear franquicia
        String franquiciaBody = """
            {
                "nombre": "Franquicia Validacion"
            }
            """;

        MvcResult resultadoFranquicia = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(franquiciaBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear sucursal
        String sucursalBody = """
            {
                "nombre": "Sucursal Validacion"
            }
            """;

        MvcResult resultadoSucursal = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sucursalBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursalId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Intentar crear producto con stock negativo
        String productoBody = """
            {
                "nombre": "Producto Invalido",
                "stock": -10
            }
            """;

        mockMvc.perform(
                        post("/api/sucursales/" + sucursalId + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(productoBody)
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    void debeActualizarStockDeProducto() throws Exception {

        // Crear franquicia
        String franquiciaBody = """
            {
                "nombre": "Franquicia Stock"
            }
            """;

        MvcResult resultadoFranquicia = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(franquiciaBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear sucursal
        String sucursalBody = """
            {
                "nombre": "Sucursal Stock"
            }
            """;

        MvcResult resultadoSucursal = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sucursalBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursalId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear producto
        String productoBody = """
            {
                "nombre": "Producto Stock",
                "stock": 100
            }
            """;

        MvcResult resultadoProducto = mockMvc.perform(
                        post("/api/sucursales/" + sucursalId + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(productoBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long productoId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoProducto.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Actualizar stock
        String stockBody = """
            {
                "stock": 250
            }
            """;

        mockMvc.perform(
                        patch("/api/sucursales/"
                                + sucursalId
                                + "/productos/"
                                + productoId
                                + "/stock")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stockBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(productoId))
                .andExpect(jsonPath("$.nombre")
                        .value("Producto Stock"))
                .andExpect(jsonPath("$.stock")
                        .value(250))
                .andExpect(jsonPath("$.sucursalId")
                        .value(sucursalId));
    }
    @Test
    void debeActualizarNombreDeProducto() throws Exception {

        // Crear franquicia
        String franquiciaBody = """
            {
                "nombre": "Franquicia Producto"
            }
            """;

        MvcResult resultadoFranquicia = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(franquiciaBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear sucursal
        String sucursalBody = """
            {
                "nombre": "Sucursal Producto"
            }
            """;

        MvcResult resultadoSucursal = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sucursalBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursalId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear producto
        String productoBody = """
            {
                "nombre": "Producto Original",
                "stock": 100
            }
            """;

        MvcResult resultadoProducto = mockMvc.perform(
                        post("/api/sucursales/" + sucursalId + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(productoBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long productoId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoProducto.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Actualizar nombre
        String nombreBody = """
            {
                "nombre": "Producto Actualizado"
            }
            """;

        mockMvc.perform(
                        put("/api/sucursales/"
                                + sucursalId
                                + "/productos/"
                                + productoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(nombreBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(productoId))
                .andExpect(jsonPath("$.nombre")
                        .value("Producto Actualizado"))
                .andExpect(jsonPath("$.stock")
                        .value(100))
                .andExpect(jsonPath("$.sucursalId")
                        .value(sucursalId));
    }
    @Test
    void debeEliminarProductoDeSucursal() throws Exception {

        // Crear franquicia
        String franquiciaBody = """
            {
                "nombre": "Franquicia Eliminar"
            }
            """;

        MvcResult resultadoFranquicia = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(franquiciaBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear sucursal
        String sucursalBody = """
            {
                "nombre": "Sucursal Eliminar"
            }
            """;

        MvcResult resultadoSucursal = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sucursalBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursalId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear producto
        String productoBody = """
            {
                "nombre": "Producto A Eliminar",
                "stock": 50
            }
            """;

        MvcResult resultadoProducto = mockMvc.perform(
                        post("/api/sucursales/" + sucursalId + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(productoBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long productoId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoProducto.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Eliminar producto
        mockMvc.perform(
                        delete("/api/sucursales/"
                                + sucursalId
                                + "/productos/"
                                + productoId)
                )
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(
                        patch("/api/sucursales/"
                                + sucursalId
                                + "/productos/"
                                + productoId
                                + "/stock")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "stock": 100
                                    }
                                    """)
                )
                .andExpect(status().isNotFound());
    }
    @Test
    void noDebeActualizarProductoDeOtraSucursal() throws Exception {

        // Crear franquicia
        String franquiciaBody = """
            {
                "nombre": "Franquicia Integridad"
            }
            """;

        MvcResult resultadoFranquicia = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(franquiciaBody)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear primera sucursal
        MvcResult resultadoSucursal1 = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Sucursal 1"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursal1Id = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal1.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear segunda sucursal
        MvcResult resultadoSucursal2 = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Sucursal 2"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursal2Id = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal2.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear producto en la sucursal 1
        MvcResult resultadoProducto = mockMvc.perform(
                        post("/api/sucursales/" + sucursal1Id + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Producto Protegido",
                                        "stock": 100
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long productoId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoProducto.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Intentar modificar el producto desde otra sucursal
        mockMvc.perform(
                        patch("/api/sucursales/"
                                + sucursal2Id
                                + "/productos/"
                                + productoId
                                + "/stock")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "stock": 999
                                    }
                                    """)
                )
                .andExpect(status().isNotFound());
    }
    @Test
    void debeObtenerProductoConMayorStockPorSucursal() throws Exception {

        // Crear franquicia
        MvcResult resultadoFranquicia = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Franquicia Mayor Stock"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear sucursal 1
        MvcResult resultadoSucursal1 = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Sucursal Norte"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursal1Id = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal1.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear productos en sucursal 1
        mockMvc.perform(
                        post("/api/sucursales/" + sucursal1Id + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Producto Bajo",
                                        "stock": 50
                                    }
                                    """)
                )
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/sucursales/" + sucursal1Id + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Producto Mayor",
                                        "stock": 200
                                    }
                                    """)
                )
                .andExpect(status().isCreated());

        // Crear sucursal 2
        MvcResult resultadoSucursal2 = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Sucursal Sur"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursal2Id = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal2.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear productos en sucursal 2
        mockMvc.perform(
                        post("/api/sucursales/" + sucursal2Id + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Producto Sur Bajo",
                                        "stock": 80
                                    }
                                    """)
                )
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/sucursales/" + sucursal2Id + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Producto Sur Mayor",
                                        "stock": 300
                                    }
                                    """)
                )
                .andExpect(status().isCreated());

        // Consultar productos con mayor stock
        mockMvc.perform(
                        get("/api/franquicias/"
                                + franquiciaId
                                + "/productos/mayor-stock")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].producto")
                        .value("Producto Mayor"))
                .andExpect(jsonPath("$[0].stock")
                        .value(200))
                .andExpect(jsonPath("$[0].sucursal")
                        .value("Sucursal Norte"))
                .andExpect(jsonPath("$[1].producto")
                        .value("Producto Sur Mayor"))
                .andExpect(jsonPath("$[1].stock")
                        .value(300))
                .andExpect(jsonPath("$[1].sucursal")
                        .value("Sucursal Sur"));
    }
    @Test
    void noDebeActualizarStockDeProductoInexistente() throws Exception {

        // Crear franquicia
        MvcResult resultadoFranquicia = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Franquicia Error"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear sucursal
        MvcResult resultadoSucursal = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Sucursal Error"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursalId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Intentar actualizar un producto inexistente
        mockMvc.perform(
                        patch("/api/sucursales/"
                                + sucursalId
                                + "/productos/999999/stock")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "stock": 500
                                    }
                                    """)
                )
                .andExpect(status().isNotFound());
    }
    @Test
    void noDebeEliminarProductoDeOtraSucursal() throws Exception {

        // Crear franquicia
        MvcResult resultadoFranquicia = mockMvc.perform(
                        post("/api/franquicias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Franquicia Delete"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long franquiciaId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoFranquicia.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear sucursal 1
        MvcResult resultadoSucursal1 = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Sucursal 1"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursal1Id = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal1.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear sucursal 2
        MvcResult resultadoSucursal2 = mockMvc.perform(
                        post("/api/franquicias/" + franquiciaId + "/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Sucursal 2"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long sucursal2Id = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoSucursal2.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Crear producto en sucursal 1
        MvcResult resultadoProducto = mockMvc.perform(
                        post("/api/sucursales/" + sucursal1Id + "/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "nombre": "Producto Protegido",
                                        "stock": 100
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long productoId = new tools.jackson.databind
                .ObjectMapper()
                .readTree(
                        resultadoProducto.getResponse().getContentAsString()
                )
                .get("id")
                .asLong();

        // Intentar eliminarlo desde la sucursal 2
        mockMvc.perform(
                        delete("/api/sucursales/"
                                + sucursal2Id
                                + "/productos/"
                                + productoId)
                )
                .andExpect(status().isNotFound());

        // Verificar que el producto sigue existiendo
        mockMvc.perform(
                        patch("/api/sucursales/"
                                + sucursal1Id
                                + "/productos/"
                                + productoId
                                + "/stock")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "stock": 150
                                    }
                                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(150));
    }

}