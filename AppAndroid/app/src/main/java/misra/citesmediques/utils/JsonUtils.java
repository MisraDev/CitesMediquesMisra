package misra.citesmediques.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class JsonUtils {
    public static String generarJSON(String tipoFuncion, Object... parametros) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tipo", tipoFuncion);

        JsonObject parametrosJson = new JsonObject();
        for (int i = 0; i < parametros.length; i++) {
            Object parametro = parametros[i];
            if (parametro instanceof String) {
                parametrosJson.addProperty("param" + i, (String) parametro);
            } else if (parametro instanceof Integer) {
                parametrosJson.addProperty("param" + i, (Integer) parametro);
            } else if (parametro instanceof Double) {
                parametrosJson.addProperty("param" + i, (Double) parametro);
            } else if (parametro instanceof Date) {
                // Aquí puedes definir el formato en el que deseas representar la fecha en el JSON
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fechaString = dateFormat.format((Date) parametro);
                parametrosJson.addProperty("param" + i, fechaString);
            }
            // Agrega más casos según los tipos de parámetros que necesites
        }

        jsonObject.add("parametros", parametrosJson);

        return gson.toJson(jsonObject);
    }

}

