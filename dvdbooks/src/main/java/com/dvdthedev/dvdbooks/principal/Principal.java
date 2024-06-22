package com.dvdthedev.dvdbooks.principal;

import com.dvdthedev.dvdbooks.model.Autor;
import com.dvdthedev.dvdbooks.model.DadosRequisicao;
import com.dvdthedev.dvdbooks.model.Idioma;
import com.dvdthedev.dvdbooks.model.Livro;
import com.dvdthedev.dvdbooks.repository.AutorRepository;
import com.dvdthedev.dvdbooks.service.ConsumoApi;
import com.dvdthedev.dvdbooks.service.ConverteDados;

import java.util.*;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private AutorRepository repositorio;

    public Principal(AutorRepository repositorio) {
        this.repositorio = repositorio;
    }


    private List<Autor> autores = new ArrayList<>();
    private Optional<Autor> autorOptional;





        public void exibeMenu () {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    Escolha uma opção:
                    1 - Buscar livro pelo título
                    2 - Listar livros registados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    6 - Listar os 5 livros mais baixados
                                                        
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivosPorAno();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 6:
                    listarTop5Livros();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }


        private void buscarLivro () {
        List<Livro> livros = new ArrayList<>();
        System.out.println("Digite o nome do livro desejado");
        var nomeLivro = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeLivro.toLowerCase().replace(" ", "+"));
        //nomeLivro.replace(" ", "+").toLowerCase()
        var dados = conversor.obterDados(json, DadosRequisicao.class);

        try {
            if (!dados.results().isEmpty()) {


                var dadoLivroAutor = dados.results().get(0).autores().get(0);
                var dadosDoLivro = dados.results().get(0);

                Autor autor = new Autor(dadoLivroAutor.nome(), dadoLivroAutor.anoNascimento(), dadoLivroAutor.anoFalescimento());
                Livro novoLivro = new Livro(dadosDoLivro.titulo(), dadosDoLivro.idiomas(), dadosDoLivro.numeroDownloads(), autor);
                livros.add(novoLivro);

                autorOptional = repositorio.findByNomeContainingIgnoreCase(autor.getNome().toLowerCase());


                if (!autorOptional.isPresent()) {
                    System.out.println("Adicionando autor: " + autor.getNome());
                    autor.setLivros(livros);
                    repositorio.save(autor);


                } else {
                    System.out.println("Autor já está no repositório");
                    autorOptional.get().setLivros(livros);
                    repositorio.save(autorOptional.get());
                }

            } else System.out.println("Nenhum livro encontrado");
        } catch (IndexOutOfBoundsException e){
            System.out.println("Autor desconhecido");
        }

    }

        private void listarLivros () {


        List<Livro> livros = repositorio.buscarLivros();
        livros.stream()
                .forEach(a -> System.out.printf(String.format("""
                                -----Livro-----
                                Título: %s
                                Autor: %s
                                Idioma: %s
                                Número de downloads: %d
                                ---------------
                                """, a.getTitulo(),
                        a.getAutor().getNome(),
                        a.getIdioma(),
                        a.getNumeroDownloads())));


    }

        private void listarAutores () {
        autores = repositorio.buscarAutores();
        autores.stream()
                .forEach(a -> System.out.println(String.format("""
                        \nAutor: %s
                        Ano de nascimento: %d
                        Ano de falecimento: %d
                        Livros: %s
                        """, a.getNome(), a.getAnoNascimento(), a.getAnoFalecimento(), repositorio.listaDeLivrosPorAutor(a.getNome()))));

    }

        private void listarAutoresVivosPorAno () {
        System.out.println("Insira um ano para listar os autores vivos.");
        int ano = leitura.nextInt();
        leitura.nextLine();
        autores = repositorio.buscarAutoresVivosPorAno(ano);
        autores.stream()
                .filter(b -> b.getAnoNascimento() != 0 && b.getAnoFalecimento() != 0)
//                .filter(c -> c.getAnoNascimento() < ano && c.getAnoFalecimento() > ano)
                .forEach(a -> System.out.println(String.format("""
                        \nAutor: %s
                        Ano de nascimento: %d
                        Ano de falecimento: %d
                        Livros: %s
                        """, a.getNome(), a.getAnoNascimento(), a.getAnoFalecimento(), repositorio.listaDeLivrosPorAutor(a.getNome()))));
    }
        private void listarLivrosPorIdioma () {
        System.out.println("""
                Insira um idioma para busca:
                es - espanhol
                en - inglês
                fr - francês
                pt - português
                """);
        var idioma = leitura.nextLine();
        List<Livro> livrosPorIdioma = repositorio.listarLivrosPorIdioma(Idioma.fromString(idioma));
        livrosPorIdioma.stream()
                .forEach(l -> System.out.println(String.format("""
                        -----Livro-----
                        Título: %s
                        Autor: %s                        
                        Idioma: %s                        
                        Número de downloads: %d                                                
                        ---------------
                        """, l.getTitulo(), l.getAutor().getNome(), l.getIdioma(), l.getNumeroDownloads())));


    }

        private void listarTop5Livros () {
        List<Livro> top5Livros = repositorio.listarTop5Livros();
        top5Livros.stream()
                .forEach(l -> System.out.println(String.format("""
                        -----Livro-----
                        Título: %s
                        Autor: %s                        
                        Idioma: %s                        
                        Número de downloads: %d                                                
                        ---------------
                        """, l.getTitulo(), l.getAutor().getNome(), l.getIdioma(), l.getNumeroDownloads())));
    }


//    private void listarLivrosPorIdioma() {
//        System.out.println("""
//                Insira um idioma para busca:
//                es - espanhol
//                en - inglês
//                fr - francês
//                pt - português
//                """);
//        var idioma = leitura.nextLine();
//        autores = repositorio.findAll();
//        autores.stream()
//                .filter(b -> b.getLivros().get(0).getIdioma() == Idioma.fromString(idioma))
//                .forEach(a -> System.out.printf(String.format("""
//                        -----Livro-----
//                        Título: %s
//                        Autor: %s
//                        Idioma: %s
//                        Número de downloads: %d
//                        ---------------
//                        """, a.getLivros().get(0).getTitulo(),
//                        a.getNome(),
//                        a.getLivros().get(0).getIdioma(),
//                        a.getLivros().get(0).getNumeroDownloads())));


    }
