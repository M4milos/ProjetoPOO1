package Unidades;

public enum UnidadeMedida {
    UNIDADE("Unidade", "UN"),
    PECA("Peça", "PC"),
    CONJUNTO("Conjunto", "CJ"),
    
    QUILOGRAMA("Quilograma", "KG"),
    GRAMA("Grama", "GR"),
    
    LITRO("Litro", "LT"),
    MILILITRO("Mililitro", "ML"),
    
    METRO("Metro", "M"),
    CENTIMETRO("Centímetro", "CM"),
    
    CAIXA("Caixa", "CX"),
    PACOTE("Pacote", "PCT");

    private final String nomeCompleto;
    private final String sigla;

    UnidadeMedida(String nomeCompleto, String sigla) {
        this.nomeCompleto = nomeCompleto;
        this.sigla = sigla;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }
    
    public String getSigla() {
        return sigla;
    }
}
