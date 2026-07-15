package com.example.impulsolocalmovil.api

import com.example.impulsolocalmovil.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ============ AUTENTICACIÓN ============

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    // ============ EMPRENDIMIENTOS ============

    @GET("emprendimientos")
    suspend fun getVentures(): Response<List<Emprendimiento>>

    @GET("emprendimientos/{id}")
    suspend fun getVentureById(@Path("id") id: Int): Response<Emprendimiento>

    @GET("emprendimientos/usuario/{usuario_id}")
    suspend fun getMyVentures(@Path("usuario_id") usuarioId: Int): Response<List<Emprendimiento>>

    @POST("emprendimientos")
    suspend fun createVenture(@Body request: EmprendimientoRequest): Response<Emprendimiento>

    @Multipart
    @POST("emprendimientos")
    suspend fun createVentureWithImage(
        @Part("nombre") nombre: String,
        @Part("descripcion") descripcion: String,
        @Part("categoria_id") categoriaId: Int,
        @Part("ubicacion") ubicacion: String,
        @Part("usuario_id") usuarioId: Int,
        @Part("estado") estado: String,
        @Part image: MultipartBody.Part?
    ): Response<Emprendimiento>

    @DELETE("emprendimientos/{id}")
    suspend fun deleteVenture(@Path("id") id: Int): Response<Unit>

    // ============ CATEGORÍAS ============

    @GET("categorias")
    suspend fun getCategories(): Response<List<Categoria>>

    // ============ PRODUCTOS ============
