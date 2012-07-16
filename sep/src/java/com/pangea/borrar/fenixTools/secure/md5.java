/**
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Pasteurizadora Tachira CA</p>
 * @author Desconocido
 * @version 0.1v
 */

package com.pangea.borrar.fenixTools.secure;

import java.security.MessageDigest;

/**
 *
 * @author psistemas
 */
public class md5 {
    /**
     * Encripta un String con el algoritmo MD5.
     * @return String
     * @throws Exception
     */
    private static String hash(String clear) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(clear.getBytes());
        CheckString x;
        
        int size = b.length;
        StringBuilder h = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int u = b[i] & 255; // unsigned conversion
            if (u < 16) {
                h.append("0").append(Integer.toHexString(u));
            } else {
                h.append(Integer.toHexString(u));
            }
        }
        return h.toString();
    }

    /**
     * Encripta un String con el algoritmo MD5.
     * @return String
     * @throws Exception
     */
    public static String encriptar(String palabra) throws Exception {
        String pe = "";
        try {
            pe = hash(hash(palabra));
        } catch (Exception e) {
            throw new Error("<strong>Error: Al encriptar el password</strong>");
        }
        return pe;
    }
}
