package es.iesra.prog2425_ahorcado

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.runBlocking


/**
 * Representa una palabra oculta en el juego del ahorcado.
 *
 * @property palabraOculta La palabra que el jugador debe adivinar.
 */
class Palabra(var palabraOculta :String) {
    private val progreso = CharArray(palabraOculta.length){'_'}


    /**
     * Devuelve el número de letras de la palabra oculta.
     *
     * @return El número de letras de la palabra.
     */
    fun nLetras() :Int{
        return palabraOculta.length
    }


    /**
     * Revela una letra en la palabra oculta si es correcta.
     * Si la letra se encuentra en la palabra, la reemplaza en el progreso.
     *
     * @param letra La letra que se intentará revelar.
     * @return `true` si la letra se encuentra en la palabra y se ha revelado, `false` si no está en la palabra.
     */
    fun revelarLetra(letra :Char) :Boolean{
        var acierto = false
        var contador = 0
        for (letrap in palabraOculta){
            if (letrap.quitarAcentos() == letra.quitarAcentos()){
                progreso[contador] = letrap
                acierto = true
            }
            contador ++
        }
        return acierto
    }



    /**
     * Devuelve el progreso actual de la palabra oculta, mostrando las letras reveladas y los guiones bajos.
     *
     * @return El progreso actual como una cadena de caracteres.
     */
    fun obtenerProgreso() :String{
        var progresoObtenido = ""

        for(letra in progreso){progresoObtenido += "$letra "}
        return progresoObtenido
    }


    /**
     * Verifica si la palabra ha sido completamente revelada.
     *
     * @return `true` si la palabra está completa (sin guiones bajos), `false` en caso contrario.
     */
    fun esCompleta() :Boolean{
        for (letra in progreso){
            if (letra == '_'){return false}
        }
        return true
    }

    companion object{


        /**
         * Genera un conjunto de palabras aleatorias basadas en parámetros como la cantidad, tamaño y idioma.
         *
         * @param cantidad El número de palabras que se desean generar.
         * @param tamanioMin El tamaño mínimo de las palabras.
         * @param tamanioMax El tamaño máximo de las palabras.
         * @param idioma El idioma de las palabras generadas (por defecto es Español).
         * @return Un conjunto mutable de objetos `Palabra` generados.
         */
        fun generarPalabras(cantidad: Int, tamanioMin: Int, tamanioMax: Int, idioma: Idioma = Idioma.ES): MutableSet<Palabra> {
            val client = HttpClient {
                install(ContentNegotiation) {
                    gson()
                }
            }

            val palabras = mutableSetOf<Palabra>() // Usamos un conjunto para evitar repeticiones
            val url = "https://random-word-api.herokuapp.com/word?number=${cantidad * 5}&lang=${idioma.codigo}"

            val patron = if (idioma == Idioma.ES) {
                "^[a-záéíóúüñ]+$"
            } else {
                "^[a-z]+$"
            }

            runBlocking {
                try {
                    while (palabras.size < cantidad) {
                        // Hacemos la solicitud GET
                        val respuesta: Array<String> = client.get(url).body()

                        // Filtramos las palabras según las condiciones
                        val filtradas = respuesta
                            .map { it.trim().lowercase() } // Convertimos a minúsculas
                            .filter { it.length in tamanioMin..tamanioMax } // Filtramos por tamaño
                            .filter { it.matches(Regex(patron)) } // Solo letras
                            .filter { !it.contains(" ") } // Excluye palabras que contengan espacios
                            .map { Palabra(it) } // Mapeamos a la data class

                        palabras.addAll(filtradas)
                    }
                } catch (e: Exception) {
                    println("Error al obtener las palabras: ${e.message}")
                }
            }

            client.close()
            return palabras.take(cantidad).toMutableSet()
        }
    }
}