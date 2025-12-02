package Class;

import java.sql.Timestamp;

public class Estoque {
	private int id;
    private int idProduto;
    private float quantidade;
    private String unidade;
    private Timestamp dataUltMovimento;

    public Estoque(int id, int idProduto, float quantidade, String unidade, Timestamp dataUltMovimento) {
        this.id = id;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.dataUltMovimento = dataUltMovimento;
    }

    public Estoque(int idProduto, float quantidade, String unidade) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.unidade = unidade;
    }
    
    public int getId() { 
    	return id; 
    }
    public int getIdProduto() { 
    	return idProduto; 
    }
    public float getQuantidade() { 
    	return quantidade; 
    }
    public String getUnidade() { 
    	return unidade; 
    }
    public Timestamp getDataUltMovimento() { 
    	return dataUltMovimento; 
    }
    
    public void setId(int id) { 
    	this.id = id; 
    }
    public void setQuantidade(float quantidade) { 
    	this.quantidade = quantidade; 
    }
    public void setUnidade(String unidade) { 
    	this.unidade = unidade; 
    }

    @Override
    public String toString() {
        return "Estoque [ID=" + id + ", Produto=" + idProduto + ", Quantidade=" + quantidade + " " + unidade + 
               ", Última Movimentação=" + dataUltMovimento + "]";
    }
}
