package feira.graspcrud;

import feira.graspcrud.controller.BarracaController;
import feira.graspcrud.repository.BarracaRepository;
import feira.graspcrud.repository.TipoBarracaRepository;
import feira.graspcrud.repositoryJson.BarracaRepositoryJson;
import feira.graspcrud.repositoryJson.TipoBarracaRepositoryJson;
import feira.graspcrud.service.BarracaService;
import feira.graspcrud.service.TipoBarracaService;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TipoBarracaRepository tipoRepository = new TipoBarracaRepositoryJson();
        BarracaRepository barracaRepository   = new BarracaRepositoryJson();

        TipoBarracaService tipoService    = new TipoBarracaService(tipoRepository, barracaRepository);
        BarracaService     barracaService = new BarracaService(barracaRepository, tipoRepository);

        try (Scanner scanner = new Scanner(System.in)) {
            BarracaController controller = new BarracaController(barracaService, tipoService, scanner);
            controller.iniciarMenu();
        }

        System.out.println("Sistema finalizado. Ate logo!");
    }
}
