package Class;

public class ItensVenda {
    private int idVenda; 
    private int idProduto;
    
    private float quantidade;
    private double precoUnitario; 

    public ItensVenda(int idVenda, int idProduto, float quantidade, double precoUnitario) {
        this.idVenda = idVenda;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }
    
    public ItensVenda(int idProduto, float quantidade, double precoUnitario) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }
    
    public int getIdVenda() { 
    	return idVenda; 
    }
    public void setIdVenda(int idVenda) { 
    	this.idVenda = idVenda; 
    }
    
    public int getIdProduto() { 
    	return idProduto; 
    }
    public void setIdProduto(int idProduto) { 
    	this.idProduto = idProduto; 
    }
    
    public float getQuantidade() { 
    	return quantidade; 
    }
    public void setQuantidade(float quantidade) { 
    	this.quantidade = quantidade; 
    }
    
    public double getPrecoUnitario() { 
    	return precoUnitario; 
    }
    public void setPrecoUnitario(double precoUnitario) { 
    	this.precoUnitario = precoUnitario; 
    }

    @Override
    public String toString() {
        return "ItemVenda [Venda ID=" + idVenda + ", Produto ID=" + idProduto + 
               ", Qtd=" + quantidade + ", Preço Unitário=" + precoUnitario + "]";
    }
}