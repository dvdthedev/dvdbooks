package com.dvdthedev.dvdbooks.model;

public enum Idioma {
    PT("pt"),
    EN("en"),
    FR("fr"),
    ES("es");

    private String linguagem;

    Idioma(String linguagem){
        this.linguagem = linguagem;
    }

    public static Idioma fromString(String text){
        for(Idioma lingua : Idioma.values()){
            if(lingua.linguagem.equalsIgnoreCase(text)){
                return lingua;
            }

        }
        throw new IllegalArgumentException("Nenhuma lingua encontrada para a string fornecida: " + text);
    }
}
