package Class;

import java.sql.Timestamp;

public class Venda {
    private int id;
    private int idLogin;
    private Timestamp dataVenda;
    private double totalVenda;
    private String status;

    public Venda(int id, int idLogin, Timestamp dataVenda, double totalVenda, String status) {
        this.id = id;
        this.idLogin = idLogin;
        this.dataVenda = dataVenda;
        this.totalVenda = totalVenda;
        this.status = status;
    }
    
    public Venda(int idLogin, double totalVenda, String status) {
        this.idLogin = idLogin;
        this.totalVenda = totalVenda;
        this.status = status;
    }

    public Venda(double totalVenda, String status) {
        this.totalVenda = totalVenda;
        this.status = status;
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


	public Timestamp getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Timestamp dataVenda) {
		this.dataVenda = dataVenda;
	}

	public double getTotalVenda() {
		return totalVenda;
	}

	public void setTotalVenda(double totalVenda) {
		this.totalVenda = totalVenda;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
    public String toString() {
        return "Venda [ID=" + id + ", Data=" + dataVenda + ", Total=" + totalVenda + ", Status=" + status + "]";
    }
}