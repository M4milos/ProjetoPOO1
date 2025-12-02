package Class;

public class Sessao {
    private int idUsuarioLogado;
    private String username;
    private int permissionLevel; 
    
    public Sessao(int idUsuarioLogado, String username, int permissionLevel) {
        setIdUsuarioLogado(idUsuarioLogado);
        setUsername(username);
        setPermissionLevel(permissionLevel);
    }
    
    public int getIdUsuarioLogado() {
		return idUsuarioLogado;
	}

	public void setIdUsuarioLogado(int idUsuarioLogado) {
		this.idUsuarioLogado = idUsuarioLogado;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPermissionLevel() {
		return permissionLevel;
	}

	public void setPermissionLevel(int permissionLevel) {
		this.permissionLevel = permissionLevel;
	}

    public boolean isLogado() {
        return idUsuarioLogado > 0;
    }
}