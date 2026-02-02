package Modelo;

public class PedidoItem {

    private int idProduto;
    private String nome;
    private int quantidade;
    private double subtotal;

    public PedidoItem() {}

    public PedidoItem(int idProduto, String nome, int quantidade, double subtotal) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

   
    public String getNome() {
        return nome;
    }

    
    public void setNome(String nome) {
        this.nome = nome;
    }
}
