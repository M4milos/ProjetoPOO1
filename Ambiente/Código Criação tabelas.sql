CREATE SCHEMA IF NOT EXISTS gestaoprodutos;
USE gestaoprodutos;

CREATE TABLE IF NOT EXISTS Login (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    pass_hash VARCHAR(255) NOT NULL, 
    permission INT NOT NULL DEFAULT 1, 
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Produto (
    id INT NOT NULL AUTO_INCREMENT,
    id_login INT NOT NULL,
    codigo VARCHAR(255) UNIQUE NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    preco_compra DECIMAL(10 , 2 ),
    preco_venda DECIMAL(10 , 2 ) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_login)
        REFERENCES Login (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Estoque (
    id INT NOT NULL AUTO_INCREMENT,
    id_produto INT UNIQUE NOT NULL, 
    quantidade FLOAT NOT NULL,
    unidade VARCHAR(10) NOT NULL,
    data_ult_movimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    PRIMARY KEY (id),
    
    FOREIGN KEY (id_produto) 
    REFERENCES Produto(id)
    ON DELETE CASCADE 
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Venda (
    id INT NOT NULL AUTO_INCREMENT,
    id_login INT NOT NULL,
    data_venda DATETIME NOT NULL,
    total_venda DECIMAL(10, 2) NOT NULL, 
    status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE',
    
    PRIMARY KEY (id),
    FOREIGN KEY (id_login)
    REFERENCES Login(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Itens_Venda (
    id_venda INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade FLOAT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL, 
    
    PRIMARY KEY (id_venda, id_produto), 
    
    FOREIGN KEY (id_venda) 
    REFERENCES Venda(id)
    ON DELETE CASCADE 
    ON UPDATE CASCADE,
    
    FOREIGN KEY (id_produto) 
    REFERENCES Produto(id)
    ON DELETE RESTRICT 
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Historico_Venda (
    id INT NOT NULL AUTO_INCREMENT,
    id_venda INT NOT NULL, 
    id_login INT NOT NULL, 
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    novo_status VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (id),
    
    FOREIGN KEY (id_venda) 
    REFERENCES Venda(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

    FOREIGN KEY (id_login)
    REFERENCES Login(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);