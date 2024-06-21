package com.dvdthedev.dvdbooks.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Array;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosRequisicao(@JsonAlias("results") List<DadosLivro> results) {
}
