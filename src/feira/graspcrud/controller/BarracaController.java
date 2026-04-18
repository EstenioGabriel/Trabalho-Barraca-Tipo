package feira.graspcrud.controller;

import feira.graspcrud.domain.Barraca;
import feira.graspcrud.domain.TipoBarraca;
import feira.graspcrud.dto.BarracaRequest;
import feira.graspcrud.dto.TipoBarracaRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.service.BarracaService;
import feira.graspcrud.service.TipoBarracaService;
import java.util.List;
import java.util.Scanner;

public class BarracaController {

    private final BarracaService barracaService;
    private final TipoBarracaService tipoService;
    private final Scanner scanner;

    public BarracaController(BarracaService barracaService, TipoBarracaService tipoService, Scanner scanner) {
        this.barracaService = barracaService;
        this.tipoService = tipoService;
        this.scanner = scanner;
    }

    public void iniciarMenu() {
        boolean executando = true;
        while (executando) {
            mostrarMenu();
            String opcao = scanner.nextLine().trim();
            try {
                switch (opcao) {
                    case "1": cadastrarTipo();    break;
                    case "2": listarTipos();      break;
                    case "3": cadastrarBarraca(); break;
                    case "4": listarBarracas();   break;
                    case "5": buscarBarraca();    break;
                    case "6": atualizarBarraca(); break;
                    case "7": excluirBarraca();   break;
                    case "8": excluirTipo();      break;
                    case "9": executando = false; break;
                    default:
                        System.out.println("Opcao invalida. Tente novamente.");
                }
            } catch (RegraNegocioException e) {
                System.out.println("\n[ERRO DE NEGOCIO] " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Valor numerico invalido.");
            } catch (Exception e) {
                System.out.println("\n[ERRO] " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void mostrarMenu() {
        System.out.println("=========================================");
        System.out.println("      SISTEMA DE GESTAO - FEIRA LIVRE   ");
        System.out.println("=========================================");
        System.out.println("1. Cadastrar TipoBarraca");
        System.out.println("2. Listar TipoBarraca");
        System.out.println("3. Cadastrar Barraca");
        System.out.println("4. Listar Barraca");
        System.out.println("5. Buscar Barraca por id");
        System.out.println("6. Atualizar Barraca");
        System.out.println("7. Excluir Barraca");
        System.out.println("8. Excluir TipoBarraca");
        System.out.println("9. Sair");
        System.out.println("-----------------------------------------");
        System.out.print("Escolha uma opcao: ");
    }

    private void cadastrarTipo() {
        System.out.print("Nome do tipo: ");
        String nome = scanner.nextLine();
        System.out.print("Descricao do tipo: ");
        String descricao = scanner.nextLine();
        TipoBarraca tipo = tipoService.criar(new TipoBarracaRequest(0, nome, descricao));
        System.out.println("TipoBarraca cadastrado com sucesso! ID: " + tipo.getId());
    }

    private void listarTipos() {
        List<TipoBarraca> tipos = tipoService.listarTodos();
        if (tipos.isEmpty()) {
            System.out.println("Nenhum TipoBarraca cadastrado.");
            return;
        }
        System.out.println("\n--- Tipos de Barraca ---");
        for (TipoBarraca t : tipos) {
            System.out.printf("  [%d] %s - %s%n", t.getId(), t.getNome(), t.getDescricao());
        }
    }

    private void cadastrarBarraca() {
        if (tipoService.listarTodos().isEmpty()) {
            throw new RegraNegocioException("Cadastre ao menos um TipoBarraca antes de cadastrar uma Barraca.");
        }
        listarTipos();
        System.out.print("Nome da barraca: ");
        String nome = scanner.nextLine();
        System.out.print("Descricao: ");
        String descricao = scanner.nextLine();
        System.out.print("ID do tipo: ");
        int tipoId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Barraca ativa? (s/n): ");
        boolean ativo = scanner.nextLine().trim().equalsIgnoreCase("s");
        Barraca b = barracaService.criar(new BarracaRequest(0, nome, descricao, tipoId, ativo));
        System.out.println("Barraca cadastrada com sucesso! ID: " + b.getId());
    }

    private void listarBarracas() {
        List<Barraca> barracas = barracaService.listarTodas();
        if (barracas.isEmpty()) {
            System.out.println("Nenhuma Barraca cadastrada.");
            return;
        }
        System.out.println("\n--- Barracas ---");
        for (Barraca b : barracas) {
            TipoBarraca tipo = tipoService.buscarPorId(b.getTipoId());
            System.out.printf("  [%d] %s | Desc: %s | Tipo: %s | %s%n",
                b.getId(), b.getNome(), b.getDescricao(), tipo.getNome(), b.isAtivo() ? "ATIVA" : "INATIVA");
        }
    }

    private void buscarBarraca() {
        System.out.print("ID da barraca: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Barraca b = barracaService.buscarPorId(id);
        TipoBarraca tipo = tipoService.buscarPorId(b.getTipoId());
        System.out.printf("Encontrada: [%d] %s | Desc: %s | Tipo: %s | %s%n",
            b.getId(), b.getNome(), b.getDescricao(), tipo.getNome(), b.isAtivo() ? "ATIVA" : "INATIVA");
    }

    private void atualizarBarraca() {
        listarBarracas();
        System.out.print("ID da barraca para atualizar: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        listarTipos();
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Nova descricao: ");
        String descricao = scanner.nextLine();
        System.out.print("Novo ID do tipo: ");
        int tipoId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Barraca ativa? (s/n): ");
        boolean ativo = scanner.nextLine().trim().equalsIgnoreCase("s");
        barracaService.atualizar(id, new BarracaRequest(id, nome, descricao, tipoId, ativo));
        System.out.println("Barraca atualizada com sucesso!");
    }

    private void excluirBarraca() {
        listarBarracas();
        System.out.print("ID da barraca para excluir: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        barracaService.remover(id);
        System.out.println("Barraca excluida com sucesso!");
    }

    private void excluirTipo() {
        listarTipos();
        System.out.print("ID do tipo para excluir: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        tipoService.remover(id);
        System.out.println("TipoBarraca excluido com sucesso!");
    }
}
