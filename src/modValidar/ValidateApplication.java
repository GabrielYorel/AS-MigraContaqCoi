package modValidar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

/**
 *
 * @author Gabriel
 */
public final class ValidateApplication {

    String Key;
    String Val;
    String nSerie;
    String nombreProducto;
    private String arNomProdMD5;
    private String arRazonSocial;
    public static String archivoDll = "lib\\registration.dll";
    public static String archivoExe = "lib\\NPCS.exe";
    public static String archivodllSe = "NPCS.dll";

    public static boolean validar() {
        ValidateApplication obj = new ValidateApplication();
        return obj.comprobarActivacion();
    }

    public char getTipoInstalacion(String linea) {
        char tipo = 0;
        try {
            tipo = (char) linea.charAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tipo;
    }

    public Date getFechaInst(String linea) {
        Date fecha = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String sFecha = linea.substring(2, linea.length() - 3);
            fecha = df.parse(sFecha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public int getDiasDemo(String linea) {
        int dias = 0;
        try {
            String sDias = linea.substring(linea.length() - 2, linea.length());
            dias = Integer.parseInt(sDias);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dias;
    }

    public Date getFechaSI() {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date fechaSI = null;
        try {
            Calendar cal1 = Calendar.getInstance();
            String fecha = (cal1.get(Calendar.DATE) + "-" + (cal1.get(Calendar.MONTH) + 1) + "-" + cal1.get(Calendar.YEAR));
            fechaSI = formato.parse(fecha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fechaSI;
    }

    public boolean comprobarActivacion() {
        boolean banderaCorrecto = false;
        try { // Software\\My Application
            File archivo = new File(archivoDll);
            if (archivo.exists()) {
                File archivo2 = new File(archivoExe);
                if (archivo2.exists()) {
                    String linea = getRot13(archivoDll);
                    arNomProdMD5 = linea.substring(0, linea.indexOf("desc"));
                    arRazonSocial = linea.substring(linea.indexOf("desc") + 4, linea.indexOf("[ProdN]"));
                    nombreProducto = linea.substring(linea.indexOf("[ProdN]") + 7, linea.length());
                    String serieACC = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Asciende\\" + nombreProducto, "Serie Acc");
                    String razonSocial = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Asciende\\" + nombreProducto, "RS");
                    String cda = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Asciende\\" + nombreProducto, "CDA");
                    String dllReg = "C:/Windows/System32/" + serieACC + ".dll";
                    if (!(new File(dllReg).exists())) {
                        banderaCorrecto = false;
                    } else {
                        String lineaDll = getRot13(dllReg);
                        if (getTipoInstalacion(lineaDll) != 0) {
                            if (getTipoInstalacion(lineaDll) == 'C') {
                                if (cda == null || serieACC == null || razonSocial == null
                                        || serieACC.equalsIgnoreCase("") || cda.equalsIgnoreCase("") || razonSocial.equalsIgnoreCase("")) {
                                    banderaCorrecto = false;
                                } else {//comparar las claves
                                    if (validarCDA(serieACC, razonSocial, cda) == false) {
                                        banderaCorrecto = false;
                                    } else {
                                        banderaCorrecto = true;
                                    }
                                }
                            } else if (getTipoInstalacion(lineaDll) == 'D') {
                                Date fecha = getFechaInst(lineaDll);
                                int dias = getDiasDemo(lineaDll);
                                if (fecha != null && dias != 0) {
                                    if (getFechaSI().before(fecha)) {
                                        JOptionPane.showMessageDialog(null, "El periodo de prueba ha terminado", "La aplicacion ha expirado", JOptionPane.ERROR_MESSAGE);
                                        System.exit(0);
                                    } else {
                                        final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día
                                        long diferencia = (getFechaSI().getTime() - fecha.getTime()) / MILLSECS_PER_DAY;
                                        if (diferencia > dias) {
                                            JOptionPane.showMessageDialog(null, "El periodo de prueba ha terminado", "La aplicacion ha expirado", JOptionPane.ERROR_MESSAGE);
                                            System.exit(0);
                                        } else {
                                            if (cda == null || serieACC == null || razonSocial == null
                                                    || serieACC.equalsIgnoreCase("") || cda.equalsIgnoreCase("") || razonSocial.equalsIgnoreCase("")) {
                                                banderaCorrecto = false;
                                            } else {//comparar las claves
                                                if (validarCDA(serieACC, razonSocial, cda) == false) {
                                                    banderaCorrecto = false;
                                                } else {
                                                    banderaCorrecto = true;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    banderaCorrecto = false;
                                }
                            } else {
                                banderaCorrecto = false;
                            }
                        } else {
                            banderaCorrecto = false;
                        }
                    }
                } else {
                    banderaCorrecto = false;
                }
            } else {
                banderaCorrecto = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return banderaCorrecto;
    }

    public String ejectDelphi() {
        String texto = "";
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            archivo = new File(archivodllSe);
            if (archivo.exists()) {
                archivo.delete();
            }
            Process p = Runtime.getRuntime().exec("cmd.exe /K start " + archivoExe);
            try {
                Thread.sleep(3000);
                // Apertura del fichero y creacion de BufferedReader para poder
                archivo = new File(archivodllSe);
                if (!archivo.exists()) {
                    JOptionPane.showMessageDialog(null, "La aplicación no se instaldo correctamente", "Error en la aplicación", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                } else {
                    fr = new FileReader(archivo);
                    br = new BufferedReader(fr);
                    // Lectura del fichero
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        texto = linea;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // En el finally cerramos el fichero, para asegurarnos
                // que se cierra tanto si todo va bien como si salta
                // una excepcion.
                try {
                    if (null != fr) {
                        fr.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            archivo.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return texto;
    }

    public boolean validarCDA(String serieACC, String razonSocial, String cdaReg) {
        boolean bandera = false;
        try {
            String pcSerial = ejectDelphi();
            if (!pcSerial.equalsIgnoreCase("")) {
                if (getMD5(razonSocial).equals(arRazonSocial)) {
                    if (getMD5(nombreProducto).equals(arNomProdMD5)) {
                        String sSerie = ValidateApplication.getMD5(pcSerial + "" + serieACC);
                        int intSerie = ValidateApplication.getDecimal(sSerie);
                        int intProducto = ValidateApplication.getDecimal(serieACC);
                        String sRazonSocial = ValidateApplication.getMD5(razonSocial);
                        int intRazon = ValidateApplication.getDecimal(sRazonSocial);
                        int prefix = Integer.parseInt(pcSerial.substring(0, 3));
                        int cda = 0;
                        if (prefix > 0) {
                            cda = ((intSerie + intProducto) + intRazon) * prefix;
                        } else {
                            cda = ((intSerie + intProducto) + intRazon) + prefix;
                        }
                        String key = cda + "" + sSerie + "" + serieACC + "" + sRazonSocial;
                        if (ValidateApplication.getMD5(key).equalsIgnoreCase(cdaReg)) {
                            bandera = true;
                        } else {
                            bandera = false;
                        }
                    } else {
                        bandera = false;
                    }
                } else {
                    bandera = false;
                }
            } else {
                bandera = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            bandera = false;
        }
        return bandera;
    }

    static String rot13(String cadena) {
        char c;
        StringBuilder temp = new StringBuilder();        //crea un StringBuilder para construir la cadena resultante en la variable temp
        for (int i = 0; i < cadena.length(); i++) {    //comienza el bucle para analizar los caracteres de la cadena
            c = cadena.charAt(i);                        //se obtiene el carácter de la posición actual de la cadena y se guarda en la variable c
            if (c >= 'A' && c < 'N') {            //compara c con A y N
                c += 13;                          //si es igual o mayor que A y menor que N, se realiza un desplazamiento sumándole 13
            } else if (c >= 'N' && c <= 'Z') {    //compara c con N y Z
                c -= 13;                          //si es igual o mayor que N e igual o menor que Z, se realiza un desplazamiento restándole 13
            } else if (c >= 'a' && c < 'n') {     //compara c con a y n
                c += 13;                          //si es igual o mayor que a y menor que n, se realiza un desplazamiento sumándole 13
            } else if (c >= 'n' && c <= 'z') {    //compara c con n y z
                c -= 13;                          //si es igual o mayor que n e igual o menor que z, se realiza un desplazamiento restándole 13
            }
            temp.append(c);    //se añade c en temp
        }
        return temp.toString();    //retornamos la variable temp, la cual contiene la cadena cifrada o descifrada
    }

    public static int getDecimal(String cadena) {
        int decimal = 0;
        for (int i = 0; i < cadena.length(); i++) {
            char car = cadena.charAt(i);
            decimal = decimal + (int) car;
        }
        return decimal;
    }

    public String getRot13(String archivoDll) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String texto = "";
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File(archivoDll);
            if (!archivo.exists()) {
//                JOptionPane.showMessageDialog(null, "La aplicación no se instaldo correctamente", "Error en la aplicación", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } else {
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                // Lectura del fichero
                String linea;
                while ((linea = br.readLine()) != null) {
                    texto = linea;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return rot13(texto);
    }

    public static String getMD5(String cadena) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(cadena.getBytes());

        int size = b.length;
        StringBuilder h = new StringBuilder(size);
        for (int i = 0; i < size; i++) {

            int u = b[i] & 255;

            if (u < 16) {
                h.append("0").append(Integer.toHexString(u));
            } else {
                h.append(Integer.toHexString(u));
            }
        }
        return h.toString();
    }
}

class WinRegistry {

    public static final int HKEY_CURRENT_USER = 0x80000001;
    public static final int HKEY_LOCAL_MACHINE = 0x80000002;
    public static final int HKEY_CLASSES_ROOT = 0x80000003;
    public static final int REG_SUCCESS = 0;
    public static final int REG_NOTFOUND = 2;
    public static final int REG_ACCESSDENIED = 5;
    private static final int KEY_ALL_ACCESS = 0xf003f;
    private static final int KEY_READ = 0x20019;
    private static Preferences userRoot = Preferences.userRoot();
    private static Preferences systemRoot = Preferences.systemRoot();
    private static Class<? extends Preferences> userClass = userRoot.getClass();
    private static Method regOpenKey = null;
    private static Method regCloseKey = null;
    private static Method regQueryValueEx = null;
    private static Method regEnumValue = null;
    private static Method regQueryInfoKey = null;
    private static Method regEnumKeyEx = null;
    private static Method regCreateKeyEx = null;
    private static Method regSetValueEx = null;
    private static Method regDeleteKey = null;
    private static Method regDeleteValue = null;

    static {
        try {
            regOpenKey = userClass.getDeclaredMethod("WindowsRegOpenKey",
                    new Class[]{int.class, byte[].class, int.class});
            regOpenKey.setAccessible(true);
            regCloseKey = userClass.getDeclaredMethod("WindowsRegCloseKey",
                    new Class[]{int.class});
            regCloseKey.setAccessible(true);
            regQueryValueEx = userClass.getDeclaredMethod("WindowsRegQueryValueEx",
                    new Class[]{int.class, byte[].class});
            regQueryValueEx.setAccessible(true);
            regEnumValue = userClass.getDeclaredMethod("WindowsRegEnumValue",
                    new Class[]{int.class, int.class, int.class});
            regEnumValue.setAccessible(true);
            regQueryInfoKey = userClass.getDeclaredMethod("WindowsRegQueryInfoKey1",
                    new Class[]{int.class});
            regQueryInfoKey.setAccessible(true);
            regEnumKeyEx = userClass.getDeclaredMethod(
                    "WindowsRegEnumKeyEx", new Class[]{int.class, int.class,
                int.class});
            regEnumKeyEx.setAccessible(true);
            regCreateKeyEx = userClass.getDeclaredMethod(
                    "WindowsRegCreateKeyEx", new Class[]{int.class,
                byte[].class});
            regCreateKeyEx.setAccessible(true);
            regSetValueEx = userClass.getDeclaredMethod(
                    "WindowsRegSetValueEx", new Class[]{int.class,
                byte[].class, byte[].class});
            regSetValueEx.setAccessible(true);
            regDeleteValue = userClass.getDeclaredMethod(
                    "WindowsRegDeleteValue", new Class[]{int.class,
                byte[].class});
            regDeleteValue.setAccessible(true);
            regDeleteKey = userClass.getDeclaredMethod(
                    "WindowsRegDeleteKey", new Class[]{int.class,
                byte[].class});
            regDeleteKey.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WinRegistry() {
    }

    /**
     * Read a value from key and value name
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @param valueName
     * @return the value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String readString(int hkey, String key, String valueName)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            return readString(userRoot, hkey, key, valueName);
        } else if (hkey == HKEY_CURRENT_USER) {
            return readString(userRoot, hkey, key, valueName);
        } else {
            throw new IllegalArgumentException("hkey=" + hkey);
        }
    }

    /**
     * Read value(s) and value name(s) form given key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @return the value name(s) plus the value(s)
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map<String, String> readStringValues(int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            return readStringValues(userRoot, hkey, key);
        } else if (hkey == HKEY_CURRENT_USER) {
            return readStringValues(userRoot, hkey, key);
        } else {
            throw new IllegalArgumentException("hkey=" + hkey);
        }
    }

    /**
     * Read the value name(s) from a given key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @return the value name(s)
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static List<String> readStringSubKeys(int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            return readStringSubKeys(userRoot, hkey, key);
        } else if (hkey == HKEY_CURRENT_USER) {
            return readStringSubKeys(userRoot, hkey, key);
        } else {
            throw new IllegalArgumentException("hkey=" + hkey);
        }
    }

    private static String readString(Preferences root, int hkey, String key, String value)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        int[] handles = (int[]) regOpenKey.invoke(root, new Object[]{
            new Integer(hkey), toCstr(key), new Integer(KEY_READ)});
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        byte[] valb = (byte[]) regQueryValueEx.invoke(root, new Object[]{
            new Integer(handles[0]), toCstr(value)});
        regCloseKey.invoke(root, new Object[]{new Integer(handles[0])});
        return (valb != null ? new String(valb).trim() : null);
    }

    private static Map<String, String> readStringValues(Preferences root, int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        HashMap<String, String> results = new HashMap<String, String>();
        int[] handles = (int[]) regOpenKey.invoke(root, new Object[]{
            new Integer(hkey), toCstr(key), new Integer(KEY_READ)});
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        int[] info = (int[]) regQueryInfoKey.invoke(root,
                new Object[]{new Integer(handles[0])});

        int count = info[2]; // count  
        int maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            byte[] name = (byte[]) regEnumValue.invoke(root, new Object[]{
                new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1)});
            String value = readString(hkey, key, new String(name));
            results.put(new String(name).trim(), value);
        }
        regCloseKey.invoke(root, new Object[]{new Integer(handles[0])});
        return results;
    }

    private static List<String> readStringSubKeys(Preferences root, int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        List<String> results = new ArrayList<String>();
        int[] handles = (int[]) regOpenKey.invoke(root, new Object[]{
            new Integer(hkey), toCstr(key), new Integer(KEY_READ)
        });
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        int[] info = (int[]) regQueryInfoKey.invoke(root,
                new Object[]{new Integer(handles[0])});

        int count = info[2]; // count  
        int maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            byte[] name = (byte[]) regEnumKeyEx.invoke(root, new Object[]{
                new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1)
            });
            results.add(new String(name).trim());
        }
        regCloseKey.invoke(root, new Object[]{new Integer(handles[0])});
        return results;
    }

    // utility
    private static byte[] toCstr(String str) {
        byte[] result = new byte[str.length() + 1];

        for (int i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }
}
