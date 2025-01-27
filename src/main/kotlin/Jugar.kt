package es.iesra.prog2425_ahorcado


/**
 * Representa una partida del juego del ahorcado entre un jugador y una palabra.
 *
 * @property palabra La palabra que el jugador debe adivinar.
 * @property jugador El jugador que está participando en la partida.
 */
class Jugar(val palabra :Palabra,val jugador :Jugador) {


    /**
     * Inicia el juego mostrando el mensaje de bienvenida y ejecutando el ciclo del juego
     * hasta que el jugador se quede sin intentos o adivine la palabra completa.
     */
    fun iniciar(){
        println("""
            ¡Bienvenido al juego del Ahorcado!
            La palabra tiene ${palabra.nLetras()} letras.
        """.trimIndent())

        while (jugador.intentos > 0 && !palabra.esCompleta()){
            println("""
            Palabra: ${palabra.obtenerProgreso()}
            Intentos restantes: ${jugador.intentos}
            Letras usadas: ${jugador.obtenerLetrasUsadas()}
        """.trimIndent())
            print("Introduce una letra: ")

            val letra = readln().lowercase().firstOrNull()?.quitarAcentos()

            if (letra != null && jugador.intentarLetra(letra)){

                if (palabra.revelarLetra(letra)){
                    println("¡Bien hecho! La letra $letra")
                }else{
                    println("La letra $letra no esta en la palara.")
                    jugador.fallarIntento()
                }

            }else{println("Letra no válida o ya utilizada. Intenta otra vez.")}
        }
        if (palabra.esCompleta()) {
            println("\n¡Felicidades! Has adivinado la palabra: ${palabra.obtenerProgreso()}")
        } else {
            println("\nLo siento, te has quedado sin intentos. La palabra era: ${palabra.palabraOculta}")
        }
    }


    /**
     * Solicita al jugador una respuesta de tipo sí o no.
     *
     * @param msj El mensaje que se muestra al jugador para que responda.
     * @return `true` si el jugador responde "s", `false` si responde "n".
     */
    fun preguntar(msj: String): Boolean {
        do {
            print("$msj (s/n): ")
            val respuesta = readln().trim().lowercase()
            when (respuesta) {
                "s" -> return true
                "n" -> return false
                else -> println("Respuesta no válida! Inténtelo de nuevo...")
            }
        } while (true)
    }

}