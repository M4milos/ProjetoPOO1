package Class;

public class Produto {
    private int id;
    private int idLogin;
    private String codigo;
    private String descricao;
    private double precoCompra;
    private double precoVenda;

    public Produto(int id, int idLogin, String codigo, String descricao, double precoCompra, double precoVenda) {
        this.id = id;
        this.idLogin = idLogin;
        this.codigo = codigo;
        this.descricao = descricao;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
    }
    
    public Produto(int idLogin, String codigo, String descricao, double precoCompra, double precoVenda) {
        this.idLogin = idLogin;
        this.codigo = codigo;
        this.descricao = descricao;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdLogin() {
		return idLogin;
	}

	public void setIdLogin(int idLogin) {
		this.idLogin = idLogin;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getPrecoCompra() {
		return precoCompra;
	}

	public void setPrecoCompra(double precoCompra) {
		this.precoCompra = precoCompra;
	}

	public double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(double precoVenda) {
		this.precoVenda = precoVenda;
	}

	@Override
    public String toString() {
        return "Produto [ID=" + id + ", Código=" + codigo + ", Descrição=" + descricao + ", Preço Venda=" + precoVenda + "]";
    }
}