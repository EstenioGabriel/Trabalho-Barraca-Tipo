package feira.graspcrud.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class JsonMini {

    public static void salvar(String caminho, List<String> linhas) {
        try {
            Files.createDirectories(Paths.get("data"));
            String json = "[\n" + String.join(",\n", linhas) + "\n]";
            Files.write(Paths.get(caminho), json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static List<String> ler(String caminho) {
        try {
            Path path = Paths.get(caminho);
            if (!Files.exists(path)) return new ArrayList<>();

            byte[] bytes = Files.readAllBytes(path);
            String conteudo = new String(bytes, StandardCharsets.UTF_8);

            conteudo = conteudo.replace("[", "").replace("]", "").trim();
            if (conteudo.isEmpty()) return new ArrayList<>();

            String[] partes = conteudo.split("(?<=\\}),\\s*");
            List<String> resultado = new ArrayList<>();
            for (String p : partes) {
                resultado.add(p);
            }
            return resultado;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}