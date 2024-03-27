package net.amentum.security;

/**
 * Created by dev06 on 12/04/17.
 */
public class Utils {

    /**
     * Método para concatenar un string de la forma %cadena%
     * @param pattern el parametro a ser formateado
     * @return  la cadena con el formato
     * */
    public static String getPatternLike(String pattern){
        return new StringBuffer("%").append(pattern.toLowerCase()).append("%").toString();
    }
}
