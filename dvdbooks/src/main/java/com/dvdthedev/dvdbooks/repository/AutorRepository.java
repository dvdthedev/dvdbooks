package com.dvdthedev.dvdbooks.repository;

import com.dvdthedev.dvdbooks.model.Autor;
import com.dvdthedev.dvdbooks.model.Idioma;
import com.dvdthedev.dvdbooks.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNomeContainingIgnoreCase(String nome);
    @Query("SELECT l.titulo FROM Livro l JOIN l.autor a WHERE a.nome LIKE %:nome%")
    List<String> listaDeLivrosPorAutor(String nome);

    @Query("Select l from Livro l where l.idioma = :idioma")
    List<Livro> listarLivrosPorIdioma(Idioma idioma);

    @Query("Select a from Autor a")
    List<Autor> buscarAutores();

    @Query("Select l from Livro l")
    List<Livro> buscarLivros();

    @Query("Select a from Autor a WHERE a.anoNascimento <= :ano AND a.anoFalecimento >= :ano")
    List<Autor> buscarAutoresVivosPorAno(int ano);

    @Query("Select l from Livro l ORDER BY l.numeroDownloads DESC LIMIT 5")
    List<Livro> listarTop5Livros();
}
