package com.dvdthedev.dvdbooks.service;

public interface IConverteDados {
    <T> T  obterDados(String json, Class<T> classe);
}
