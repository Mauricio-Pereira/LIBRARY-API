package br.com.fiap.library_api.model;

public enum Categoria {
    ROMANCE("Romance"),
    FICCAO("Ficção"),
    BIOGRAFIA("Biografia"),
    PSICOLOGIA("Psicologia"),
    TERROR("Terror"),
    PROGRAMACAO("Programação"),
    AUTOAJUDA("Autoajuda");

    private String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Categoria{");
        sb.append("descricao='").append(descricao).append('\'');
        sb.append('}');
        return sb.toString();
    }
}