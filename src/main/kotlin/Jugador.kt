package es.iesra.prog2425_ahorcado


/**
 * Representa un jugador en el juego del ahorcado.
 *
 * @property intentos El número de intentos que el jugador tiene para adivinar la palabra.
 */
class Jugador(intentos: Int) {

    var intentos = intentos
        private set

    private var letrasUsadas = mutableSetOf<Char>()


    /**
     * Intenta agregar una letra a la lista de letras usadas por el jugador.
     * Si la letra ya fue utilizada anteriormente, no se agregará.
     *
     * @param letra La letra que el jugador quiere intentar.
     * @return `true` si la letra no había sido usada antes, `false` si ya fue usada.
     */
    fun intentarLetra(letra :Char) :Boolean{
        if (letra !in letrasUsadas){
            letrasUsadas.add(letra)
            return true
        }
        return false
    }


    /**
     * Disminuye el número de intentos disponibles del jugador en uno.
     */
    fun fallarIntento(){intentos --}


    /**
     * Obtiene las letras que el jugador ha utilizado hasta el momento.
     *
     * @return Una cadena con las letras usadas separadas por espacios.
     */
    fun obtenerLetrasUsadas() :String{
        var cadena = ""

        for(letra in letrasUsadas){cadena += "$letra "}
        return cadena
    }

}