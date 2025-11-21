package com.example.booktookv3

object testerCatalog {
    data class Libro(
        val titulo: String,
        val autor: String,
        val editorial: String,
        val isbn: String,
        val anioPublicacion: Int,
        val portadaResId: Int,
        val estado: EstadoLectura,
        val generoPrincipal: String,
        val generoSecundario: String
    )

    enum class EstadoLectura{
        LEYENDO,
        LEIDO,
        QUIERO_LEER
    }

    //Repositorio de libros y su información.
    val librosDemo = listOf(
        Libro(
            titulo = "It",
            autor = "Stephen King",
            editorial = "Debolsillo",
            isbn = "9788466357302",
            anioPublicacion = 2021,
            portadaResId = R.drawable.it,
            estado = EstadoLectura.QUIERO_LEER,
            generoPrincipal = "Terror",
            generoSecundario = "Suspense"
        ),
        Libro(
            titulo = "La mala costumbre",
            autor = "Alana S. Portero",
            editorial = "Seix Barral",
            isbn = "9788432242120",
            anioPublicacion = 2023,
            portadaResId = R.drawable.la_mala_costumbre,
            estado = EstadoLectura.LEIDO,
            generoPrincipal = "Ficción contemporánea",
            generoSecundario = "LGTBIQ+"
        ),
        Libro(
            titulo = "Las malas",
            autor = "Camila Sosa Villada",
            editorial = "Tusquets Editores",
            isbn = "9788490668061",
            anioPublicacion = 2020,
            portadaResId = R.drawable.las_malas,
            estado = EstadoLectura.LEIDO,
            generoPrincipal = "Ficción contemporánea",
            generoSecundario = "LGTBIQ+"
        ),
        Libro(
            titulo = "Crepúsculo",
            autor = "Stephenie Meyer",
            editorial = "Alfaguara",
            isbn = "9788420469287",
            anioPublicacion = 2006,
            portadaResId = R.drawable.crepusculo,
            estado = EstadoLectura.LEIDO,
            generoPrincipal = "Romántica juvenil",
            generoSecundario = "Fantasía"
        ),
        Libro(
            titulo = "Maldito karma",
            autor = "David Safier",
            editorial = "Seix Barral",
            isbn = "9788432228582",
            anioPublicacion = 2009,
            portadaResId = R.drawable.maldito_karma,
            estado = EstadoLectura.QUIERO_LEER,
            generoPrincipal = "Comedia",
            generoSecundario = "Ficción"
        ),
        Libro(
            titulo = "Sin noticias de Gurb",
            autor = "Eduardo Mendoza",
            editorial = "Seix Barral",
            isbn = "9788432207822",
            anioPublicacion = 2001,
            portadaResId = R.drawable.sin_noticias_de_gurb,
            estado = EstadoLectura.LEIDO,
            generoPrincipal = "Comedia",
            generoSecundario = "Sátira"
        ),
        Libro(
            titulo = "Rojo, blanco y sangre azul",
            autor = "Casey McQuiston",
            editorial = "Molino",
            isbn = "9788427218697",
            anioPublicacion = 2019,
            portadaResId = R.drawable.rojo_blanco_sangre_azul,
            estado = EstadoLectura.QUIERO_LEER,
            generoPrincipal = "Romance",
            generoSecundario = "Romcom"
        ),
        Libro(
            titulo = "Los pilares de la Tierra",
            autor = "Ken Follett",
            editorial = "Debolsillo",
            isbn = "9788499088037",
            anioPublicacion = 2010,
            portadaResId = R.drawable.los_pilares_de_la_tierra,
            estado = EstadoLectura.QUIERO_LEER,
            generoPrincipal = "Novela histórica",
            generoSecundario = "Ficción"
        ),
        Libro(
            titulo = "La catedral del mar",
            autor = "Ildefonso Falcones",
            editorial = "Debolsillo",
            isbn = "9788499088044",
            anioPublicacion = 2011,
            portadaResId = R.drawable.la_catedral_del_mar,
            estado = EstadoLectura.LEIDO,
            generoPrincipal = "Novela histórica",
            generoSecundario = "Ficción"
        )
    )

}