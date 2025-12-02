package Class;
import java.sql.Timestamp;

public class HistoricoVenda {
    private int id;
    private int idVenda;    
    private int idLogin;    
    private Timestamp dataRegistro;
    private String novoStatus;

    public HistoricoVenda(int id, int idVenda, int idLogin, Timestamp dataRegistro, String novoStatus) {
        this.id = id;
        this.idVenda = idVenda;
        this.idLogin = idLogin;
        this.dataRegistro = dataRegistro;
        this.novoStatus = novoStatus;
    }

    public HistoricoVenda(int idVenda, int idLogin, String novoStatus) {
        this.idVenda = idVenda;
        this.idLogin = idLogin;
        this.novoStatus = novoStatus;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIdVenda() { return idVenda; }
    public int getIdLogin() { return idLogin; }
    public String getNovoStatus() { return novoStatus; }
    public void setNovoStatus(String novoStatus) { this.novoStatus = novoStatus; }


    @Override
    public String toString() {
        return "Historico [ID=" + id + ", Venda ID=" + idVenda + ", Usuário=" + idLogin + 
               ", Data=" + dataRegistro + ", Novo Status=" + novoStatus + "]";
    }
}