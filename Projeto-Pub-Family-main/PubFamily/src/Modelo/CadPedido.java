package Modelo;

import java.util.ArrayList;
import java.util.List;

public class CadPedido {

    private int id;
    private int idMesa;
    private int idCliente;
    private double precoTotal;
    private List<PedidoItem> itens;

    public CadPedido() {
        this.itens = new ArrayList<>();
    }

    public CadPedido(int idMesa, int idCliente) {
        this.idMesa = idMesa;
        this.idCliente = idCliente;
        this.itens = new ArrayList<>();
        this.precoTotal = 0.0;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public List<PedidoItem> getItens() {
        return itens;
    }

    public void setItens(List<PedidoItem> itens) {
        this.itens = itens;
    }
}