//
    @GET("productos/emprendimiento/{emprendimiento_id}")
    suspend fun getProductsByVenture(@Path("emprendimiento_id") emprendimientoId: Int): Response<List<Producto>>

    @POST("productos")
    suspend fun createProduct(@Body request: ProductoRequest): Response<Producto>

    @DELETE("productos/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>

    // ============ RESEÑAS ============
    @GET("resenas/emprendimiento/{emprendimiento_id}")
    suspend fun getResenas(@Path("emprendimiento_id") emprendimientoId: Int): Response<List<Resena>>

    @POST("resenas")
    suspend fun crearResena(@Body request: ResenaRequest): Response<Resena>

    @PUT("resenas/{id}")
    suspend fun actualizarResena(
        @Path("id") id: Int,
        @Body request: ResenaUpdateRequest
    ): Response<Resena>

    @DELETE("resenas/{id}")
    suspend fun eliminarResena(@Path("id") id: Int): Response<Unit>


    // ============ CURSOS ============

    @GET("cursos")
    suspend fun getCourses(): Response<List<Curso>>

    @GET("cursos/usuario/{usuario_id}")
    suspend fun getMyCourses(@Path("usuario_id") usuarioId: Int): Response<List<InscripcionCurso>>

    @POST("cursos/inscribir")
    suspend fun enrollCourse(@Body request: EnrollRequest): Response<InscripcionCurso>

    @GET("cursos/{id}")
    suspend fun getCursoById(@Path("id") id: Int): Response<Curso>

    @DELETE("cursos/inscripcion/{id}")
    suspend fun cancelEnrollment(
        @Path("id") id: Int,
        @Query("usuario_id") usuarioId: Int
    ): Response<Unit>

    // ============ EVENTOS ============

    @GET("eventos")
    suspend fun getEvents(): Response<List<Evento>>

    @GET("eventos/usuario/{usuario_id}")
    suspend fun getMyEvents(@Path("usuario_id") usuarioId: Int): Response<List<AsistenciaEvento>>

    @POST("eventos/confirmar-asistencia")
    suspend fun confirmAttendance(@Body request: AsistenciaRequest): Response<AsistenciaEvento>

    @GET("eventos/{id}")
    suspend fun getEventById(@Path("id") id: Int): Response<Evento>

    @DELETE("eventos/asistencia/{id}")
    suspend fun cancelAttendance(
        @Path("id") id: Int,
        @Query("usuario_id") usuarioId: Int
    ): Response<Unit>

    // ============ ME ENCANTA ============

    @GET("me-encanta/usuario/{usuario_id}")
    suspend fun getMyLikes(@Path("usuario_id") usuarioId: Int): Response<List<MeEncanta>>

    @POST("me-encanta/toggle")
    suspend fun toggleLike(@Body request: ToggleLikeRequest): Response<ToggleLikeResponse>

    // ============ TESTIMONIOS ============

    @GET("testimonios")
    suspend fun getTestimonials(): Response<List<Testimonio>>

    @POST("testimonios")
    suspend fun createTestimonial(@Body request: TestimonioRequest): Response<Testimonio>

    @DELETE("testimonios/{id}")
    suspend fun deleteTestimonial(
        @Path("id") id: Int,
        @Query("usuario_id") usuarioId: Int
    ): Response<Unit>


    // ============ CARRITO ============

    @GET("carrito/{usuario_id}")
    suspend fun getCart(@Path("usuario_id") usuarioId: Int): Response<CarritoResponse>

    @POST("carrito/agregar")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<CarritoItem>

    @DELETE("carrito/eliminar/{item_id}")
    suspend fun removeFromCart(@Path("item_id") itemId: Int): Response<Unit>

    @DELETE("carrito/vaciar/{usuario_id}")
    suspend fun clearCart(@Path("usuario_id") usuarioId: Int): Response<Unit>

    // ============ ÓRDENES ============

    @POST("ordenes")
    suspend fun createOrder(@Body request: OrdenRequest): Response<Orden>

    @GET("ordenes/usuario/{usuario_id}")
    suspend fun getMyOrders(@Path("usuario_id") usuarioId: Int): Response<List<Orden>>

    @GET("ordenes/{id}")
    suspend fun getOrderById(@Path("id") id: Int): Response<Orden>

    @PUT("ordenes/{id}/cancelar")
    suspend fun cancelOrder(@Path("id") id: Int): Response<Unit>

    // ============ PERFIL ============

    @GET("perfil/{id}")
    suspend fun getProfile(@Path("id") id: Int): Response<Usuario>

    @FormUrlEncoded
    @PUT("perfil/{id}")
    suspend fun updateProfile(
        @Path("id") id: Int,
        @FieldMap fields: Map<String, String>
    ): Response<Usuario>

    // ============ CAMBIAR PASSWORD ============

    @FormUrlEncoded
    @PUT("cambiar-password/{id}")
    suspend fun changePassword(
        @Path("id") id: Int,
        @FieldMap fields: Map<String, String>
    ): Response<Unit>


    // ============ CHAT ============
    @GET("conversaciones/{usuario_id}")
    suspend fun getConversaciones(@Path("usuario_id") usuarioId: Int): Response<List<ConversacionResumen>>

    @GET("usuarios/disponibles/{usuario_id}")
    suspend fun getUsuariosDisponibles(@Path("usuario_id") usuarioId: Int): Response<List<Chat>>

    @GET("usuarios/{id}")
    suspend fun getUsuarioChat(@Path("id") id: Int): Response<Chat>

    @GET("mensajes/{conversacion_id}")
    suspend fun getMensajes(@Path("conversacion_id") conversacionId: Int): Response<List<Mensaje>>

    @POST("mensajes")
    suspend fun enviarMensaje(@Body request: MensajeRequest): Response<EnviarMensajeResponse>

    @PUT("mensajes/leer/{conversacion_id}")
    suspend fun marcarComoLeidos(
        @Path("conversacion_id") conversacionId: Int,
        @Body request: MarcarLeidosRequest
    ): Response<Map<String, @JvmSuppressWildcards Any>>

    @DELETE("conversaciones/{conversacion_id}")
    suspend fun eliminarConversacion(@Path("conversacion_id") conversacionId: Int): Response<Unit>


    // ============ ADMIN ============

    @GET("admin/stats")
    suspend fun getAdminStats(): Response<AdminStats>

    @GET("admin/orders")
    suspend fun getAllOrders(): Response<List<Orden>>

    @GET("admin/reviews")
    suspend fun getAllReviews(): Response<List<Resena>>

    @GET("usuarios")
    suspend fun getAllUsers(): Response<List<Usuario>>

}