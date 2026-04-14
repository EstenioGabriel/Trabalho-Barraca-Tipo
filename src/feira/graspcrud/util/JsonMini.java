package feira.graspcrud.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class JsonMini {

    public static void salvar(String caminho, List<String> linhas) {
        try {
            Files.createDirectories(Paths.get("data"));
            String json = "[\n" + String.join(",\n", linhas) + "\n]";
            Files.writeString(Paths.get(caminho), json);
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static List<String> ler(String caminho) {
        try {
            Path path = Paths.get(caminho);
            if (!Files.exists(path)) return new ArrayList<>();
            
            String conteudo = Files.readString(path);

            conteudo = conteudo.replace("[", "").replace("]", "").trim();
            if (conteudo.isEmpty()) return new ArrayList<>();
            
            String[] partes = conteudo.split("(?<=\\}),\\s*");
            return List.of(partes);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}