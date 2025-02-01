package br.com.projeto.jamusic.Principal;

import br.com.projeto.jamusic.Repository.ArtistaRepository;
import br.com.projeto.jamusic.Service.ConsultaGeminiAI;
import br.com.projeto.jamusic.model.Artista;
import br.com.projeto.jamusic.model.Musica;
import br.com.projeto.jamusic.model.TipoArtista;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private final ArtistaRepository repositorio;
    private Scanner scan = new Scanner(System.in);

    public Principal(ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao!= 9) {
            var menu = """
                    *** Screen Sound Músicas ***                    
                    
                    1- Cadastrar artistas
                    2- Cadastrar músicas
                    3- Listar músicas
                    4- Buscar músicas por artistas
                    5- Pesquisar dados sobre um artista
                    
                    9 - Sair
                    """;

            System.out.println(menu);
            opcao = scan.nextInt();
            scan.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    buscarMusicasPorArtista();
                    break;
                case 5:
                    pesquisarDadosDoArtista();
                    break;
                case 9:
                    System.out.println("Encerrando a aplicação!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }


    private void pesquisarDadosDoArtista() {
        System.out.print("Pesquisar sobre qual artista? ");
        var nome = scan.nextLine();
        var resposta = ConsultaGeminiAI.obterInformacao(nome);
        System.out.println(resposta.trim());

    }


    private void buscarMusicasPorArtista() {
        System.out.println("Buscar musicas de qual artista? ");
        var nome = scan.nextLine();
        List<Musica> musicas = repositorio.buscaMusicasPorArtista(nome);
        musicas.forEach(System.out::println);
    }


    private void listarMusicas() {
        List<Artista> artistas = repositorio.findAll();
        artistas.forEach(a -> a.getMusicas().forEach(System.out::println));
    }


    private void cadastrarMusicas() {
        System.out.print("Cadastrar musica de qual artista? ");
        var nome = scan.nextLine();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nome);
        if(artista.isPresent()) {
            System.out.print("Informe o nome da musica: ");
            var nomeMusica = scan.nextLine();
            Musica musica = new Musica(nomeMusica);
            musica.setArtista(artista.get());
            artista.get().getMusicas().add(musica);
            repositorio.save(artista.get());
        } else {
            System.out.println("\nArtista nao encontrado!");
        }
    }


    private void cadastrarArtistas() {
        var cadastrarOutro = "S";

        while (cadastrarOutro.equalsIgnoreCase("s")) {

            System.out.print("Informe o nome do artista: ");
            var nome = scan.nextLine();
            System.out.print("Qual o tipo desse artista? (solo, dupla ou banda) ");
            var tipo = scan.nextLine();
            TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());
            Artista artista = new Artista(nome, tipoArtista);
            repositorio.save(artista);
            System.out.print("\nCadastrar outro artista? (S/N) ");
            cadastrarOutro = scan.nextLine();
        }
    }


}
